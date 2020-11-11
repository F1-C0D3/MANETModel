package de.manetgraph;

import java.util.function.Supplier;

public class ManetGraphSupplier implements Supplier<ManetGraph<ManetVertex, ManetEdge>> {
	@Override
	public ManetGraph<ManetVertex, ManetEdge> get() {
		ManetGraph<ManetVertex, ManetEdge> graph = new ManetGraph<ManetVertex, ManetEdge>(new ManetVertexSupplier(), new ManetEdgeSupplier());
		return graph;
	}	
	
	public static class ManetEdgeSupplier implements Supplier<ManetEdge> {
		@Override
		public ManetEdge get() {
			return new ManetEdge();
		}	
	}
	
	public static class ManetVertexSupplier implements Supplier<ManetVertex> {
		@Override
		public ManetVertex get() {
			return new ManetVertex();
		}	
	}	 
}