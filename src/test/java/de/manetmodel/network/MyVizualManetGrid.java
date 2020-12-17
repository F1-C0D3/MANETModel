package de.manetmodel.network;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import de.manetmodel.graph.WeightedUndirectedGraph;
import de.manetmodel.graph.generator.GraphGenerator;
import de.manetmodel.graph.viz.VisualEdgeDistanceTextBuilder;
import de.manetmodel.graph.viz.VisualGraph;
import de.manetmodel.graph.viz.VisualGraphMarkUp;
import de.manetmodel.graph.viz.VisualGraphPanel;
import de.manetmodel.network.radio.IdealRadioModel;

public class MyVizualManetGrid {

    public Manet<Node, Link> manet;

    VisualGraphPanel<Node, Link> panel;

    public MyVizualManetGrid(int numNodes) {

	createManetGrid(numNodes);
	toVisual();

    }

    void createManetGrid(int numNodes) {
	Manet<Node, Link> manet = new Manet<Node, Link>(new ManetSupplier.ManetNodeSupplier(),
		new ManetSupplier.ManetLinkSupplier());
	WeightedUndirectedGraph<Node, Link> graph = manet.getGraph();
	GraphGenerator<Node, Link> generator = new GraphGenerator<Node, Link>(graph);
	generator.generateGridGraph(10000, 10000, 100, numNodes);

	manet.setRadioOccupationModel(new IdealRadioModel(100, 2000000L));
	manet.initialize();

	this.manet = manet;

    }

    void toVisual() {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		VisualGraph<Node, Link> visualGraph = new VisualGraph<Node, Link>(manet.getGraph(),
			new VisualGraphMarkUp<Link>(new VisualEdgeDistanceTextBuilder<Link>()));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int windowWidth = (int) screenSize.getWidth() * 3 / 4;
		int windowHeight = (int) screenSize.getHeight() * 3 / 4;
		VisualGraphPanel<Node, Link> panel = new VisualGraphPanel<Node, Link>(visualGraph);

		panel.setPreferredSize(new Dimension(windowWidth, windowHeight));
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
