package de.manetmodel.graph.experimental;

import java.util.function.Supplier;

public class UndirectedWeighted2DGraph<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W> extends UndirectedWeightedGraph<V, E, Position2D, W> {
	
	public UndirectedWeighted2DGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier) {
		super(vertexSupplier, edgeSupplier);
	}
	
	public V addVertex(double x, double y){
		return super.addVertex(new Position2D(x,y));
	}
	
	public double getDistance(V v1, V v2) {
		return Math.sqrt(Math.pow(v1.getPosition().x() - v2.getPosition().x(), 2) + Math.pow(v1.getPosition().y() - v2.getPosition().y(), 2));
	}	
}
