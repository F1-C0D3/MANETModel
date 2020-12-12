package de.manetmodel.graph.experimental;

import java.util.function.Supplier;

public class UndirectedWeighted3DGraph<V extends Vertex<Position3D>, E extends WeightedEdge<W>, W> extends UndirectedWeightedGraph<V, E, Position3D, W> {
	
	public UndirectedWeighted3DGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier) {
		super(vertexSupplier, edgeSupplier);
	}
	
	public V addVertex(double x, double y, double z){
		return super.addVertex(new Position3D(x,y,z));
	}
	
	public double getDistance(V v1, V v2) {
		return Math.sqrt(
			Math.pow(v2.getPosition().x() - v1.getPosition().x(), 2) +
			Math.pow(v2.getPosition().y() - v1.getPosition().y(), 2) + 
			Math.pow(v2.getPosition().z() - v1.getPosition().z(), 2) );
	}	
}
	