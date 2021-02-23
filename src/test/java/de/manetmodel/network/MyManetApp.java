package de.manetmodel.network;

import org.junit.Test;

import de.jgraphlib.graph.EdgeDistance;
import de.jgraphlib.graph.EdgeDistanceSupplier;
import de.jgraphlib.graph.Position2D;
import de.jgraphlib.graph.Vertex;
import de.jgraphlib.graph.WeightedEdge;
import de.jgraphlib.graph.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;

public class MyManetApp {
    @Test
    public void run() {
	
	MyManet graph = new MyManet();
	
	NetworkGraphGenerator<MyNode, MyLink, MyLinkProperties> generator = new NetworkGraphGenerator<MyNode, MyLink, MyLinkProperties>(graph);
	
	NetworkGraphProperties properties = 
		new NetworkGraphProperties(
				/*width*/				1000, 
				/*height*/				1000, 
				/*vertices*/ 			new IntRange(100, 100),
				/*vertex distance*/		new DoubleRange(75d, 100d), 
				/*edge distance*/		100);

	generator.generate(properties);		
    }
}
