package de.manetmodel.network;

import java.util.function.Supplier;

public class ManetSupplier implements Supplier<Manet<Node<Link>, Link>>
{
	@Override
	public Manet<Node<Link>, Link> get()
	{
		return new Manet<Node<Link>, Link>(new ManetNodeSupplier(), new ManetLinkSupplier());
	}

	public static class ManetLinkSupplier implements Supplier<Link>
	{
		@Override
		public Link get()
		{
			return new Link();
		}
	}

	public static class ManetNodeSupplier implements Supplier<Node<Link>>
	{
		@Override
		public Node get()
		{
			return new Node();
		}
	}

}