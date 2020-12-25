package de.manetmodel.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import de.manetmodel.util.Tuple;

public abstract class WeightedGraph<P, W> {

    protected int vertexCount;
    protected int edgeCount;
    protected ArrayList<Vertex<P>> vertices;    
    protected ArrayList<WeightedEdge<W>> edges;
    protected ArrayList<ArrayList<Tuple<Integer, Integer>>> vertexAdjacencies;
    protected ArrayList<Tuple<Integer, Integer>> edgeAdjacencies;

    public WeightedGraph() {
	this.vertices = new ArrayList<Vertex<P>>();
	this.edges = new ArrayList<WeightedEdge<W>>();
	this.vertexAdjacencies = new ArrayList<ArrayList<Tuple<Integer, Integer>>>();
	this.edgeAdjacencies = new ArrayList<Tuple<Integer, Integer>>();
    }

    public Vertex<P> addVertex(P position) {
	Vertex<P> v = new Vertex<P>();
	v.setID(vertexCount++);
	v.setPosition(position);
	vertices.add(v);
	vertexAdjacencies.add(new ArrayList<Tuple<Integer, Integer>>());	
	return v;
    }

    public boolean addVertex(Vertex<P> vertex) {
	if (vertex.getPosition() != null) {
	    vertex.setID(vertexCount++);
	    vertices.add(vertex);
	    vertexAdjacencies.add(new ArrayList<Tuple<Integer, Integer>>());
	    return true;	    
	}
	return false;
    }

    public List<WeightedEdge<W>> getEdges() {
	return edges;
    }

    public List<Vertex<P>> getVertices() {
	return vertices;
    }

    public Vertex<P> getFirstVertex() {
	return this.vertices.get(0);
    }

    public Vertex<P> getLastVertex() {
	return this.vertices.get(vertexCount - 1);
    }

    public Vertex<P> getVertex(Vertex<P> v) {
	return this.vertices.get(v.getID());
    }

    public Vertex<P> getVertex(int ID) {
	return this.vertices.get(ID);
    }
    
    public WeightedEdge<W> getEdge(WeightedEdge<W> e) {
	return edges.get(e.getID());
    }

    public WeightedEdge<W> getEdge(int ID) {
	return edges.get(ID);
    }

    public Tuple<Vertex<P>, Vertex<P>> getVerticesOf(WeightedEdge<W> edge) {
	Tuple<Integer, Integer> vertexIDs = this.edgeAdjacencies.get(edge.getID());
	return new Tuple<Vertex<P>, Vertex<P>>(this.vertices.get(vertexIDs.getFirst()), this.vertices.get(vertexIDs.getSecond()));
    }

    public WeightedEdge<W> getEdge(Vertex<P> source, Vertex<P> target) {
	for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(source.getID()))
	    if (adjacency.getSecond() == target.getID())
		return this.edges.get(adjacency.getFirst());
	return null;
    }

    public boolean containsEdge(Vertex<P> source, Vertex<P> target) {
	if (source.getID() < vertexAdjacencies.size())
	    for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(source.getID()))
		if (adjacency.getSecond() == target.getID())
		    return true;
	return false;
    }

    public WeightedEdge<W> addEdge(Vertex<P> source, Vertex<P> target) {
	return this.addEdge(source, target, null);
    }

    public abstract WeightedEdge<W> addEdge(Vertex<P> source, Vertex<P> target, W weight);

    public abstract List<WeightedEdge<W>> getEdgesOf(Vertex<P> vertex);
 
    public Vertex<P> getTargetOf(Vertex<P> vertex, WeightedEdge<W> edge) {
	Tuple<Integer, Integer> vertexIDs = this.edgeAdjacencies.get(edge.getID());
	if (vertex.getID() == vertexIDs.getFirst())
	    return this.vertices.get(vertexIDs.getSecond());
	else if (vertex.getID() == vertexIDs.getSecond())
	    return this.vertices.get(vertexIDs.getFirst());
	return null;
    }

    public List<Vertex<P>> getNextHopsOf(Vertex<P> vertex) {
	List<Vertex<P>> nextHops = new ArrayList<Vertex<P>>();
	for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(vertex.getID()))
	    nextHops.add(vertices.get(adjacency.getSecond()));
	return nextHops;
    }

    public List<Tuple<WeightedEdge<W>, Vertex<P>>> getNextPathsOf(Vertex<P> vertex) {
	List<Tuple<WeightedEdge<W>, Vertex<P>>> nextPaths = new ArrayList<Tuple<WeightedEdge<W>, Vertex<P>>>();
	for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(vertex.getID()))
	    nextPaths.add(new Tuple<WeightedEdge<W>, Vertex<P>>(edges.get(adjacency.getSecond()), vertices.get(adjacency.getFirst())));
	return nextPaths;
    }

    public Iterator<Vertex<P>> vertexIterator() {
	Iterator<Vertex<P>> iterator = new Iterator<Vertex<P>>() {
	    private int i = 0;
	    @Override
	    public boolean hasNext() {
		return i < vertices.size() && vertices.get(i) != null;
	    }
	    @Override
	    public Vertex<P> next() {
		return vertices.get(i++);
	    }
	};
	return iterator;
    }

    public Iterator<WeightedEdge<W>> edgeIterator() {
	Iterator<WeightedEdge<W>> iterator = new Iterator<WeightedEdge<W>>() {
	    private int i = 0;
	    @Override
	    public boolean hasNext() {
		return i < edges.size() && edges.get(i) != null;
	    }
	    @Override
	    public WeightedEdge<W> next() {
		return edges.get(i++);
	    }
	};
	return iterator;
    }

    public void clear() {
	this.vertices.clear();
	this.vertexCount = 0;
	this.edges.clear();
	this.edgeCount = 0;
	this.vertexAdjacencies.clear();
	this.edgeAdjacencies.clear();
    }
}