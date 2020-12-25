package de.manetmodel.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import de.manetmodel.util.Tuple;

public class UndirectedWeightedGraph<P, W> extends WeightedGraph<P, W>{

    public UndirectedWeightedGraph() {}

    public WeightedEdge<W> addEdge(Vertex<P> source, Vertex<P> target, W weight) {
	if (containsEdge(source, target)) return null;
	WeightedEdge<W> edge = new WeightedEdge<W>();
	edge.setID(edgeCount++);
	edge.setWeight(weight);
	edges.add(edge);
	vertexAdjacencies.get(source.getID()).add(new Tuple<Integer, Integer>(edge.getID(), target.getID()));
	vertexAdjacencies.get(target.getID()).add(new Tuple<Integer, Integer>(edge.getID(), source.getID()));
	edgeAdjacencies.add(new Tuple<Integer, Integer>(source.getID(), target.getID()));
	return edge;
    }

    public List<WeightedEdge<W>> getEdgesOf(Vertex<P> vertex) {
	List<WeightedEdge<W>> edges = new ArrayList<WeightedEdge<W>>();
	for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(vertex.getID()))
	    edges.add(this.edges.get(adjacency.getFirst()));
	return edges;
    }   
}