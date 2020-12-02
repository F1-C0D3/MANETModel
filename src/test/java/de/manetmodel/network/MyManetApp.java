package de.manetmodel.network;

import org.junit.Test;

public class MyManetApp
{

	@Test
	public void run()
	{

		MyManet graph = new MyManet();

		graph.generateRandomGraph(100d);

		System.out.println(graph.getVertices().size() + " " + graph.getEdges().size());

	}
}
