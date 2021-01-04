package de.manetmodel.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.manetmodel.util.Tuple;

public class Path<V extends Vertex<?>, E extends WeightedEdge<?>> extends LinkedList<Tuple<E, V>> {

    protected V source;
    protected V target;

    public Path() {
	this.source = null;
	this.target = null;
    }

    public Path(V source) {
	this.source = source;
	super.add(new Tuple<E, V>(null, source));
    }

    public Path(V source, V target) {
	this.source = source;
	this.target = target;
	super.add(new Tuple<E, V>(null, source));
    }

    @Override
    public boolean add(Tuple<E, V> linkAndNode) {
	return super.add(linkAndNode);
    }

    @Override
    public void clear() {
	super.clear();
	super.add(new Tuple<E, V>(null, source));
    }

    public V getSource() {
	return this.source;
    }

    public V getTarget() {
	return this.target;
    }

    @Override
    public Tuple<E, V> getFirst() {
	if (this.get(0) != null)
	    return this.get(0);
	return null;
    }

    @Override
    public Tuple<E, V> getLast() {
	if (this.size() > 0)
	    return this.get(this.size() - 1);
	return null;
    }

    public V getLastVertex() {
	if (this.size() > 0)
	    return this.get(this.size() - 1).getSecond();
	return null;
    }

    public E getLastEdge() {
	return this.get(this.size() - 1).getFirst();
    }

    public boolean contains(V vertex) {
	for (Tuple<E, V> vertexEdgeTuple : this)
	    if (vertex.equals(vertexEdgeTuple.getSecond()))
		return true;
	return false;
    }

    public boolean contains(E edge) {
	for (Tuple<E, V> vertexEdgeTuple : this)
	    if (edge.equals(vertexEdgeTuple.getFirst()))
		return true;
	return false;
    }

    public Boolean isComplete() {
	return this.getLast().getSecond().equals(target);
    }

    public List<V> getUnvisitedVertices(List<V> vertices) {
	List<V> unvisitedVertices = new ArrayList<V>();
	for (V vertex : vertices)
	    if (!this.contains(vertex))
		unvisitedVertices.add(vertex);
	return unvisitedVertices;
    }

    public List<V> getVisitedVertices() {
	List<V> vertices = new ArrayList<V>();
	for (Tuple<E, V> vertexEdgeTuple : this)
	    vertices.add(vertexEdgeTuple.getSecond());
	return vertices;
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
	StringBuilder stringBuilder = new StringBuilder();
	Iterator<Tuple<E, V>> iterator = this.iterator();
	while (iterator.hasNext()) {
	    Tuple<E, V> next = iterator.next();
	    if (iterator.hasNext())
		stringBuilder.append(next.getSecond().getID()).append(", ");
	    else
		stringBuilder.append(next.getSecond().getID());
	}
	return stringBuilder.toString();
    }
}
