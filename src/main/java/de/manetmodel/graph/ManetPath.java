package de.manetmodel.graph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import de.manetmodel.util.Tuple;

public class ManetPath<N extends ManetVertex, L extends ManetEdge> extends LinkedList<Tuple<L, N>>
		implements Comparable<ManetPath<N, L>>
{

	double distance;
	Set<Integer> occupation;
	private N source;
	private N target;

	public ManetPath(N source, N target)
	{
		this();
		this.source = source;
		this.target = target;

	}

	public ManetPath()
	{
		this.occupation = new HashSet<Integer>();
		this.distance = 0;
	}

	public N getSource()
	{
		return this.source;
	}

	public N getTarget()
	{
		return this.target;
	}

	public boolean isShorter(ManetPath<N, L> path)
	{
		if (path != null)
			return this.distance < path.getDistance();
		return false;
	}

	public double getDistance()
	{
		return this.distance;
	}

	public Set<Integer> getOccupation()
	{
		return this.occupation;
	}

	@Override
	public boolean add(Tuple<L, N> linkAndNode)
	{
		super.add(linkAndNode);
		this.distance += linkAndNode.getFirst().getDistance();
		this.occupation.addAll(linkAndNode.getFirst().getOccupation());
		return true;
	}

	@Override
	public String toString()
	{
		String str = "";
		for (Tuple<L, N> linkAndNode : this)
		{
			if (linkAndNode.getFirst() != null)
				str += String.format("- %.2f -", linkAndNode.getFirst().getDistance());
			str += String.format("[%s]", linkAndNode.getSecond().toString());
		}
		return str;
	}

	public int compareDistanceTo(ManetPath<N, L> path)
	{
		return Double.compare(this.distance, path.getDistance());
	}

	public int compareOccupationTo(ManetPath<N, L> path)
	{
		return Integer.compare(this.occupation.size(), path.getOccupation().size());
	}

	@Override
	public int compareTo(ManetPath<N, L> path)
	{
		return Double.compare(this.distance, path.getDistance());
	}
}
