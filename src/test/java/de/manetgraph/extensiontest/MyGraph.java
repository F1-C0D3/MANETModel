package de.manetgraph.extensiontest;

import java.util.function.Supplier;

import de.manetgraph.ManetGraph;

public class MyGraph extends ManetGraph<MyVertex, MyEdge>{

	public MyGraph() {
		super(MyEdge.class);
				
		MyVertex a = new MyVertex(1,1);
		MyVertex b = new MyVertex(1,1);
		
		this.setEdgeSupplier(new MyEdgeSupplier());
		this.setVertexSupplier(new MyVertexSupplier());
				
		this.addVertex(a);
		MyEdge edge = this.addEdge(a, b);	
		this.setEdgeWeight(edge, 5);
	}
	
	public class MyVertexSupplier implements Supplier<MyVertex> {
		@Override
		public MyVertex get() {
			return new MyVertex(0, 0);
		}	
	}
	
	public class MyEdgeSupplier implements Supplier<MyEdge> {
		@Override
		public MyEdge get() {
			return new MyEdge();
		}	
	}
}


