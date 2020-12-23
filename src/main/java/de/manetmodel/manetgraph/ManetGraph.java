package de.manetmodel.manetgraph;

import java.util.function.Supplier;

import de.manetmodel.graph.UndirectedWeighted2DGraph;

public class ManetGraph<W> extends UndirectedWeighted2DGraph<Node, Link<W>, W>{

    public ManetGraph(Supplier<Node> vertexSupplier, Supplier<Link<W>> edgeSupplier) {
	super(vertexSupplier, edgeSupplier);
    }
}
