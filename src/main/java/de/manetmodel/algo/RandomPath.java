package de.manetmodel.algo;

import java.util.List;

import de.manetmodel.graph.Path;
import de.manetmodel.graph.UndirectedWeightedGraph;
import de.manetmodel.graph.Vertex;
import de.manetmodel.graph.WeightedEdge;
import de.manetmodel.util.RandomNumbers;
import de.manetmodel.util.Tuple;

public class RandomPath<P,W> {

    private UndirectedWeightedGraph<P,W> graph;

    public RandomPath(UndirectedWeightedGraph<P,W> graph) {
	this.graph = graph;
    }

    /*
     * public Path<V, E> compute(V source, V target) { Path<V, E> randomPath = new
     * Path<V, E>(); Set<E> visited = new HashSet<E>();
     * 
     * while (source.getID() != target.getID()) { List<E> edges =
     * graph.getEdgesOf(source); edges.removeIf(e -> visited.contains(e));
     * 
     * if (!edges.isEmpty()) { E edge = edges.get(RandomNumbers.getRandom(0,
     * edges.size())); visited.add(edge); randomPath.add(new Tuple<E, V>(edge,
     * source)); source = graph.getTargetOf(source, edge); } else {
     * randomPath.clear(); return randomPath; } } return randomPath; }
     * 
     */

    public Path<P,W> compute(Vertex<P> source, Vertex<P> target) {

	Path<P,W> randomPath = new Path<P,W>(source, target);

	while (!randomPath.isComplete()) {
	    
	    List<Vertex<P>> nextHops = randomPath.getUnvisitedVertices(graph.getNextHopsOf(randomPath.getLastVertex()));

	    if (!nextHops.isEmpty()) {
		Vertex<P> nextHop = nextHops.get(RandomNumbers.getRandom(0, nextHops.size()));
		randomPath.add(new Tuple<WeightedEdge<W>, Vertex<P>>(graph.getEdge(randomPath.getLastVertex(), nextHop), nextHop));
	    } else
		return new Path<P,W>(source, target);
	}
	return randomPath;
    }

    public Path<P, W> compute(Vertex<P> source, int hops) {

	Path<P,W> randomPath = new Path<P,W>(source, null);

	for (int i = 0; i < hops; i++) {

	    List<Vertex<P>> nextHops = randomPath.getUnvisitedVertices(graph.getNextHopsOf(randomPath.getLastVertex()));

	    if (!nextHops.isEmpty()) {
		Vertex<P> nextHop = nextHops.get(RandomNumbers.getRandom(0, nextHops.size()));
		randomPath.add(new Tuple<WeightedEdge<W>, Vertex<P>>(graph.getEdge(randomPath.getLastVertex(), nextHop), nextHop));
	    }
	    else
		return new Path<P,W>(source, null);
	}
	return randomPath;
    }
}
