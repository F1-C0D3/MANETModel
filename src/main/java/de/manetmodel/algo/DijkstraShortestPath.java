package de.manetmodel.algo;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.function.Function;

import de.manetmodel.graph.ManetGraph;
import de.manetmodel.graph.ManetPath;
import de.manetmodel.network.Link;
import de.manetmodel.network.Node;
import de.manetmodel.util.Tuple;

public class DijkstraShortestPath<N extends Node, L extends Link>
{
	private ManetGraph<N, L> manetGraph;

	public DijkstraShortestPath(ManetGraph<N, L> manetGraph)
	{
		this.manetGraph = manetGraph;
	}

	public ManetPath<N, L> compute(N source, N target, Function<L, Double> metric)
	{
		/* Initializaton */
		N current = source;
		Random random = new Random();
		ManetPath<N, L> sp = new ManetPath<N, L>(source, target);
		List<Integer> vertices = new ArrayList<Integer>();
		List<Tuple<N, Double>> predDist = new ArrayList<Tuple<N, Double>>();

		for (N n : manetGraph.getVertices())
		{
			vertices.add(n.getID());

			if (n.getID() == current.getID())
			{
				predDist.add(new Tuple<N, Double>(null, 0d));
			} else
			{
				predDist.add(new Tuple<N, Double>(null, Double.POSITIVE_INFINITY));
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

			for (N neig : manetGraph.getNextHopsOf(current))
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

	private ManetPath<N, L> generateSP(List<Tuple<N, Double>> predDist, ManetPath<N, L> sp)
	{
		N t = sp.getTarget();
		List<Tuple<L, N>> copy = new ArrayList<Tuple<L, N>>();

		do
		{
			N pred = predDist.get(t.getID()).getFirst();
			copy.add(0, new Tuple<L, N>(manetGraph.getEdge(t, pred), pred));
			t = pred;
		} while (t.getID() != sp.getSource().getID());

		sp.addAll(copy);
		return sp;
	}

	private Integer minDistance(List<Tuple<N, Double>> predT, List<Integer> v)
	{
		int id = -1;
		double result = Double.POSITIVE_INFINITY;
		ListIterator<Tuple<N, Double>> it = predT.listIterator();

		while (it.hasNext())
		{
			Tuple<N, Double> pred = it.next();

			if (v.contains(it.previousIndex()) && pred.getSecond() < result)
			{
				result = pred.getSecond();
				id = it.previousIndex();
			}
		}
		return id;
	}
}
