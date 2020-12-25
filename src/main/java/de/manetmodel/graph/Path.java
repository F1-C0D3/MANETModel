package de.manetmodel.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.manetmodel.util.Tuple;

public class Path<P,W> extends LinkedList<Tuple<WeightedEdge<W>, Vertex<P>>> {

    private Vertex<P> source;
    private Vertex<P> target;

    public Path() {
	this.source = null;
	this.target = null;
    }

    public Path(Vertex<P> source) {
	this.source = source;
	super.add(new Tuple<WeightedEdge<W>, Vertex<P>>(null, source));
    }

    public Path(Vertex<P> source, Vertex<P> target) {
	this.source = source;
	this.target = target;
	super.add(new Tuple<WeightedEdge<W>, Vertex<P>>(null, source));
    }
    
    @Override
    public boolean add(Tuple<WeightedEdge<W>, Vertex<P>> linkAndNode) {
	return super.add(linkAndNode);
    }

    @Override
    public void clear() {
	super.clear();
	super.add(new Tuple<WeightedEdge<W>, Vertex<P>>(null, source));
    }

    public Vertex<P> getSource() {
	return this.source;
    }

    public Vertex<P> getTarget() {
	return this.target;
    }

    @Override
    public Tuple<WeightedEdge<W>, Vertex<P>> getFirst() {
	if (this.get(0) != null)
	    return this.get(0);
	return null;
    }

    @Override
    public Tuple<WeightedEdge<W>, Vertex<P>> getLast() {
	if (this.size() > 0)
	    return this.get(this.size() - 1);
	return null;
    }

    public Vertex<P> getLastVertex() {
	if (this.size() > 0)
	    return this.get(this.size() - 1).getSecond();
	return null;
    }

    public WeightedEdge<W> getLastEdge() {
	return this.get(this.size() - 1).getFirst();
    }

    public boolean contains(Vertex<P> vertex) {
	for (Tuple<WeightedEdge<W>, Vertex<P>> vertexEdgeTuple : this)
	    if (vertex.equals(vertexEdgeTuple.getSecond()))
		return true;
	return false;
    }

    public boolean contains(WeightedEdge<W> edge) {
	for (Tuple<WeightedEdge<W>, Vertex<P>> vertexEdgeTuple : this)
	    if (edge.equals(vertexEdgeTuple.getFirst()))
		return true;
	return false;
    }

    public Boolean isComplete() {
	return this.getLast().getSecond().equals(target);
    }

    public List<Vertex<P>> getUnvisitedVertices(List<Vertex<P>> vertices) {
	List<Vertex<P>> unvisitedVertices = new ArrayList<Vertex<P>>();
	for (Vertex<P> vertex : vertices)
	    if (!this.contains(vertex))
		unvisitedVertices.add(vertex);
	return unvisitedVertices;
    }

    public List<Vertex<P>> getVisitedVertices() {
	List<Vertex<P>> vertices = new ArrayList<Vertex<P>>();
	for (Tuple<WeightedEdge<W>, Vertex<P>> vertexEdgeTuple : this)
	    vertices.add(vertexEdgeTuple.getSecond());
	return vertices;
    }

    public boolean equals(Path<P,W> path) {
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
	for (Tuple<WeightedEdge<W>, Vertex<P>> vertexEdgeTuple : this) {
	    if (vertexEdgeTuple.getFirst() != null)
		str += String.format("- %.2f -", vertexEdgeTuple.getFirst().toString());
	    str += String.format("[%s]", vertexEdgeTuple.getSecond().toString());
	}
	return str;
    }
}
