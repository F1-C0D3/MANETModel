package de.manetmodel.network.generator;

import java.util.function.Supplier;

import de.jgraphlib.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.Node;

public class MANETGenerator<N extends Node, L extends Link<W>, W extends LinkQuality>
	extends NetworkGraphGenerator<N, L, W> {

    public MANETGenerator(Weighted2DGraph<N,L, W, ?> graph, Supplier<W> edgeWeightSupplier, RandomNumbers random) {
	super(graph, edgeWeightSupplier, random);
    }

    public void update(double maxEdgeDistance) {
	

	for (N node : this.graph.getVertices()) {

	    this.removeLinks(node, maxEdgeDistance);
	}
    }
    
    private void removeLinks(N node, double maxEdgeDistance) {
	
    }
}