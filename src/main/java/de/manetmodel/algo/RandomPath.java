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

public class RandomPath
{

	private ManetGraph<ManetVertex, ManetEdge> manetGraph;

	public RandomPath(ManetGraph<ManetVertex, ManetEdge> manetGraph)
	{
		this.manetGraph = manetGraph;
	}

	public ManetPath<ManetVertex, ManetEdge> compute(ManetVertex source, ManetVertex target)
	{
		ManetPath<ManetVertex, ManetEdge> randomPath = new ManetPath<ManetVertex, ManetEdge>();
		Set<ManetEdge> visited = new HashSet<ManetEdge>();

		while (source.getID() != target.getID())
		{
			List<ManetEdge> edges = manetGraph.getEdgesOf(source);
			edges.removeIf(e -> visited.contains(e));

			if (!edges.isEmpty())
			{
				ManetEdge edge = edges.get(RandomNumbers.getRandom(0, edges.size()));
				visited.add(edge);
				randomPath.add(new Tuple<ManetEdge, ManetVertex>(edge, source));
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
