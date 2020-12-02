package de.manetmodel.network;

import java.util.function.Supplier;

import de.manetmodel.graph.ManetGraph;

public class MyManet extends ManetGraph<MyNode, MyLink>{

	public MyManet() {	
		super(new MyVertexSupplier(), new MyEdgeSupplier());			
	}
	
	public static class MyVertexSupplier implements Supplier<MyNode> {
		public MyNode get() {
			return new MyNode();
		}	
	}
	
	public static class MyEdgeSupplier implements Supplier<MyLink> {
		public MyLink get() {
			return new MyLink();
		}	
	}
}