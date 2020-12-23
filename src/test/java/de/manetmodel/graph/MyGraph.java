package de.manetmodel.graph;

import java.util.function.Supplier;

public class MyGraph extends WeightedUndirectedGraph<MyVertex, MyEdge>{

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