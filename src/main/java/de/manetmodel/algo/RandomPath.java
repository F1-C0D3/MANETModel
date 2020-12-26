package de.manetmodel.algo;

import java.util.List;

import de.manetmodel.graph.Path;
import de.manetmodel.graph.Position2D;
import de.manetmodel.graph.UndirectedWeighted2DGraph;
import de.manetmodel.graph.UndirectedWeightedGraph;
import de.manetmodel.graph.Vertex;
import de.manetmodel.graph.WeightedEdge;
import de.manetmodel.util.RandomNumbers;
import de.manetmodel.util.Tuple;

public class RandomPath<V extends Vertex<?>, E extends WeightedEdge<?>> {

    private UndirectedWeightedGraph<V,?,E,?> graph;

    public RandomPath(UndirectedWeightedGraph<V,?,E,?> graph) {
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

    public Path<V,E> compute(V source, V target) {

	Path<V,E> randomPath = new Path<V,E>(source, target);

	while (!randomPath.isComplete()) {
	    
	    List<V> nextHops = randomPath.getUnvisitedVertices(graph.getNextHopsOf(randomPath.getLastVertex()));

	    if (!nextHops.isEmpty()) {
		V nextHop = nextHops.get(RandomNumbers.getRandom(0, nextHops.size()));
		randomPath.add(new Tuple<E, V>(graph.getEdge(randomPath.getLastVertex(), nextHop), nextHop));
	    } else
		return new Path<V,E>(source, target);
	}
	return randomPath;
    }

    public Path<V,E> compute(V source, int hops) {

	Path<V,E> randomPath = new Path<V,E>(source, null);

	for (int i = 0; i < hops; i++) {

	    List<V> nextHops = randomPath.getUnvisitedVertices(graph.getNextHopsOf(randomPath.getLastVertex()));

	    if (!nextHops.isEmpty()) {
		V nextHop = nextHops.get(RandomNumbers.getRandom(0, nextHops.size()));
		randomPath.add(new Tuple<E, V>(graph.getEdge(randomPath.getLastVertex(), nextHop), nextHop));
	    }
	    else
		return new Path<V,E>(source, null);
	}
	return randomPath;
    }
}
