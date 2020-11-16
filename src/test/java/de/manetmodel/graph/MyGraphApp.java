package de.manetmodel.graph;

import org.junit.Test;

public class MyGraphApp {
	
	@Test
	public void run() {
				
		MyGraph graph = new MyGraph();
					
		graph.generateRandomGraph();
		
		System.out.println(graph.getVertices().size() + " " + graph.getEdges().size());	
		
	}		
}
