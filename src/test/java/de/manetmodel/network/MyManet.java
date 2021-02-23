package de.manetmodel.network;

import java.util.function.Supplier;

public class MyManet extends Manet<MyNode, MyLink, MyLinkProperties> {

    public MyManet() {
	super(new MyVertexSupplier(), new MyEdgeSupplier(), null);
    }

    public static class MyVertexSupplier implements Supplier<MyNode> {
	@Override
	public MyNode get() {
	    return new MyNode();
	}
    }

    public static class MyEdgeSupplier implements Supplier<MyLink> {
	@Override
	public MyLink get() {
	    return new MyLink();
	}
    }
}