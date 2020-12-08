package de.manetmodel.network;

import java.util.function.Supplier;

public class ManetSupplier implements Supplier<Manet<Node, Link>>
{
	@Override
	public Manet<Node, Link> get()
	{
		Manet<Node, Link> graph = new Manet<Node, Link>(new ManetNodeSupplier(), new ManetLinkSupplier());
		return graph;
	}

	public static class ManetLinkSupplier implements Supplier<Link>
	{
		@Override
		public Link get()
		{
			return new Link();
		}
	}

	public static class ManetNodeSupplier implements Supplier<Node>
	{
		@Override
		public Node get()
		{
			return new Node();
		}
	}

}