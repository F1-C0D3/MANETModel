package de.manetmodel.network;

import de.manetmodel.graph.Path;

public class Flow<N extends Node<L>, L extends Link> extends Path<N, L>
{
	private double bitrate;

	public Flow(N source, N target)
	{
		this(source, target, 0d);
	}

	public Flow(N source, N target, double bitrate)
	{
		super(source, target);
		this.bitrate = bitrate;
	}
}
