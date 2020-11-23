package de.manetmodel.algo;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import de.manetmodel.graph.ManetEdge;
import de.manetmodel.graph.ManetGraph;
import de.manetmodel.graph.ManetGraphSupplier;
import de.manetmodel.graph.ManetVertex;

public class RandomPathTest
{
	private ManetGraph<ManetVertex, ManetEdge> graph;

	@Test
	public void RandomPathTest()
	{
		ManetGraph<ManetVertex, ManetEdge> graph = new ManetGraph<ManetVertex, ManetEdge>(
				new ManetGraphSupplier.ManetVertexSupplier(), new ManetGraphSupplier.ManetEdgeSupplier());
		graph.generateTrapeziumGraph();

		RandomPath randomPath = new RandomPath(graph);
		assertNotNull(randomPath.compute(graph.getFirstVertex(), graph.getLastVertex()));
	}

}
