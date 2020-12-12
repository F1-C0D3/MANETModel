package de.manetmodel.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import de.manetmodel.util.Tuple;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "WeightedUndirectedGraph")
@XmlAccessorType(XmlAccessType.FIELD)
public class WeightedUndirectedGraph<V extends Vertex, E extends Edge> {
    @XmlTransient
    private final Supplier<V> vertexSupplier;
    @XmlTransient
    private final Supplier<E> edgeSupplier;

    @XmlElement(name = "VertexCount")
    private int vertexCount;
    @XmlElement(name = "EdgeCount")
    private int edgeCount;

    @XmlElementWrapper(name = "Vertices")
    @XmlElement(name = "Vertex")
    private ArrayList<V> vertices;
    @XmlElementWrapper(name = "Edges")
    @XmlElement(name = "Edge")
    private ArrayList<E> edges;

    @XmlElementWrapper(name = "VertexAdjacencies")
    @XmlElement(name = "VertexAdjacency")
    private ArrayList<VertexAdjacency> vertexAdjacencies;
    @XmlElementWrapper(name = "EdgeAdjacencies")
    @XmlElement(name = "EdgeAdjacency")
    private ArrayList<EdgeAdjacency> edgeAdjacencies;

    public WeightedUndirectedGraph() {
	this.vertexSupplier = null;
	this.edgeSupplier = null;
	this.vertexCount = 0;
	this.edgeCount = 0;
	this.vertices = new ArrayList<V>();
	this.edges = new ArrayList<E>();
	this.vertexAdjacencies = new ArrayList<VertexAdjacency>();
	this.edgeAdjacencies = new ArrayList<EdgeAdjacency>();
    }

    public WeightedUndirectedGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier) {
	this.vertexSupplier = vertexSupplier;
	this.edgeSupplier = edgeSupplier;
	this.vertexCount = 0;
	this.edgeCount = 0;
	this.vertices = new ArrayList<V>();
	this.edges = new ArrayList<E>();
	this.vertexAdjacencies = new ArrayList<VertexAdjacency>();
	this.edgeAdjacencies = new ArrayList<EdgeAdjacency>();
    }

    public V addVertex(double x, double y) {
	if (this.vertexSupplier == null)
	    return null;
	V vertex = this.vertexSupplier.get();
	vertex.setID(vertexCount++);
	vertex.setPosition(x, y);
	this.vertices.add(vertex);
	this.vertexAdjacencies.add(new VertexAdjacency());
	return vertex;
    }

    public boolean addVertex(V vertex) {
	if (vertex.getPostion() == null)
	    return false;
	vertex.setID(vertexCount++);
	this.vertices.add(vertex);
	this.vertexAdjacencies.add(new VertexAdjacency());
	return true;
    }

    public V getVertex(int ID) {
	return this.vertices.get(ID);
    }

    public List<VertexAdjacency> getVertexAdjacencies() {
	return this.vertexAdjacencies;
    }

    public Tuple<V, V> getVerticesOf(E edge) {
	Tuple<Integer, Integer> vertexIDs = this.edgeAdjacencies.get(edge.getID()).getVertexIDs();
	return new Tuple<V, V>(this.vertices.get(vertexIDs.getFirst()), this.vertices.get(vertexIDs.getSecond()));
    }

    public E getEdge(V source, V target) {
	for (EdgeVertexMapping edgeVertexMapping : vertexAdjacencies.get(source.getID()).getEdgeVertexMappings())
	    if (edgeVertexMapping.getVertexID().equals(target.getID()))
		return this.edges.get(edgeVertexMapping.getEdgeID());

	return null;
    }

    public boolean containsEdge(V source, V target) {
	for (EdgeVertexMapping edgeVertexMapping : vertexAdjacencies.get(source.getID()).getEdgeVertexMappings())
	    if (edgeVertexMapping.getVertexID().equals(target.getID()))
		return true;

	return false;
    }

    public E addEdge(V source, V target) {
	if (this.edgeSupplier == null)
	    return null;
	if (containsEdge(source, target))
	    return null;

	E edge = this.edgeSupplier.get();
	edge.setID(edgeCount++);
	edge.setDistance(getDistance(source, target));
	edge.setVertexIDs(source.getID(), target.getID());
	this.edges.add(edge);
	this.vertexAdjacencies.get(source.getID()).add(new EdgeVertexMapping(edge.getID(), target.getID()));
	this.vertexAdjacencies.get(target.getID()).add(new EdgeVertexMapping(edge.getID(), source.getID()));
	this.edgeAdjacencies.add(new EdgeAdjacency(source.getID(), target.getID()));
	return edge;
    }

    public List<E> getEdgesOf(V vertex) {
	List<E> edges = new ArrayList<E>();
	for (EdgeVertexMapping edgeVertexMapping : vertexAdjacencies.get(vertex.getID()).getEdgeVertexMappings())
	    edges.add(this.edges.get(edgeVertexMapping.getEdgeID()));

	return edges;
    }

    public V getTargetOf(V vertex, E edge) {
	Tuple<Integer, Integer> vertexIDs = this.edgeAdjacencies.get(edge.getID()).getVertexIDs();
	if (vertex.getID() == vertexIDs.getFirst())
	    return this.vertices.get(vertexIDs.getSecond());
	else if (vertex.getID() == vertexIDs.getSecond())
	    return this.vertices.get(vertexIDs.getFirst());
	return null;
    }

    public List<V> getVertices() {
	return this.vertices;
    }

    public List<E> getEdges() {
	return this.edges;
    }

    public double getDistance(V source, V target) {
	return Math.sqrt(Math.pow(source.x() - target.x(), 2) + Math.pow(source.y() - target.y(), 2));
    }

    public double getDistance(Coordinate source, Coordinate target) {
	return Math.sqrt(Math.pow(source.x() - target.x(), 2) + Math.pow(source.y() - target.y(), 2));
    }

    public List<V> getVerticesInRadius(Coordinate coordinate, double radius) {
	List<V> vertices = new ArrayList<>();
	for (V vertex : this.vertices)
	    if (this.getDistance(coordinate, vertex.getPostion()) <= radius)
		vertices.add(vertex);
	return vertices;
    }

    public List<V> getVerticesInRadius(V source, double radius) {
	List<V> vertices = new ArrayList<V>();
	for (V vertex : this.vertices)
	    if (!vertex.equals(source) && this.getDistance(source.getPostion(), vertex.getPostion()) <= radius)
		vertices.add(vertex);
	return vertices;
    }
    
    public Boolean hasVertexInRadius(Coordinate coordinate, double radius) {
	for (V vertex : this.vertices)
	    if (this.getDistance(coordinate, vertex.getPostion()) <= radius)
		return true;
	return false;
    }

    public V getFirstVertex() {
	return this.vertices.get(0);
    }

    public V getLastVertex() {
	return this.vertices.get(vertexCount - 1);
    }

    public List<V> getNextHopsOf(V vertex) {
	List<V> nextHops = new ArrayList<V>();
	for (EdgeVertexMapping edgeVertexMapping : vertexAdjacencies.get(vertex.getID()).getEdgeVertexMappings())
	    nextHops.add(this.vertices.get(edgeVertexMapping.getVertexID()));
	return nextHops;
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

    @Override
    public String toString() {
	StringBuffer stringBuffer = new StringBuffer();

	for (V vertex : this.vertices)
	    stringBuffer.append(vertex);

	for (E edge : this.edges)
	    stringBuffer.append(edge);

	return stringBuffer.toString();
    }
}
