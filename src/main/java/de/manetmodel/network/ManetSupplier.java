package de.manetmodel.network;

import java.util.function.Supplier;

public class ManetSupplier<W> implements Supplier<Manet<Node<Link<W>>, Link<W>, W>> {
    
    @Override
    public Manet<Node<Link<W>>, Link<W>, W> get() {
	return new Manet<Node<Link<W>>, Link<W>, W>(new ManetNodeSupplier<W>(), new ManetLinkSupplier<W>(), null);
    }

    public static class ManetLinkSupplier<W> implements Supplier<Link<W>> {
	@Override
	public Link<W> get() {
	    return new Link<W>();
	}
    }

    public static class ManetNodeSupplier<W> implements Supplier<Node<Link<W>>> {
	@Override
	public Node<Link<W>> get() {
	    return new Node<Link<W>>();
	}
    }
}