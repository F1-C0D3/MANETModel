package de.manetmodel.app.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;

import de.manetmodel.app.gui.visualgraph.VisualGraph;
import de.manetmodel.graph.Position2D;
import de.manetmodel.graph.Vertex;
import de.manetmodel.graph.WeightedEdge;

public class VisualGraphFrame<V extends Vertex<Position2D>, E extends WeightedEdge<?>> extends JFrame {

    Terminal terminal;
    VisualGraphPanel<V, E> visualGraphPanel;

    public VisualGraphFrame(VisualGraph<V, E> graph) {

	this.setLayout(new BorderLayout());

	this.visualGraphPanel = new VisualGraphPanel<V, E>(graph);
	this.visualGraphPanel.setFont(new Font("NotoSans", Font.PLAIN, 14));
	this.visualGraphPanel.setLayout(new OverlayLayout(this.visualGraphPanel));
	
	this.terminal = new Terminal(new Font("Monospace", Font.PLAIN, 16), Color.WHITE);
	this.terminal.setFont(new Font("Consolas", Font.PLAIN, 16));
	this.terminal.setOpaque(false);
	this.terminal.setBackground(new Color(0, 0, 0, 100));
	this.terminal.setVisible(true);
	this.terminal.addComponentListener(new ComponentAdapter() {
	    @Override
	    public void componentShown(ComponentEvent e) {
		terminal.textArea.requestFocus();
		;
	    }
	});

	this.visualGraphPanel.add(this.terminal);
	this.add(visualGraphPanel);

	this.visualGraphPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"),
		VisualGraphFrameActions.TOGGLE_TERMINAL);
	this.visualGraphPanel.getActionMap().put(VisualGraphFrameActions.TOGGLE_TERMINAL,
		new VisualGraphFrameAction(VisualGraphFrameActions.TOGGLE_TERMINAL));
    }

    public void scale(double scaleFactor) {

	Font font = visualGraphPanel.getFont();
	visualGraphPanel.setFont(new Font(font.getName(), font.getStyle(), (int) (font.getSize() * scaleFactor)));
	visualGraphPanel.setVertexWidth((int) (visualGraphPanel.getVertexWidth() * scaleFactor));
	visualGraphPanel.repaint();

	font = terminal.getTextArea().getFont();
	terminal.getTextArea().setFont(new Font(font.getName(), font.getStyle(), (int) (font.getSize() * scaleFactor)));
	terminal.repaint();
    }

    public VisualGraphPanel<V, E> getVisualGraphPanel() {
	return this.visualGraphPanel;
    }

    public Terminal getTerminal() {
	return this.terminal;
    }

    private enum VisualGraphFrameActions {
	TOGGLE_TERMINAL;
    }

    private class VisualGraphFrameAction extends AbstractAction {

	VisualGraphFrameActions action;

	public VisualGraphFrameAction(VisualGraphFrameActions action) {
	    this.action = action;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    switch (action) {
	    case TOGGLE_TERMINAL:
		if (terminal.isVisible()) {
		    terminal.setVisible(false);
		} else {
		    terminal.setVisible(true);
		}
		break;
	    default:
		break;
	    }
	}
    }

    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {

		/*
		 * WeightedUndirectedGraph<Vertex, Edge> graph = new
		 * WeightedUndirectedGraph<Vertex, Edge>( new
		 * WeightedUndirectedGraphSupplier.VertexSupplier(), new
		 * WeightedUndirectedGraphSupplier.EdgeSupplier());
		 * 
		 * GraphGenerator<Vertex, Edge> generator = new GraphGenerator<Vertex,
		 * Edge>(graph); NetworkGraphProperties properties = new
		 * NetworkGraphProperties(1024, 768, new IntRange(100, 200), new
		 * DoubleRange(50d, 100d), 100); generator.generateNetworkGraph(properties);
		 * 
		 * VisualGraph<Vertex, Edge> visualGraph = new VisualGraph<Vertex, Edge>(graph,
		 * new VisualGraphMarkUp<Edge>(new VisualEdgeDistanceTextBuilder<Edge>()));
		 * 
		 * RandomPath<Vertex, Edge> randomPath = new RandomPath<Vertex, Edge>(graph);
		 * 
		 * for (int i = 1; i <= 20; i++) visualGraph.addVisualPath(randomPath
		 * .compute(graph.getVertex(RandomNumbers.getRandom(0,
		 * graph.getVertices().size())), 5));
		 * 
		 * VisualGraphFrame<Vertex, Edge> frame = new VisualGraphFrame<Vertex,
		 * Edge>(visualGraph); Dimension screenSize =
		 * Toolkit.getDefaultToolkit().getScreenSize(); int width = (int)
		 * screenSize.getWidth() * 3 / 4; int height = (int) screenSize.getHeight() * 3
		 * / 4; frame.setPreferredSize(new Dimension(width, height));
		 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); frame.pack();
		 * frame.setLocationRelativeTo(null); frame.setVisible(true);
		 */
	    }
	});
    }
}
