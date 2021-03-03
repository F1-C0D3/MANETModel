package de.manetmodel.network;

import java.util.function.Supplier;

import de.jgraphlib.graph.EdgeDistance;

public class ManetSupplier implements
	Supplier<MANET<Node<EdgeDistance>, Link<EdgeDistance>, EdgeDistance>, Flow<Node, Link<LinkQuality>, LinkQuality>> {

    public static class ManetLinkSupplier implements Supplier<Link<EdgeDistance>> {
	@Override
	public Link<EdgeDistance> get() {
	    return new Link<EdgeDistance>();
	}
    }

    public static class ManetNodeSupplier implements Supplier<Node<EdgeDistance>> {
	@Override
	public Node<EdgeDistance> get() {
	    return new Node<EdgeDistance>();
	}
    }

    @Override
    public MANET<Node<EdgeDistance>, Link<EdgeDistance>, EdgeDistance> get() {
	// TODO Auto-generated method stub
	return new MANET<Node<EdgeDistance>, Link<EdgeDistance>, EdgeDistance>(new ManetNodeSupplier(),
		new ManetLinkSupplier(), null);
    }

}