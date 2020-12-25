package de.manetmodel.manetgraph;

import java.util.function.Supplier;

import de.manetmodel.graph.EdgeDistance;
import de.manetmodel.graph.UndirectedWeighted2DGraph;
import de.manetmodel.graph.generator.NetworkGraphGenerator;

public class ManetGraph<W> extends UndirectedWeighted2DGraph<W>{

    public ManetGraph() {}
    
    public static class ManetEdgeWeight extends EdgeDistance {

	public double Datarate;
	
	public ManetEdgeWeight(double distance) {
	    super(distance);
	}
	
    }
    
    public static void main() {
	
	ManetGraph<ManetEdgeWeight> graph = new ManetGraph<ManetEdgeWeight>();
		
	NetworkGraphGenerator<ManetEdgeWeight> generator = new NetworkGraphGenerator<ManetEdgeWeight>(graph);
	
    }
}
