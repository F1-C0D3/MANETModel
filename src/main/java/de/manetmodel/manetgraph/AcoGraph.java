package de.manetmodel.manetgraph;

import java.util.function.Supplier;

import de.manetmodel.graph.EdgeDistance;
import de.manetmodel.graph.Position2D;
import de.manetmodel.graph.UndirectedWeighted2DGraph;
import de.manetmodel.graph.Vertex;
import de.manetmodel.graph.WeightedEdge;
import de.manetmodel.graph.generator.GraphGenerator;

public class AcoGraph extends ManetGraph<AcoEdgeWeight>{

    public AcoGraph(Supplier<Node> vertexSupplier, Supplier<Link<AcoEdgeWeight>> edgeSupplier) {
	super(vertexSupplier, edgeSupplier);
    }

    public void add(AcoNode v){
	super.addVertex(v);
    }
    
    public static void main() {
	
	ManetGraph<Double> manetGraph1 = new ManetGraph<Double>(null, null);
	
	ManetGraph<AcoEdgeWeight> manetGraph2 = new AcoGraph(null, null);
	
	AcoGraph acoGraph = new AcoGraph(null, null);
		
	
	GraphGenerator<Node, Link<Double>, Double> gen1 = new GraphGenerator<Node, Link<Double>, Double>(manetGraph1);	
		
	
	GraphGenerator<Node, Link<AcoEdgeWeight>, AcoEdgeWeight> gen2 = new GraphGenerator<Node, Link<AcoEdgeWeight>, AcoEdgeWeight>(manetGraph2);	
	
	
	GraphGenerator<Node, Link<AcoEdgeWeight>, AcoEdgeWeight> gen3 = new GraphGenerator<Node, Link<AcoEdgeWeight>, AcoEdgeWeight>(acoGraph);	
	
	// ? °_° ok java
	GraphGenerator<AcoNode, AcoLink, AcoEdgeWeight> gen4 = new GraphGenerator<AcoNode, AcoLink, AcoEdgeWeight>(acoGraph);	
    }
}
