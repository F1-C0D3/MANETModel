package de.manetmodel.algo;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

import de.manetmodel.graph.Path;
import de.manetmodel.graph.UndirectedWeightedGraph;
import de.manetmodel.graph.Vertex;
import de.manetmodel.graph.WeightedEdge;
import de.manetmodel.network.unit.DataRate;
import de.manetmodel.util.Tuple;

public class DijkstraShortestPath<V extends Vertex<?>, E extends WeightedEdge<?>> {

    private UndirectedWeightedGraph<V, ?, E, ?> graph;

    public DijkstraShortestPath(UndirectedWeightedGraph<V, ?, E, ?> graph) {
	this.graph = graph;
    }

    public Path<V, E> compute(V source, V target, DataRate flowDataRate, Function<Tuple<E, DataRate>, Long> metric) {

	/* Initializaton */
	V current = source;
	Path<V, E> sp = new Path<V, E>(source, target);
	List<Integer> vertices = new ArrayList<Integer>();
	List<Tuple<V, Double>> predDist = new ArrayList<Tuple<V, Double>>();

	for (V n : graph.getVertices()) {
	    vertices.add(n.getID());

	    if (n.getID() == current.getID()) {
		predDist.add(new Tuple<V, Double>(null, 0d));
	    } else {
		predDist.add(new Tuple<V, Double>(null, Double.POSITIVE_INFINITY));
	    }
	}

	while (!vertices.isEmpty()) {
	    Integer nId = minDistance(predDist, vertices);
	    vertices.remove(nId);
	    current = graph.getVertex(nId);

	    if (current.getID() == target.getID()) {
		return generateSP(predDist, sp);
	    }

	    for (V neig : graph.getNextHopsOf(current)) {

		double edgeDist = metric.apply(new Tuple<E, DataRate>(graph.getEdge(current, neig), flowDataRate));
		double oldPahtDist = predDist.get(neig.getID()).getSecond();
		double altPathDist = edgeDist + predDist.get(current.getID()).getSecond();

		if (altPathDist < oldPahtDist) {
		    predDist.get(neig.getID()).setFirst(current);
		    predDist.get(neig.getID()).setSecond(altPathDist);
		}
	    }
	}
	sp.clear();
	return sp;
    }

    private Path<V, E> generateSP(List<Tuple<V, Double>> predDist, Path<V, E> sp) {
	V t = sp.getTarget();
	List<Tuple<E, V>> copy = new ArrayList<Tuple<E, V>>();

	do {
	    V pred = predDist.get(t.getID()).getFirst();
	    copy.add(0, new Tuple<E, V>(graph.getEdge(t, pred), t));
	    t = pred;
	} while (t.getID() != sp.getSource().getID());

	sp.addAll(copy);
	return sp;
    }

    private Integer minDistance(List<Tuple<V, Double>> predT, List<Integer> v) {
	int id = -1;
	double result = Double.POSITIVE_INFINITY;
	ListIterator<Tuple<V, Double>> it = predT.listIterator();

	while (it.hasNext()) {
	    Tuple<V, Double> pred = it.next();

	    if (v.contains(it.previousIndex()) && pred.getSecond() < result) {
		result = pred.getSecond();
		id = it.previousIndex();
	    }
	}
	return id;
    }
}
