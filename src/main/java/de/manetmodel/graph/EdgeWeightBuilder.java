package de.manetmodel.graph;

public abstract class EdgeWeightBuilder<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W> {

    protected UndirectedWeighted2DGraph<V, E, W> graph;

    public EdgeWeightBuilder(UndirectedWeighted2DGraph<V, E, W> graph) {
	this.graph = graph;
    }

    public abstract W get(V v1, V v2);
}
