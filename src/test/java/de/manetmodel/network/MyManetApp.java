package de.manetmodel.network;

import org.junit.Test;

import de.manetmodel.graph.Playground;
import de.manetmodel.graph.generator.GraphGenerator;

public class MyManetApp
{
	@Test
	public void run()
	{
		MyManet graph = new MyManet();		
		GraphGenerator<MyNode, MyLink> generator = new GraphGenerator<MyNode,MyLink>(graph);
		generator.generateRandomGraph(new Playground());
		System.out.println(graph.getVertices().size() + " " + graph.getEdges().size());
	}
}
