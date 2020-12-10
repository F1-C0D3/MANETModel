package de.manetmodel.graph;

import org.junit.Test;

import de.manetmodel.graph.generator.GraphGenerator;

public class MyGraphApp {
	
	@Test
	public void run() {			
		MyGraph graph = new MyGraph();			
		GraphGenerator<MyVertex, MyEdge> generator = new GraphGenerator<MyVertex,MyEdge>(graph);
		generator.generateRandomGraph(new Playground());
		System.out.println(graph.getVertices().size() + " " + graph.getEdges().size());			
	}		
}
