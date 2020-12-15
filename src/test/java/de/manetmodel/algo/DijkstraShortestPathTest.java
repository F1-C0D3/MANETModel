package de.manetmodel.algo;

import java.awt.Color;
import java.util.function.Function;

import org.junit.Test;

import de.manetmodel.app.ManetModelApp;
import de.manetmodel.graph.Path;
import de.manetmodel.graph.Playground;
import de.manetmodel.graph.Playground.DoubleRange;
import de.manetmodel.graph.Playground.IntRange;
import de.manetmodel.graph.generator.GraphGenerator;
import de.manetmodel.network.Link;
import de.manetmodel.network.Manet;
import de.manetmodel.network.ManetSupplier;
import de.manetmodel.network.Node;
import de.manetmodel.network.radio.IdealRadioModel;
import de.manetmodel.util.Tuple;

public class DijkstraShortestPathTest {
    @Test
    public void DijkstraShortestPathTest() {
	Manet<Node, Link> manet = new Manet<Node, Link>(new ManetSupplier.ManetNodeSupplier(),
		new ManetSupplier.ManetLinkSupplier());

	/*
	 * Node<Link> source = manet.getGraph().addVertex(0, 0); Node<Link> a =
	 * manet.getGraph().addVertex(7, 4); Node<Link> b =
	 * manet.getGraph().addVertex(2, 7); Node<Link> target =
	 * manet.getGraph().addVertex(10, 10); manet.getGraph().addEdge(source, a);
	 * manet.getGraph().addEdge(source, b); manet.getGraph().addEdge(a, b);
	 * manet.getGraph().addEdge(a, target); manet.getGraph().addEdge(b, target);
	 */

	GraphGenerator<Node, Link> generator = new GraphGenerator<Node, Link>(manet.getGraph());
	Playground playground = new Playground(1024, 768, new IntRange(100, 200),
		new DoubleRange(50d, 100d), new IntRange(2, 4), new DoubleRange(50d, 100d));
	generator.generateRandomGraph(playground);

	manet.setRadioOccupationModel(new IdealRadioModel(100d, 125d, 2L));
	manet.initialize();

	Function<Tuple<Link, Node>, Double> metric = (Tuple<Link, Node> t) -> {
	    return 1d;
	};

	DijkstraShortestPath<Node, Link> dijkstra = new DijkstraShortestPath<Node, Link>(manet.getGraph());
	Path<Node, Link> shortestPath = dijkstra.compute(manet.getGraph().getFirstVertex(),
		manet.getGraph().getLastVertex(), metric);
	
	ManetModelApp<Node, Link> app = new ManetModelApp<Node, Link>(manet.getGraph());
	app.getPanel().getVisualGraph().addPath(shortestPath, Color.RED);
	app.run();

	/*
	 * List<Integer> spCompare = new ArrayList<Integer>(); spCompare.add(0);
	 * spCompare.add(1); spCompare.add(3); Iterator<Tuple<Link, Node<Link>>> it =
	 * sp.iterator(); List<Integer> spComputed = new ArrayList<Integer>(); while
	 * (it.hasNext()) { spComputed.add(it.next().getSecond().getID()); }
	 * assertTrue(spCompare.equals(spComputed));
	 */
    }
}