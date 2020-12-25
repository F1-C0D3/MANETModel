package de.manetmodel.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class UndirectedWeighted2DGraph<W> extends UndirectedWeightedGraph<Position2D, W> {

    public UndirectedWeighted2DGraph() {
    }

    public Vertex<Position2D> addVertex(double x, double y) {
	return super.addVertex(new Position2D(x, y));
    }

    public double getDistance(Position2D p1, Position2D p2) {
	double distance = Math.sqrt(Math.pow(p1.x() - p2.x(), 2) + Math.pow(p1.y() - p2.y(), 2));
	return distance;
    }

    public List<Vertex<Position2D>> getVerticesInRadius(Vertex<Position2D> source, double radius) {
	List<Vertex<Position2D>> vertices = new ArrayList<Vertex<Position2D>>();
	for (Vertex<Position2D> vertex : this.vertices)
	    if (!vertex.equals(source) && getDistance(source.getPosition(), vertex.getPosition()) <= radius)
		vertices.add(vertex);

	return vertices;
    }

    public Boolean vertexInRadius(Position2D position, double radius) {
	for (Vertex<Position2D> vertex : vertices)
	    if (getDistance(position, vertex.getPosition()) <= radius)
		return true;
	return false;
    }
}
