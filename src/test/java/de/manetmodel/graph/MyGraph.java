package de.manetmodel.graph;

import java.util.function.Supplier;

import de.manetmodel.graph.ManetGraph;

public class MyGraph extends ManetGraph<MyVertex, MyEdge>{

	public MyGraph() {	
		super(new MyVertexSupplier(), new MyEdgeSupplier());			
	}
	
	public static class MyVertexSupplier implements Supplier<MyVertex> {
		public MyVertex get() {
			return new MyVertex();
		}	
	}
	
	public static class MyEdgeSupplier implements Supplier<MyEdge> {
		public MyEdge get() {
			return new MyEdge();
		}	
	}
}