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
	
	ManetGraph<AcoEdgeWeight> manetGraph = new AcoGraph(null, null);
	
	AcoGraph acoGraph = new AcoGraph(null, null);
			
	// ? °_°
	
	GraphGenerator<AcoNode, AcoLink, AcoEdgeWeight> gen1 = new GraphGenerator<AcoNode, AcoLink, AcoEdgeWeight>(manetGraph);	
	
	GraphGenerator<AcoNode, AcoLink, AcoEdgeWeight> gen2 = new GraphGenerator<AcoNode, AcoLink, AcoEdgeWeight>(acoGraph);

    }
}
