package de.manetmodel.manetgraph;

import java.util.function.Supplier;

public class AcoGraph extends ManetGraph<AcoEdgeWeight>{

    public AcoGraph(Supplier<Node> vertexSupplier, Supplier<Link<AcoEdgeWeight>> edgeSupplier) {
	super(vertexSupplier, edgeSupplier);
    }

    public void add(AcoNode v){
	super.addVertex(v);
    }
 
}
