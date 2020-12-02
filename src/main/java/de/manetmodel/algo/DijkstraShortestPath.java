package de.manetmodel.algo;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.function.Function;

import de.manetmodel.graph.ManetEdge;
import de.manetmodel.graph.ManetGraph;
import de.manetmodel.graph.ManetPath;
import de.manetmodel.graph.ManetVertex;
import de.manetmodel.util.Tuple;

public class DijkstraShortestPath<V extends ManetVertex, E extends ManetEdge>
{
	private ManetGraph<V, E> manetGraph;

	public DijkstraShortestPath(ManetGraph<V, E> manetGraph)
	{
		this.manetGraph = manetGraph;
	}

	public ManetPath<V, E> compute(V source, V target, Function<E, Double> metric)
	{
		/* Initializaton */
		V current = source;
		Random random = new Random();
		ManetPath<V, E> sp = new ManetPath<V, E>(source, target);
		List<Integer> vertices = new ArrayList<Integer>();
		List<Tuple<V, Double>> predDist = new ArrayList<Tuple<V, Double>>();

		for (V n : manetGraph.getVertices())
		{
			vertices.add(n.getID());

			if (n.getID() == current.getID())
			{
				predDist.add(new Tuple<V, Double>(null, 0d));
			} else
			{
				predDist.add(new Tuple<V, Double>(null, Double.POSITIVE_INFINITY));
			}

		}

		while (!vertices.isEmpty())
		{
			Integer nId = minDistance(predDist, vertices);
			vertices.remove(nId);
			current = manetGraph.getVertex(nId);

			if (current.getID() == target.getID())
			{
				return generateSP(predDist, sp);
			}

			for (V neig : manetGraph.getNextHopsOf(current))
			{
				double edgeDist = metric.apply(manetGraph.getEdge(current, neig));
				double oldPahtDist = predDist.get(neig.getID()).getSecond();

				double altPathDist = edgeDist + predDist.get(current.getID()).getSecond();

				if (altPathDist < oldPahtDist)
				{
					predDist.get(neig.getID()).setFirst(current);
					predDist.get(neig.getID()).setSecond(altPathDist);
				}
			}
		}
		sp.clear();
		return sp;
	}

	private ManetPath<V, E> generateSP(List<Tuple<V, Double>> predDist, ManetPath<V, E> sp)
	{
		V t = sp.getTarget();
		List<Tuple<E, V>> copy = new ArrayList<Tuple<E, V>>();

		do
		{
			V pred = predDist.get(t.getID()).getFirst();
			copy.add(0, new Tuple<E, V>(manetGraph.getEdge(t, pred), pred));
			t = pred;
		} while (t.getID() != sp.getSource().getID());

		sp.addAll(copy);
		return sp;
	}

	private Integer minDistance(List<Tuple<V, Double>> predT, List<Integer> v)
	{
		int id = -1;
		double result = Double.POSITIVE_INFINITY;
		ListIterator<Tuple<V, Double>> it = predT.listIterator();

		while (it.hasNext())
		{
			Tuple<V, Double> pred = it.next();

			if (v.contains(it.previousIndex()) && pred.getSecond() < result)
			{
				result = pred.getSecond();
				id = it.previousIndex();
			}
		}
		return id;
	}
}
