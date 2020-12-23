package de.manetmodel.graph;

public class EdgeDistanceBuilder<V extends Vertex<Position2D>, E extends WeightedEdge<EdgeDistance>> extends EdgeWeightBuilder<V, WeightedEdge<EdgeDistance>, EdgeDistance> {

    public EdgeDistanceBuilder(UndirectedWeighted2DGraph<V, WeightedEdge<EdgeDistance>, EdgeDistance> graph) {
	super(graph);
    }

    @Override
    public EdgeDistance get(V v1, V v2) {			
	return new EdgeDistance(graph.getDistance(v1.getPosition(), v2.getPosition()));
    }
}
