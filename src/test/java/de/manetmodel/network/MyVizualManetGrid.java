package de.manetmodel.network;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import de.manetmodel.app.gui.VisualGraphPanel;
import de.manetmodel.app.gui.visualgraph.VisualGraph;
import de.manetmodel.app.gui.visualgraph.VisualGraphMarkUp;
import de.manetmodel.graph.EdgeDistance;
import de.manetmodel.graph.generator.GridGraphGenerator;
import de.manetmodel.graph.generator.GridGraphProperties;
import de.manetmodel.network.radio.Propagation;
import de.manetmodel.network.radio.ScalarRadioModel;

public class MyVizualManetGrid {

    public MANET<Node<EdgeDistance>, Link<EdgeDistance>, EdgeDistance> manet;

    VisualGraphPanel<Node<EdgeDistance>, Link<EdgeDistance>> panel;

    public MyVizualManetGrid(int numNodes) {

	createManetGrid(numNodes);
	toVisual();

    }

    void createManetGrid(int numNodes) {
	MANET<Node<EdgeDistance>, Link<EdgeDistance>, EdgeDistance> manet = new MANET<Node<EdgeDistance>, Link<EdgeDistance>, EdgeDistance>(
		new ManetSupplier.ManetNodeSupplier(), new ManetSupplier.ManetLinkSupplier(),
		new ScalarRadioModel(Propagation.pathLoss(125d, Propagation.waveLength(2412000000d)), 0.002d, 1e-11,
			2000000d, 2412000000d));
	GridGraphGenerator<Node<EdgeDistance>, Link<EdgeDistance>, EdgeDistance> generator = new GridGraphGenerator<Node<EdgeDistance>, Link<EdgeDistance>, EdgeDistance>(
		manet);
	generator.generate(new GridGraphProperties(400, 400, 100, 100));

	this.manet = manet;

    }

    void toVisual() {
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		VisualGraph<Node<EdgeDistance>, Link<EdgeDistance>> visualGraph = new VisualGraph<Node<EdgeDistance>, Link<EdgeDistance>>(
			manet, new VisualGraphMarkUp());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int windowWidth = (int) screenSize.getWidth() * 3 / 4;
		int windowHeight = (int) screenSize.getHeight() * 3 / 4;
		VisualGraphPanel<Node<EdgeDistance>, Link<EdgeDistance>> panel = new VisualGraphPanel<Node<EdgeDistance>, Link<EdgeDistance>>(
			visualGraph);

		panel.setPreferredSize(new Dimension(windowWidth, windowHeight));
		panel.setFont(new Font("Consolas", Font.PLAIN, 16));
		panel.setLayout(null);

		JFrame frame = new JFrame("VisualGraphPanel");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	    }
	});
    }
}
