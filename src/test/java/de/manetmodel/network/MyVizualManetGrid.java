package de.manetmodel.network;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import de.manetmodel.graph.generator.GraphGenerator;
import de.manetmodel.graph.viz.VisualEdgeDistanceTextBuilder;
import de.manetmodel.graph.viz.VisualGraph;
import de.manetmodel.graph.viz.VisualGraphMarkUp;
import de.manetmodel.graph.viz.VisualGraphPanel;
import de.manetmodel.network.radio.Propagation;
import de.manetmodel.network.radio.ScalarRadioModel;

public class MyVizualManetGrid {

    public Manet<Node, Link> manet;

    VisualGraphPanel<Node, Link> panel;

    public MyVizualManetGrid(int numNodes) {

	createManetGrid(numNodes);
	toVisual();

    }

    void createManetGrid(int numNodes) {
	Manet<Node, Link> manet = new Manet<Node, Link>(new ManetSupplier.ManetNodeSupplier(),
		new ManetSupplier.ManetLinkSupplier(),
		new ScalarRadioModel(Propagation.pathLoss(125d, Propagation.waveLength(2412000000d)), 0.002d, 1e-11,
			2000000d, 2412000000d));
	GraphGenerator<Node, Link> generator = new GraphGenerator<Node, Link>(manet);
	generator.generateGridGraph(400, 500, 100, numNodes);

	this.manet = manet;

    }

    void toVisual() {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		VisualGraph<Node, Link> visualGraph = new VisualGraph<Node, Link>(manet,
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
