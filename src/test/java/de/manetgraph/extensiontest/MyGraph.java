package de.manetgraph.extensiontest;

import java.util.function.Supplier;

import de.manetgraph.ManetGraph;

public class MyGraph extends ManetGraph<MyVertex, MyEdge>{

	public MyGraph() {
		
		super(new MyVertexSupplier(), new MyEdgeSupplier());
			
	}
	
	public static class MyVertexSupplier implements Supplier<MyVertex> {
		@Override
		public MyVertex get() {
			return new MyVertex();
		}	
	}
	
	public static class MyEdgeSupplier implements Supplier<MyEdge> {
		@Override
		public MyEdge get() {
			return new MyEdge();
		}	
	}
}