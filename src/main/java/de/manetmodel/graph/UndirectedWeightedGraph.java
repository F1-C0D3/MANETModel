package de.manetmodel.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import de.manetmodel.util.Tuple;

public class UndirectedWeightedGraph<V extends Vertex<P>, P, E extends WeightedEdge<W>, W> {

    int vertexCount;
    int edgeCount;
    ArrayList<V> vertices;
    ArrayList<E> edges;
    private final Supplier<V> vertexSupplier;
    private final Supplier<E> edgeSupplier;
    private ArrayList<ArrayList<Tuple<Integer, Integer>>> vertexAdjacencies;
    private ArrayList<Tuple<Integer, Integer>> edgeAdjacencies;

    public UndirectedWeightedGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier) {
	this.vertexSupplier = vertexSupplier;
	this.edgeSupplier = edgeSupplier;
	this.vertices = new ArrayList<V>();
	this.edges = new ArrayList<E>();
	this.vertexAdjacencies = new ArrayList<ArrayList<Tuple<Integer, Integer>>>();
	this.edgeAdjacencies = new ArrayList<Tuple<Integer, Integer>>();
    }

    public V addVertex(P position) {
	V v = vertexSupplier.get();
	v.setID(vertexCount++);
	v.setPosition(position);
	vertices.add(v);
	vertexAdjacencies.add(new ArrayList<Tuple<Integer, Integer>>());
	return v;
    }

    public boolean addVertex(V vertex) {
	if (vertex.getPosition() != null) {
	    vertex.setID(vertexCount++);
	    vertices.add(vertex);
	    vertexAdjacencies.add(new ArrayList<Tuple<Integer, Integer>>());
	    return true;
	}
	return false;
    }

    public List<E> getEdges() {
	return edges;
    }

    public List<V> getVertices() {
	return vertices;
    }

    public V getFirstVertex() {
	return this.vertices.get(0);
    }

    public V getLastVertex() {
	return this.vertices.get(vertexCount - 1);
    }

    public V getVertex(V v) {
	return this.vertices.get(v.getID());
    }

    public V getVertex(int ID) {
	return this.vertices.get(ID);
    }

    public Tuple<V, V> getVerticesOf(E edge) {
	Tuple<Integer, Integer> vertexIDs = this.edgeAdjacencies.get(edge.getID());
	return new Tuple<V, V>(this.vertices.get(vertexIDs.getFirst()), this.vertices.get(vertexIDs.getSecond()));
    }

    public E getEdge(V source, V target) {
	for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(source.getID()))
	    if (adjacency.getSecond() == target.getID())
		return this.edges.get(adjacency.getFirst());
	return null;
    }

    public boolean containsEdge(V source, V target) {
	if (source.getID() < vertexAdjacencies.size())
	    for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(source.getID()))
		if (adjacency.getSecond() == target.getID())
		    return true;
	return false;
    }

    public E addEdge(V source, V target) {
	return this.addEdge(source, target, null);
    }

    public E addEdge(V source, V target, W weight) {
	if (containsEdge(source, target))
	    return null;
	E edge = edgeSupplier.get();
	edge.setID(edgeCount++);
	edge.setWeight(weight);
	edges.add(edge);
	vertexAdjacencies.get(source.getID()).add(new Tuple<Integer, Integer>(edge.getID(), target.getID()));
	vertexAdjacencies.get(target.getID()).add(new Tuple<Integer, Integer>(edge.getID(), source.getID()));
	edgeAdjacencies.add(new Tuple<Integer, Integer>(source.getID(), target.getID()));
	return edge;
    }

    public List<E> getEdgesOf(V vertex) {
	List<E> edges = new ArrayList<E>();
	for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(vertex.getID()))
	    edges.add(this.edges.get(adjacency.getFirst()));
	return edges;
    }

    public V getTargetOf(V vertex, E edge) {
	Tuple<Integer, Integer> vertexIDs = this.edgeAdjacencies.get(edge.getID());
	if (vertex.getID() == vertexIDs.getFirst())
	    return this.vertices.get(vertexIDs.getSecond());
	else if (vertex.getID() == vertexIDs.getSecond())
	    return this.vertices.get(vertexIDs.getFirst());
	return null;
    }

    public List<V> getNextHopsOf(V vertex) {
	List<V> nextHops = new ArrayList<V>();
	for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(vertex.getID()))
	    nextHops.add(vertices.get(adjacency.getSecond()));
	return nextHops;
    }

    public List<Tuple<E, V>> getNextPathsOf(V vertex) {
	List<Tuple<E, V>> nextPaths = new ArrayList<Tuple<E, V>>();
	for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(vertex.getID()))
	    nextPaths.add(new Tuple<E, V>(edges.get(adjacency.getSecond()), vertices.get(adjacency.getFirst())));
	return nextPaths;
    }

    public Iterator<V> vertexIterator() {
	Iterator<V> iterator = new Iterator<V>() {
	    private int i = 0;

	    @Override
	    public boolean hasNext() {
		return i < vertices.size() && vertices.get(i) != null;
	    }

	    @Override
	    public V next() {
		return vertices.get(i++);
	    }
	};
	return iterator;
    }

    public Iterator<E> edgeIterator() {
	Iterator<E> iterator = new Iterator<E>() {
	    private int i = 0;

	    @Override
	    public boolean hasNext() {
		return i < edges.size() && edges.get(i) != null;
	    }

	    @Override
	    public E next() {
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