package de.manetmodel.manetgraph;

import java.util.function.Supplier;

import de.manetmodel.graph.UndirectedWeighted2DGraph;
import de.manetmodel.graph.generator.GraphGenerator;

public class ManetGraph<W> extends UndirectedWeighted2DGraph<Node, Link<W>, W>{

    public ManetGraph(Supplier<Node> vertexSupplier, Supplier<Link<W>> edgeSupplier) {
	super(vertexSupplier, edgeSupplier);
    }
    
    public static void main() {
	
	ManetGraph<Double> graph = new ManetGraph<Double>(null, null);
		
	GraphGenerator<Node, Link<Double>, Double> generator = new GraphGenerator<Node, Link<Double>, Double>(graph);
	
    }
}
