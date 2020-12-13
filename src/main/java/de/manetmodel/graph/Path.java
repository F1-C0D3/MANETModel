package de.manetmodel.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.manetmodel.util.Tuple;

public class Path<V extends Vertex, E extends Edge> extends LinkedList<Tuple<E, V>> implements Comparable<Path<V, E>> {

    Double distance;
    private V source;
    private V target;

    public Path() {
	this.source = null;
	this.target = null;
	this.distance = 0d;
    }

    public Path(V source) {
	this.source = source;
	super.add(new Tuple<E, V>(null, source));
	this.distance = 0d;
    }

    public Path(V source, V target) {
	this.source = source;
	this.target = target;
	super.add(new Tuple<E, V>(null, source));
	this.distance = 0d;
    }

    public V getSource() {
	return this.source;
    }

    public V getTarget() {
	return this.target;
    }

    public Tuple<E, V> getFirst() {
	if (this.get(0) != null)
	    return this.get(0);
	return null;
    }

    public Tuple<E, V> getLast() {
	if (this.size() > 0)
	    return this.get(this.size() - 1);
	return null;
    }

    public V getCurrentVertex() {
	if (this.size() > 0)
	    return this.get(this.size() - 1).getSecond();
	return null;
    }

    public E getCurrentEdge() {
	return this.get(this.size() - 1).getFirst();
    }

    public boolean visited(V vertex) {
	for (Tuple<E, V> edgeAndVertex : this)
	    if (vertex.equals(edgeAndVertex.getSecond()))
		return true;
	return false;
    }

    public Boolean isComplete() {
	return this.getLast().getSecond().equals(target);
    }

    public List<V> getUnvisitedVertices(List<V> vertices) {

	List<V> unvisitedVertices = new ArrayList<V>();

	for (V vertex : vertices)
	    if (!this.visited(vertex))
		unvisitedVertices.add(vertex);

	return unvisitedVertices;
    }

    public List<V> getVertices() {
	List<V> vertices = new ArrayList<V>();
	for (Tuple<E, V> edgeAndVertex : this)
	    vertices.add(edgeAndVertex.getSecond());
	return vertices;
    }

    public boolean clearPath() {
	if (this.size() > 1) {
	    Path<V, E> tmp = new Path<V, E>();
	    tmp.addAll(this.subList(1, this.size()));
	    this.removeAll(tmp);
	    return true;
	}
	return false;
    }

    public double getDistance() {
	return this.distance;
    }

    @Override
    public boolean add(Tuple<E, V> linkAndNode) {
	super.add(linkAndNode);
	if (linkAndNode.getFirst() != null)
	    this.distance += linkAndNode.getFirst().getDistance();
	return true;
    }

    public boolean isShorter(Path<V, E> path) {
	if (path != null)
	    return this.distance < path.getDistance();
	return false;
    }

    public int compareDistanceTo(Path<V, E> path) {
	return Double.compare(this.distance, path.getDistance());
    }

    @Override
    public int compareTo(Path<V, E> path) {
	return Double.compare(this.distance, path.getDistance());
    }

    public boolean equals(Path<V, E> path) {

	for (int i = 0; i < this.size(); i++) {
	    if (path.get(i) != null)
		if (!path.get(i).getSecond().equals(this.get(i).getSecond()))
		    return false;
		else
		    return false;
	}

	return true;
    }

    @Override
    public String toString() {
	String str = "";
	for (Tuple<E, V> linkAndNode : this) {
	    if (linkAndNode.getFirst() != null)
		str += String.format("- %.2f -", linkAndNode.getFirst().getDistance());
	    str += String.format("[%s]", linkAndNode.getSecond().toString());
	}
	return str;
    }
}
