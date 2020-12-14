package de.manetmodel.graph.viz;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.manetmodel.algo.RandomPath;
import de.manetmodel.app.gui.math.Line2D;
import de.manetmodel.app.gui.math.Point2D;
import de.manetmodel.app.gui.math.VectorLine2D;
import de.manetmodel.graph.Edge;
import de.manetmodel.graph.Playground;
import de.manetmodel.graph.Vertex;
import de.manetmodel.graph.WeightedUndirectedGraph;
import de.manetmodel.graph.WeightedUndirectedGraphSupplier;
import de.manetmodel.graph.Playground.DoubleRange;
import de.manetmodel.graph.Playground.IntRange;
import de.manetmodel.graph.generator.GraphGenerator;
import de.manetmodel.graph.viz.VisualEdge;
import de.manetmodel.graph.viz.VisualGraph;
import de.manetmodel.graph.viz.VisualVertex;
import de.manetmodel.util.RandomNumbers;

public class VisualGraphPanel<V extends Vertex, E extends Edge> extends JPanel {

    private VisualGraph<V, E> graph;
    private Scope scope;
    private double xScale;
    private double yScale;
    private int vertexWidth = 50;
    private int padding = vertexWidth;
    private static Stroke EDGE_STROKE = new BasicStroke(1);
    private static BasicStroke EDGE_PATH_STROKE = new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0.0f,
	    new float[] { 10.0f, 2.0f }, 0);
    private static Stroke VERTEX_STROKE = new BasicStroke(1);
    private static Stroke VERTEX_PATH_STROKE = new BasicStroke(3);


    public VisualGraphPanel(VisualGraph<V, E> graph) {
	this.graph = graph;
	this.scope = this.getScope(graph);
    }

    public void paintPlayground(Graphics2D g2) {
	g2.setColor(Color.WHITE);
	g2.fillRect(padding, padding, getWidth() - (2 * padding), getHeight() - 2 * padding);
    }

    public void paintEdge(Graphics2D g2, VisualEdge edge) {

	// Edge Line

	Line2D edgeLine = new Line2D(edge.getStartPosition().x() * xScale + padding,
		(scope.y.max - edge.getStartPosition().y()) * yScale + padding,
		edge.getTargetPosition().x() * xScale + padding,
		(scope.y.max - edge.getTargetPosition().y()) * yScale + padding);

	// Edge Visual Paths

	if (!edge.getVisualPaths().isEmpty()) {

	    VectorLine2D startPositionLine = new VectorLine2D(edge.getStartPosition().x() * xScale + padding,
		    (scope.y.max - edge.getStartPosition().y()) * yScale + padding, edgeLine.getPerpendicularSlope());

	    VectorLine2D targetPositionLine = new VectorLine2D(edge.getTargetPosition().x() * xScale + padding,
		    (scope.y.max - edge.getTargetPosition().y()) * yScale + padding, edgeLine.getPerpendicularSlope());

	    int i = 0;
	    int offset = 0;
	    int offsetSteps = 5;

	    for (VisualPath visualPath : edge.getVisualPaths()) {

		Point2D pathStartPosition;
		Point2D pathTargetPosition;

		if (i % 2 == 0) {
		    pathStartPosition = startPositionLine.getPointInDistance(offset);
		    pathTargetPosition = targetPositionLine.getPointInDistance(offset);
		    offset += offsetSteps;

		} else {
		    pathStartPosition = startPositionLine.getPointInDistance(-offset);
		    pathTargetPosition = targetPositionLine.getPointInDistance(-offset);
		}

		g2.setStroke(EDGE_PATH_STROKE);
		g2.setColor(visualPath.getColor());
		g2.drawLine(pathStartPosition.x().intValue(), pathStartPosition.y().intValue(),
			pathTargetPosition.x().intValue(), pathTargetPosition.y().intValue());

		i++;
	    }
	} else {
	    g2.setStroke(EDGE_STROKE);
	    g2.setColor(edge.getColor());
	    g2.drawLine(edgeLine.x1().intValue(), edgeLine.y1().intValue(), edgeLine.x2().intValue(),
		    edgeLine.y2().intValue());
	}

	// Edge Text

	Point2D lineCenter = new Point2D(edgeLine.x1() / 2 + edgeLine.x2() / 2, edgeLine.y1() / 2 + edgeLine.y2() / 2);
	FontMetrics fontMetrics = g2.getFontMetrics();
	Rectangle2D stringBounds = fontMetrics.getStringBounds(edge.getText(), g2);
	g2.setColor(edge.getColor());
	g2.drawString(edge.getText(), lineCenter.x().intValue() - (int) stringBounds.getCenterX(),
		lineCenter.y().intValue() - (int) stringBounds.getCenterY());
    }

    public void paintVertex(Graphics2D g2, VisualVertex vertex) {
	int x = (int) ((vertex.getPosition().x() * xScale + padding) - vertexWidth / 2);
	int y = (int) (((scope.y.max - vertex.getPosition().y()) * yScale + padding) - vertexWidth / 2);

	g2.setColor(vertex.getBackgroundColor());
	g2.fillOval(x, y, vertexWidth, vertexWidth);

	int offset = 0;
	int offsetSteps = 6;

	if (!vertex.getVisualPaths().isEmpty()) {
	    for (VisualPath visualPath : vertex.getVisualPaths()) {
		g2.setStroke(VERTEX_PATH_STROKE);
		g2.setColor(visualPath.getColor());
		g2.drawOval(x - offset/2, y - offset/2, vertexWidth + offset, vertexWidth + offset);
		offset += offsetSteps;
	    }
	} else {
	    g2.setStroke(VERTEX_STROKE);
	    g2.setColor(vertex.getBorderColor());
	    g2.drawOval(x, y, vertexWidth, vertexWidth);
	}

	FontMetrics fm = g2.getFontMetrics();
	Rectangle2D stringBounds = fm.getStringBounds(vertex.getText(), g2);
	Point vertexCenter = new Point(x + (vertexWidth / 2), y + (vertexWidth / 2));
	g2.setColor(Color.BLACK);
	g2.drawString(vertex.getText(), vertexCenter.x - (int) stringBounds.getCenterX(),
		vertexCenter.y - (int) stringBounds.getCenterY());

    }

    @Override
    protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;
	g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

	if (graph.getVertices().isEmpty())
	    return;

	this.xScale = ((double) getWidth() - 2 * padding) / (scope.x.max);
	this.yScale = ((double) getHeight() - 2 * padding) / (scope.y.max);

	this.paintPlayground(g2);

	for (VisualEdge edge : graph.getEdges())
	    this.paintEdge(g2, edge);

	for (VisualVertex vertex : graph.getVertices())
	    this.paintVertex(g2, vertex);
    }

    private class Scope {
	Range x;
	Range y;
	boolean isSet = false;

	@Override
	public String toString() {
	    return String.format("x:%d-%d, y:%d-%d", this.x.min, this.x.max, this.y.min, this.y.max);
	}
    }

    private class Range {
	double min;
	double max;

	public Range(double min, double max) {
	    this.min = min;
	    this.max = max;
	}
    }

    private Scope getScope(VisualGraph<V, E> graph) {

	Scope scope = new Scope();

	for (VisualVertex vertex : graph.getVertices()) {

	    if (!scope.isSet) {
		scope.x = new Range(vertex.getPosition().x(), vertex.getPosition().x());
		scope.y = new Range(vertex.getPosition().y(), vertex.getPosition().y());
		scope.isSet = true;
	    } else {
		if (vertex.getPosition().x() > scope.x.max)
		    scope.x.max = vertex.getPosition().x();
		else if (vertex.getPosition().x() < scope.x.min)
		    scope.x.min = vertex.getPosition().x();

		if (vertex.getPosition().y() > scope.y.max)
		    scope.y.max = vertex.getPosition().y();
		else if (vertex.getPosition().y() < scope.y.min)
		    scope.y.min = vertex.getPosition().y();
	    }
	}

	return scope;
    }

    public VisualGraph<V, E> getVisualGraph() {
	return this.graph;
    }

    public void updateVisualGraph(VisualGraph<V, E> graph) {
	this.graph = graph;
	this.scope = this.getScope(graph);
    }

    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {

		WeightedUndirectedGraph<Vertex, Edge> graph = new WeightedUndirectedGraph<Vertex, Edge>(
			new WeightedUndirectedGraphSupplier.VertexSupplier(),
			new WeightedUndirectedGraphSupplier.EdgeSupplier());

		GraphGenerator<Vertex, Edge> generator = new GraphGenerator<Vertex, Edge>(graph);
		Playground playground = new Playground(1024, 768, new IntRange(50, 100), new DoubleRange(50d, 100d),
			new IntRange(2, 4), new DoubleRange(50d, 100d));
		generator.generateRandomGraph(playground);

		VisualGraph<Vertex, Edge> visualGraph = new VisualGraph<Vertex, Edge>(graph,
			new VisualGraphMarkUp<Edge>(new VisualEdgeDistanceTextBuilder<Edge>()));

		RandomPath<Vertex, Edge> randomPath = new RandomPath<Vertex, Edge>(graph);

		for (int i = 1; i <= 20; i++)
		    visualGraph.addPath(randomPath
			    .compute(graph.getVertex(RandomNumbers.getRandom(0, graph.getVertices().size())), 5));

		VisualGraphPanel<Vertex, Edge> panel = new VisualGraphPanel<Vertex, Edge>(visualGraph);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth() * 3 / 4;
		int height = (int) screenSize.getHeight() * 3 / 4;
		panel.setPreferredSize(new Dimension(width, height));
		panel.setFont(new Font("Consolas", Font.PLAIN, 16));
		panel.setLayout(null);

		JFrame frame = new JFrame("VisualGraphPanel");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	    }
	});
    }
}
