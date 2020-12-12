package de.manetmodel.algo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.manetmodel.graph.Edge;
import de.manetmodel.graph.Path;
import de.manetmodel.graph.Vertex;
import de.manetmodel.graph.WeightedUndirectedGraph;
import de.manetmodel.util.RandomNumbers;
import de.manetmodel.util.Tuple;

public class RandomPath<V extends Vertex, E extends Edge> {
    
    private WeightedUndirectedGraph<V, E> graph;

    public RandomPath(WeightedUndirectedGraph<V, E> graph) {
	this.graph = graph;
    }

    public Path<V, E> compute(V source, V target) {
	Path<V, E> randomPath = new Path<V, E>();
	Set<E> visited = new HashSet<E>();

	while (source.getID() != target.getID()) {
	    List<E> edges = graph.getEdgesOf(source);
	    edges.removeIf(e -> visited.contains(e));

	    if (!edges.isEmpty()) {
		E edge = edges.get(RandomNumbers.getRandom(0, edges.size()));
		visited.add(edge);
		randomPath.add(new Tuple<E, V>(edge, source));
		source = graph.getTargetOf(source, edge);
	    } else {
		randomPath.clear();
		return randomPath;
	    }
	}
	return randomPath;
    }

    public Path<V, E> compute(V source, double distance) {

	Path<V, E> randomPath = new Path<V, E>();
	Set<E> visited = new HashSet<E>();

	while (randomPath.getDistance() < distance) {

	    List<E> edges = graph.getEdgesOf(source);

	    edges.removeIf(e -> visited.contains(e));

	    if (!edges.isEmpty()) {
		E edge = edges.get(RandomNumbers.getRandom(0, edges.size()));
		visited.add(edge);
		randomPath.add(new Tuple<E, V>(edge, source));
		source = graph.getTargetOf(source, edge);
	    } else {
		randomPath.clear();
		return randomPath;
	    }
	}
	return randomPath;
    }
    
    public Path<V, E> compute(V source, int hops) {

	Path<V, E> randomPath = new Path<V, E>();
	Set<E> visited = new HashSet<E>();

    	for(int i=0; i<hops; i++) {

	    List<E> edges = graph.getEdgesOf(source);

	    edges.removeIf(e -> visited.contains(e));

	    if (!edges.isEmpty()) {
		E edge = edges.get(RandomNumbers.getRandom(0, edges.size()));
		visited.add(edge);
		randomPath.add(new Tuple<E, V>(edge, source));
		source = graph.getTargetOf(source, edge);
	    } else {
		randomPath.clear();
		return randomPath;
	    }
	}
	return randomPath;
    }
}
