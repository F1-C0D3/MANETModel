package de.manetmodel.network;

import java.util.function.Supplier;

public class ManetSupplier implements Supplier<Manet<Node, Link, RadioWavePropagation<?>>>
{
	@Override
	public Manet<Node, Link, RadioWavePropagation<?>> get()
	{
		Manet<Node, Link, RadioWavePropagation<?>> graph = new Manet<Node, Link, RadioWavePropagation<?>>(
				new ManetNodeSupplier(), new ManetLinkSupplier());
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