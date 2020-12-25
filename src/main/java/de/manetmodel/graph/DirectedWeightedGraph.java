package de.manetmodel.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import de.manetmodel.util.Tuple;

public class DirectedWeightedGraph<P, W>
	extends WeightedGraph<P, W> {

    public DirectedWeightedGraph() { }

    public WeightedEdge<W> addEdge(Vertex<P> source, Vertex<P> target, W weight) {
	if (containsEdge(source, target))
	    return null;
	WeightedEdge<W> edge = new WeightedEdge<W>();
	edge.setID(edgeCount++);
	edge.setWeight(weight);
	edges.add(edge);
	super.vertexAdjacencies.get(source.getID()).add(new Tuple<Integer, Integer>(edge.getID(), target.getID()));
	super.edgeAdjacencies.add(new Tuple<Integer, Integer>(source.getID(), target.getID()));
	return edge;
    }

    public List<WeightedEdge<W>> getEdgesOf(Vertex<P> vertex) {
	List<WeightedEdge<W>> edges = new ArrayList<WeightedEdge<W>>();
	for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(vertex.getID())) {
	    edges.add(super.edges.get(adjacency.getFirst()));
	    for (WeightedEdge<W> edge : getOutgoingEdgesOf(vertices.get(adjacency.getSecond())))
		if (getTargetOf(vertices.get(adjacency.getSecond()), edge).equals(vertex))
		    edges.add(edge);
	}
	return edges;
    }

    public List<WeightedEdge<W>> getOutgoingEdgesOf(Vertex<P> vertex) {
	List<WeightedEdge<W>> edges = new ArrayList<WeightedEdge<W>>();
	for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(vertex.getID()))
	    edges.add(super.edges.get(adjacency.getFirst()));
	return edges;
    }

    public List<WeightedEdge<W>> getIncomingEdgesOf(Vertex<P> vertex) {
	List<WeightedEdge<W>> edges = new ArrayList<WeightedEdge<W>>();
	for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(vertex.getID()))
	    for (WeightedEdge<W> edge : getOutgoingEdgesOf(vertices.get(adjacency.getSecond())))
		if (getTargetOf(vertices.get(adjacency.getSecond()), edge).equals(vertex))
		    edges.add(edge);
	return edges;
    }
}