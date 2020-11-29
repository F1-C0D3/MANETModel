package de.manetmodel.graph;

import java.util.LinkedList;

import de.manetmodel.util.Tuple;

public class ManetPath<N extends ManetVertex, L extends ManetEdge> extends LinkedList<Tuple<L, N>> implements Comparable<ManetPath<N, L>>
{
	double distance;
	private N source;
	private N target;

	public ManetPath(){
		this.source = null;
		this.target = null;
	}
	
	public ManetPath(N source){
		this.source = source;
		super.add(new Tuple<L, N>(null, source));
		this.target = null;
	}
	
	public ManetPath(N source, N target){
		this.source = source;
		super.add(new Tuple<L, N>(null, source));
		this.target = target;
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

	@Override
	public boolean add(Tuple<L, N> linkAndNode)
	{
		super.add(linkAndNode);
		this.distance += linkAndNode.getFirst().getDistance();
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

	@Override
	public int compareTo(ManetPath<N, L> path)
	{
		return Double.compare(this.distance, path.getDistance());
	}
}
