package de.manetgraph.test;

import org.junit.Test;

import de.manetgraph.ManetEdge;
import de.manetgraph.ManetGraph;
import de.manetgraph.ManetVertex;
import de.manetgraph.generator.ManetGraphGenerator;

public class ManetGraphTest {
	
	@Test
	public void ManetGraphGeneratorTest() {
		
		ManetGraph<ManetVertex, ManetEdge> graph = new ManetGraph<ManetVertex, ManetEdge>(ManetEdge.class);
		
		ManetGraphGenerator.getASimpleGraph(graph);
		
		System.out.println(graph.vertexSet().size());
		
		
	}

}
