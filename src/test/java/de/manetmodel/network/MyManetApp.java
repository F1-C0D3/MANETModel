package de.manetmodel.network;

import org.junit.Test;

import de.manetmodel.graph.Playground;
import de.manetmodel.graph.Playground.DoubleRange;
import de.manetmodel.graph.Playground.IntRange;
import de.manetmodel.graph.generator.GraphGenerator;

public class MyManetApp {
    @Test
    public void run() {
	MyManet graph = new MyManet();
	GraphGenerator<MyNode, MyLink> generator = new GraphGenerator<MyNode, MyLink>(graph);
	generator.generateRandomGraph(new Playground(500, 500, new IntRange(3, 10), new DoubleRange(50d, 80d),
		new IntRange(60, 60), new DoubleRange(60d, 100d)));
	System.out.println(graph.getVertices().size() + " " + graph.getEdges().size());
    }
}
