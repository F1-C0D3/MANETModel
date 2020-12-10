package de.manetmodel.graph;

import java.util.function.Supplier;

public class WeightedUndirectedGraphSupplier implements Supplier<WeightedUndirectedGraph<Vertex, Edge>> 
{
	@Override
	public WeightedUndirectedGraph<Vertex, Edge> get() {
		WeightedUndirectedGraph<Vertex, Edge> graph = new WeightedUndirectedGraph<Vertex, Edge>(new VertexSupplier(), new EdgeSupplier());
		return graph;
	}	
	
	public static class VertexSupplier implements Supplier<Vertex> {
		@Override
		public Vertex get() {
			return new Vertex();
		}	
	}
	
	public static class EdgeSupplier implements Supplier<Edge> {
		@Override
		public Edge get() {
			return new Edge();
		}	
	}	 
}