package de.manetmodel.algo;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

import de.manetmodel.graph.Path;
import de.manetmodel.graph.UndirectedWeightedGraph;
import de.manetmodel.graph.Vertex;
import de.manetmodel.graph.WeightedEdge;
import de.manetmodel.util.Tuple;

public class DijkstraShortestPath<P, W> {

    private UndirectedWeightedGraph<P,W> graph;

    public DijkstraShortestPath(UndirectedWeightedGraph<P, W> graph) {
	this.graph = graph;
    }

    public Path<P, W> compute(Vertex<P> source, Vertex<P> target, Function<Tuple<WeightedEdge<W>, Vertex<P>>, Double> metric) {
	/* Initializaton */
	Vertex<P> current = source;
	Path<P,W> sp = new Path<P,W>(source, target);
	List<Integer> vertices = new ArrayList<Integer>();
	List<Tuple<Vertex<P>, Double>> predDist = new ArrayList<Tuple<Vertex<P>, Double>>();

	for (Vertex<P> n : graph.getVertices()) {
	    vertices.add(n.getID());

	    if (n.getID() == current.getID()) {
		predDist.add(new Tuple<Vertex<P>, Double>(null, 0d));
	    } else {
		predDist.add(new Tuple<Vertex<P>, Double>(null, Double.POSITIVE_INFINITY));
	    }
	}

	while (!vertices.isEmpty()) {
	    Integer nId = minDistance(predDist, vertices);
	    vertices.remove(nId);
	    current = graph.getVertex(nId);

	    if (current.getID() == target.getID()) {
		return generateSP(predDist, sp);
	    }

	    for (Vertex<P> neig : graph.getNextHopsOf(current)) {
		double edgeDist = metric.apply(new Tuple<WeightedEdge<W>, Vertex<P>>(graph.getEdge(current, neig), neig));
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

    private Path<P, W> generateSP(List<Tuple<Vertex<P>, Double>> predDist, Path<P, W> sp) {
	Vertex<P> t = sp.getTarget();
	List<Tuple<WeightedEdge<W>, Vertex<P>>> copy = new ArrayList<Tuple<WeightedEdge<W>, Vertex<P>>>();

	do {
	    Vertex<P> pred = predDist.get(t.getID()).getFirst();
	    copy.add(0, new Tuple<WeightedEdge<W>, Vertex<P>>(graph.getEdge(t, pred), t));
	    t = pred;
	} while (t.getID() != sp.getSource().getID());

	sp.addAll(copy);
	return sp;
    }

    private Integer minDistance(List<Tuple<Vertex<P>, Double>> predT, List<Integer> v) {
	int id = -1;
	double result = Double.POSITIVE_INFINITY;
	ListIterator<Tuple<Vertex<P>, Double>> it = predT.listIterator();

	while (it.hasNext()) {
	    Tuple<Vertex<P>, Double> pred = it.next();

	    if (v.contains(it.previousIndex()) && pred.getSecond() < result) {
		result = pred.getSecond();
		id = it.previousIndex();
	    }
	}
	return id;
    }
}
