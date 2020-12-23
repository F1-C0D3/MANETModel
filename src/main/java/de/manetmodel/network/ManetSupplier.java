package de.manetmodel.network;

import java.util.function.Supplier;

public class ManetSupplier<W> implements Supplier<Manet<Node<Link<W>,W>, Link<W>, W>> {
    
    @Override
    public Manet<Node<Link<W>, W>, Link<W>, W> get() {
	return new Manet<Node<Link<W>, W>, Link<W>, W>(null, null, null);
    }

    public static class ManetLinkSupplier<L,W> implements Supplier<Link<W>> {
	@Override
	public Link<W> get() {
	    return new Link<W>();
	}
    }

    public static class ManetNodeSupplier<W> implements Supplier<Node<Link<W>, W>> {
	@Override
	public Node<Link<W>, W> get() {
	    return new Node<Link<W>, W>();
	}
    }
}