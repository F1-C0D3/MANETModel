package de.manetmodel.app.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;

import de.manetmodel.algo.RandomPath;
import de.manetmodel.app.gui.visualgraph.VisualEdgeDistanceTextBuilder;
import de.manetmodel.app.gui.visualgraph.VisualGraph;
import de.manetmodel.app.gui.visualgraph.VisualGraphMarkUp;
import de.manetmodel.graph.Edge;
import de.manetmodel.graph.Playground;
import de.manetmodel.graph.Vertex;
import de.manetmodel.graph.WeightedUndirectedGraph;
import de.manetmodel.graph.WeightedUndirectedGraphSupplier;
import de.manetmodel.graph.Playground.DoubleRange;
import de.manetmodel.graph.Playground.IntRange;
import de.manetmodel.graph.generator.GraphGenerator;
import de.manetmodel.util.RandomNumbers;

public class VisualGraphFrame<V extends Vertex, E extends Edge> extends JFrame {

    TerminalPanel terminalPanel;
    VisualGraphPanel<V, E> visualGraphPanel;

    public VisualGraphFrame(VisualGraph<V, E> graph) {

	this.setLayout(new BorderLayout());

	this.visualGraphPanel = new VisualGraphPanel<V, E>(graph);
	this.visualGraphPanel.setFont(new Font("Consolas", Font.PLAIN, 16));
	this.visualGraphPanel.setLayout(new OverlayLayout(this.visualGraphPanel));

	this.terminalPanel = new TerminalPanel();
	this.terminalPanel.setFont(new Font("Consolas", Font.PLAIN, 16));
	this.terminalPanel.setOpaque(false);
	this.terminalPanel.setBackground(new Color(0, 0, 0, 100));
	this.terminalPanel.setVisible(true);
	this.terminalPanel.addComponentListener(new ComponentAdapter() {
	    public void componentShown(ComponentEvent e) {
		terminalPanel.textArea.requestFocus();
		;
	    }
	});

	this.visualGraphPanel.add(this.terminalPanel);

	this.add(visualGraphPanel);

	this.visualGraphPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"),
		TerminalActions.TOGGLE);
	this.visualGraphPanel.getActionMap().put(TerminalActions.TOGGLE, new TerminalAction(TerminalActions.TOGGLE));

	this.visualGraphPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"),
		TerminalActions.UP);
	this.visualGraphPanel.getActionMap().put(TerminalActions.UP, new TerminalAction(TerminalActions.UP));

	this.visualGraphPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"),
		TerminalActions.DOWN);
	this.visualGraphPanel.getActionMap().put(TerminalActions.DOWN, new TerminalAction(TerminalActions.DOWN));
    }
    
    public VisualGraphPanel<V,E> getVisualGraphPanel(){
	return this.visualGraphPanel;
    }
    
    public TerminalPanel getTerminalPanel() {
	return this.terminalPanel;
    }
    
    public void addTerminalInputListener(Consumer<String> terminalInputListener) {
	this.terminalPanel.addInputListener(terminalInputListener);
    }

    private enum TerminalActions {
	TOGGLE, UP, DOWN;
    }

    private class TerminalAction extends AbstractAction {

	TerminalActions action;

	TerminalAction(TerminalActions action) {
	    this.action = action;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    switch (action) {
	    case TOGGLE:
		if (terminalPanel.isVisible()) {
		    terminalPanel.setVisible(false);
		} else {
		    terminalPanel.setVisible(true);
		}
		break;
	    case UP:
		break;
	    case DOWN:
		break;
	    default:
		break;
	    }
	}
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

		VisualGraphFrame<Vertex, Edge> frame = new VisualGraphFrame<Vertex, Edge>(visualGraph);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth() * 3 / 4;
		int height = (int) screenSize.getHeight() * 3 / 4;
		frame.setPreferredSize(new Dimension(width, height));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	    }
	});
    }
}
