package de.manetmodel.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class UndirectedWeighted2DGraph<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W>
	extends UndirectedWeightedGraph<V, Position2D, E, W> {

    public UndirectedWeighted2DGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier) {
	super(vertexSupplier, edgeSupplier);
    }

    public V addVertex(double x, double y) {
	return super.addVertex(new Position2D(x, y));
    }

    public double getDistance(Position2D p1, Position2D p2) {
	return Math.sqrt(Math.pow(p1.x() - p2.x(), 2) + Math.pow(p1.y() - p2.y(), 2));
    }

    public List<V> getVerticesInRadius(V source, double radius) {
	List<V> vertices = new ArrayList<V>();
	for (V vertex : this.vertices)
	    if (!vertex.equals(source) && this.getDistance(source.getPosition(), vertex.getPosition()) <= radius)
		vertices.add(vertex);
	return vertices;
    }

    public Boolean vertexInRadius(Position2D position, double radius) {
	for (V vertex : this.vertices)
	    if (this.getDistance(position, vertex.getPosition()) <= radius)
		return true;
	return false;
    }
}
