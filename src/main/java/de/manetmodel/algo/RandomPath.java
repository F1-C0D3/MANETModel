package de.manetmodel.algo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.manetmodel.graph.ManetEdge;
import de.manetmodel.graph.ManetGraph;
import de.manetmodel.graph.ManetPath;
import de.manetmodel.graph.ManetVertex;
import de.manetmodel.util.RandomNumbers;
import de.manetmodel.util.Tuple;

public class RandomPath<V extends ManetVertex, E extends ManetEdge>
{

	private ManetGraph<V, E> manetGraph;

	public RandomPath(ManetGraph<V, E> manetGraph)
	{
		this.manetGraph = manetGraph;
	}

	public ManetPath<V, E> compute(V source, V target)
	{
		ManetPath<V, E> randomPath = new ManetPath<V, E>();
		Set<E> visited = new HashSet<E>();

		while (source.getID() != target.getID())
		{
			List<E> edges = manetGraph.getEdgesOf(source);
			edges.removeIf(e -> visited.contains(e));

			if (!edges.isEmpty())
			{
				E edge = edges.get(RandomNumbers.getRandom(0, edges.size()));
				visited.add(edge);
				randomPath.add(new Tuple<E, V>(edge, source));
				source = manetGraph.getTargetOf(source, edge);
			} else
			{
				randomPath.clear();
				return randomPath;
			}
		}
		return randomPath;
	}
}
