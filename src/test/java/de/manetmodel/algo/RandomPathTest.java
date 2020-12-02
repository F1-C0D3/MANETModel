package de.manetmodel.algo;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import de.manetmodel.graph.ManetGraph;
import de.manetmodel.network.Link;
import de.manetmodel.network.ManetSupplier;
import de.manetmodel.network.Node;
import de.manetmodel.visualization.VisualGraph;

public class RandomPathTest
{
	private ManetGraph<Node, Link> graph;

	@Test
	public void RandomPathTest()
	{

		ManetGraph<Node, Link> graph = new ManetGraph<Node, Link>(new ManetSupplier.ManetNodeSupplier(),
				new ManetSupplier.ManetLinkSupplier());
		graph.generateTrapeziumGraph();
		VisualGraph gV = graph.toVisualGraph();

		RandomPath randomPath = new RandomPath(graph);
		assertNotNull(randomPath.compute(graph.getFirstVertex(), graph.getLastVertex()));
	}

}
