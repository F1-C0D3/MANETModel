package de.manetgraph.extensiontest;

import org.junit.Test;

public class MyGraphApp {
	
	@Test
	public void run() {
				
		MyGraph graph = new MyGraph();
					
		graph.generateRandomGraph();
		
		System.out.println(graph.vertexSet().size() + " " + graph.edgeSet().size());	
		
	}		
}
