package de.manetmodel.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class UndirectedWeighted3DGraph<V extends Vertex<Position3D>, E extends WeightedEdge<W>, W>
	extends UndirectedWeightedGraph<V, Position3D, E, W> {

    public UndirectedWeighted3DGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier) {
	super(vertexSupplier, edgeSupplier);
    }

    public V addVertex(double x, double y, double z) {
	return super.addVertex(new Position3D(x, y, z));
    }

    public V addVertex(double x, double y) {
	return super.addVertex(new Position3D(x, y, 0));
    }

    public double getDistance(Position3D p1, Position3D p2) {
	return Math.sqrt(Math.pow(p2.x() - p1.x(), 2) + Math.pow(p2.y() - p1.y(), 2) + Math.pow(p2.z() - p1.z(), 2));
    }

    public List<Vertex<Position3D>> getVerticesInRadius(Vertex<Position3D> source, double radius) {
	List<Vertex<Position3D>> vertices = new ArrayList<Vertex<Position3D>>();
	for (Vertex<Position3D> vertex : this.vertices)
	    if (!vertex.equals(source) && getDistance(source.getPosition(), vertex.getPosition()) <= radius)
		vertices.add(vertex);
	return vertices;
    }

    public Boolean vertexInRadius(Position3D position, double radius) {
	for (Vertex<Position3D> vertex : this.vertices)
	    if (getDistance(position, vertex.getPosition()) <= radius)
		return true;
	return false;
    }
}
