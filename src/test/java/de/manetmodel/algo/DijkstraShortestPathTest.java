package de.manetmodel.algo;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.junit.Test;

import de.manetmodel.graph.ManetGraph;
import de.manetmodel.graph.ManetPath;
import de.manetmodel.network.Link;
import de.manetmodel.network.Manet;
import de.manetmodel.network.ManetSupplier;
import de.manetmodel.network.Node;
import de.manetmodel.network.radio.IdealRadioOccupation;
import de.manetmodel.util.Topology;
import de.manetmodel.util.Tuple;

public class DijkstraShortestPathTest
{
	private ManetGraph<Node, Link> graph;

	@Test
	public void RandomPathTest()
	{

		Manet<Node, Link> manet = new Manet<Node, Link>(new ManetSupplier.ManetNodeSupplier(),
				new ManetSupplier.ManetLinkSupplier());

		manet.createManet(Topology.TRAPEZIUM, new IdealRadioOccupation(100d, 125d, 2d));

		Function<Node, Double> metric = (Node n) -> {
			return (double) n.getInterferedLinks().size();
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

		while (it.hasNext())
		{
			spComputed.add(it.next().getSecond().getID());
		}

		assertTrue(spCompare.equals(spComputed));

	}
}
