package de.manetmodel.app.gui;

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
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.manetmodel.algo.RandomPath;
import de.manetmodel.app.gui.math.Line2D;
import de.manetmodel.app.gui.math.Point2D;
import de.manetmodel.app.gui.math.VectorLine2D;
import de.manetmodel.app.gui.visualgraph.VisualEdge;
import de.manetmodel.app.gui.visualgraph.VisualEdgeDistanceTextBuilder;
import de.manetmodel.app.gui.visualgraph.VisualGraph;
import de.manetmodel.app.gui.visualgraph.VisualGraphMarkUp;
import de.manetmodel.app.gui.visualgraph.VisualPath;
import de.manetmodel.app.gui.visualgraph.VisualVertex;
import de.manetmodel.graph.Coordinate;
import de.manetmodel.graph.Edge;
import de.manetmodel.graph.Playground;
import de.manetmodel.graph.Vertex;
import de.manetmodel.graph.WeightedUndirectedGraph;
import de.manetmodel.graph.WeightedUndirectedGraphSupplier;
import de.manetmodel.graph.Playground.DoubleRange;
import de.manetmodel.graph.Playground.IntRange;
import de.manetmodel.graph.generator.GraphGenerator;
import de.manetmodel.util.RandomNumbers;

public class VisualGraphPanel<V extends Vertex, E extends Edge> extends JPanel {

    private VisualGraph<V, E> graph;
    private Scope scope;
    private double xScale;
    private double yScale;
    private int vertexWidth = 50;
    private int padding = vertexWidth;
    private static Stroke EDGE_STROKE = new BasicStroke(1);
    private static BasicStroke EDGE_PATH_STROKE = new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
	    0.0f, new float[] { 10.0f, 2.0f }, 0);
    private static Stroke VERTEX_STROKE = new BasicStroke(2);
    private static Stroke VERTEX_PATH_STROKE = new BasicStroke(4);

    public VisualGraphPanel() {

    }

    public VisualGraphPanel(VisualGraph<V, E> graph) {
	this.graph = graph;
	this.scope = this.getScope(graph);
    }

    public void paintPlayground(Graphics2D g2) {

	g2.setColor(Color.LIGHT_GRAY);
	String str = "ManetModel v1.0";
	FontMetrics fontMetrics = g2.getFontMetrics();
	Rectangle2D stringBounds = fontMetrics.getStringBounds(str, g2);
	g2.drawString(str, padding, padding/2 + (int)(stringBounds.getHeight()/2));

	g2.setColor(Color.WHITE);
	g2.fillRect(padding, padding, getWidth() - (2 * padding), getHeight() - (2 * padding));

	g2.setStroke(EDGE_STROKE);
	g2.setColor(Color.LIGHT_GRAY);

	Line2D xAxis = new Line2D(0 * xScale + padding, (scope.y.max - 0) * yScale + padding,
		scope.x.max * xScale + padding, scope.y.max * yScale + padding);

	g2.drawLine((int) xAxis.p1().x(), (int) xAxis.p1().y(), (int) xAxis.p2().x(), (int) xAxis.p2().y());

	VectorLine2D xAxisVector = new VectorLine2D(xAxis.p1().x(), xAxis.p1().y(), xAxis.getSlope());

	Line2D yAxis = new Line2D(0 * xScale + padding, (scope.y.max - 0) * yScale + padding,
		scope.x.min * xScale + padding, scope.y.min * yScale + padding);

	g2.drawLine((int) yAxis.p1().x(), (int) yAxis.p1().y(), (int) yAxis.p2().x(), (int) yAxis.p2().y());

	VectorLine2D yAxisVector = new VectorLine2D(yAxis.p1().x(), yAxis.p1().y(), yAxis.getSlope());

	DecimalFormat decimalFormat = new DecimalFormat("#.00");
	int steps = 20;
	int xOffset = (int) xAxis.getLength() / steps;
	int yOffset = (int) yAxis.getLength() / steps;

	Color gridLineColor = new Color(245, 245, 245);

	for (int i = 1; i < steps; i++) {

	    g2.setColor(Color.GRAY);

	    Point2D xAxisPoint = xAxisVector.getPointInDistance(i * xOffset);
	    g2.drawLine((int) xAxisPoint.x(), (int) xAxisPoint.y(), (int) xAxisPoint.x(),
		    (int) xAxisPoint.y() + padding / 4);

	    Point2D yAxisPoint = yAxisVector.getPointInDistance(i * -yOffset);
	    g2.drawLine((int) yAxisPoint.x(), (int) yAxisPoint.y(), (int) yAxisPoint.x() - padding / 4,
		    (int) yAxisPoint.y());

	    String xAxisPointText = decimalFormat.format(i * (scope.x.max / steps));
	    fontMetrics = g2.getFontMetrics();
	    stringBounds = fontMetrics.getStringBounds(xAxisPointText, g2);
	    g2.drawString(xAxisPointText, (int) xAxisPoint.x() - (int) stringBounds.getCenterX(),
		    (int) xAxisPoint.y() - (int) stringBounds.getCenterY() + padding / 2);

	    String yAxisPointText = decimalFormat.format(i * (scope.y.max / steps));
	    fontMetrics = g2.getFontMetrics();
	    stringBounds = fontMetrics.getStringBounds(yAxisPointText, g2);
	    g2.drawString(yAxisPointText, (int) yAxisPoint.x() - (int) stringBounds.getCenterX(),
		    (int) yAxisPoint.y() - (int) stringBounds.getCenterY() - padding / 2);

	    g2.setColor(gridLineColor);

	    g2.drawLine((int) xAxisPoint.x(), (int) xAxisPoint.y(), (int) xAxisPoint.x(),
		    (int) (xAxisPoint.y() - (scope.y.max * yScale)));

	    g2.drawLine((int) yAxisPoint.x(), (int) yAxisPoint.y(), (int) (yAxisPoint.x() + (scope.x.max * xScale)),
		    (int) (yAxisPoint.y()));
	}
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
		g2.drawLine((int) pathStartPosition.x(), (int) pathStartPosition.y(), (int) pathTargetPosition.x(),
			(int) pathTargetPosition.y());

		i++;
	    }
	} else {

	    g2.setStroke(EDGE_STROKE);
	    g2.setColor(Color.GRAY);
	    g2.drawLine(edgeLine.x1().intValue(), edgeLine.y1().intValue(), edgeLine.x2().intValue(),
		    edgeLine.y2().intValue());
	}

	// Edge Text

	Point2D lineCenter = new Point2D(edgeLine.x1() / 2 + edgeLine.x2() / 2, edgeLine.y1() / 2 + edgeLine.y2() / 2);
	FontMetrics fontMetrics = g2.getFontMetrics();
	Rectangle2D stringBounds = fontMetrics.getStringBounds(edge.getText(), g2);
	g2.setColor(edge.getColor());
	g2.drawString(edge.getText(), (int) lineCenter.x() - (int) stringBounds.getCenterX(),
		(int) lineCenter.y() - (int) stringBounds.getCenterY());
    }

    public void paintTrainsmissionRange(Graphics2D g2, VisualVertex vertex) {

	int x = (int) ((vertex.getPosition().x() * xScale + padding) - vertexWidth / 2);
	int y = (int) (((scope.y.max - vertex.getPosition().y()) * yScale + padding) - vertexWidth / 2);

	int transmissionRange = 250;
	g2.setStroke(EDGE_STROKE);
	g2.setColor(new Color(155, 155, 155, 75));
	g2.fillOval(x - (transmissionRange / 2 - (vertexWidth / 2)), y - (transmissionRange / 2 - (vertexWidth / 2)),
		transmissionRange, transmissionRange);
	// g2.drawOval(x - (transmissionRange/2-(vertexWidth /2)) , y -
	// (transmissionRange/2-(vertexWidth/2)), transmissionRange, transmissionRange);

    }

    public void paintVertex(Graphics2D g2, VisualVertex vertex) {
	int x = (int) ((vertex.getPosition().x() * xScale + padding) - vertexWidth / 2);
	int y = (int) (((scope.y.max - vertex.getPosition().y()) * yScale + padding) - vertexWidth / 2);

	g2.setColor(vertex.getBackgroundColor());
	g2.fillOval(x, y, vertexWidth, vertexWidth);

	int offset = 0;
	int offsetSteps = 6;

	if (!vertex.getVisualPaths().isEmpty()) {

	    int angle = 360 / vertex.getVisualPaths().size();
	    int angleOffset = 0;

	    for (VisualPath visualPath : vertex.getVisualPaths()) {
		/*
		 * g2.setStroke(VERTEX_PATH_STROKE); g2.setColor(visualPath.getColor());
		 * g2.drawOval(x - offset/2, y - offset/2, vertexWidth + offset, vertexWidth +
		 * offset); offset += offsetSteps;
		 */

		g2.setStroke(new BasicStroke(2 + vertex.getVisualPaths().size()));
		g2.setColor(visualPath.getColor());
		g2.drawArc(x, y, vertexWidth, vertexWidth, angleOffset, angle);
		angleOffset += angle;
	    }
	} else {
	    g2.setStroke(VERTEX_STROKE);
	    g2.setColor(Color.GRAY);
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
		Playground playground = new Playground(2000, 2000, new IntRange(150, 200), new DoubleRange(100d, 150d),
			new IntRange(2, 4), new DoubleRange(100d, 150));

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
