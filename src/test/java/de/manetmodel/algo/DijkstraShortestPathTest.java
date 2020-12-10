package de.manetmodel.algo;

import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.junit.Test;

import de.manetmodel.graph.ManetPath;
import de.manetmodel.network.Link;
import de.manetmodel.network.Manet;
import de.manetmodel.network.ManetSupplier;
import de.manetmodel.network.Node;
import de.manetmodel.network.radio.IdealRadioOccupation;
import de.manetmodel.util.Topology;
import de.manetmodel.util.Tuple;
import de.manetmodel.visualization.VisualGraphPanel;

public class DijkstraShortestPathTest {
    private Manet<Node, Link> manet;

    public DijkstraShortestPathTest() {

	manet = new Manet<Node, Link>(new ManetSupplier.ManetNodeSupplier(), new ManetSupplier.ManetLinkSupplier());
    }

    @Test
    public void TrapeziumTest() {

	manet.createManet(Topology.TRAPEZIUM, new IdealRadioOccupation(100d, 125d, 2d));

	Function<Tuple<Link, Node>, Double> metric = (Tuple<Link, Node> p) -> {
	    return p.getFirst().getDistance();
	};

	DijkstraShortestPath<Node, Link> dijkstra = new DijkstraShortestPath<Node, Link>(manet.getGraph());
	ManetPath<Node, Link> sp = dijkstra.compute(manet.getGraph().getFirstVertex(), manet.getGraph().getLastVertex(),
		metric);
	List<Integer> spCompare = new ArrayList<Integer>();
	spCompare.add(0);
	spCompare.add(1);
	spCompare.add(3);

	Iterator<Tuple<Link, Node>> it = sp.iterator();
	List<Integer> spComputed = new ArrayList<Integer>();

	while (it.hasNext()) {
	    spComputed.add(it.next().getSecond().getID());
	}

	assertTrue(spCompare.equals(spComputed));

    }

    @Test
    public void GridTest() {

	manet.createManet(Topology.GRID, new IdealRadioOccupation(100d, 125d, 2d));
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {

		VisualGraphPanel panel = new VisualGraphPanel(manet.getGraph().toVisualGraph());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//			int width = (int) screenSize.getWidth() * 2;
//			int height = (int) screenSize.getHeight() * 2;

		int width = 1000;
		int height = 1000;
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

	Function<Tuple<Link, Node>, Double> metric = (Tuple<Link, Node> p) -> {
	    return p.getFirst().getDistance();
	};

	DijkstraShortestPath<Node, Link> dijkstra = new DijkstraShortestPath<Node, Link>(manet.getGraph());
	ManetPath<Node, Link> sp = dijkstra.compute(manet.getGraph().getVertex(0), manet.getGraph().getVertex(10),
		metric);
	List<Integer> spCompare = new ArrayList<Integer>();
	spCompare.add(0);
	spCompare.add(1);
	spCompare.add(2);
	spCompare.add(3);
	spCompare.add(4);
	spCompare.add(5);
	spCompare.add(6);
	spCompare.add(7);
	spCompare.add(8);
	spCompare.add(9);
	spCompare.add(10);

	Iterator<Tuple<Link, Node>> it = sp.iterator();
	List<Integer> spComputed = new ArrayList<Integer>();

	while (it.hasNext()) {
	    spComputed.add(it.next().getSecond().getID());
	}

	assertTrue(spCompare.equals(spComputed));

    }
}
