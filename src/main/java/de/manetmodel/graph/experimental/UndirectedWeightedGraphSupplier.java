package de.manetmodel.graph.experimental;

import java.util.function.Supplier;

public class UndirectedWeightedGraphSupplier<P,W> implements Supplier<UndirectedWeightedGraph<Vertex<P>, WeightedEdge<W>, P, W>> 
{
	@Override
	public UndirectedWeightedGraph<Vertex<P>, WeightedEdge<W>, P, W>get() {
		return new UndirectedWeightedGraph<Vertex<P>, WeightedEdge<W>, P, W>(new VertexSupplier(), new EdgeSupplier());
	}	
	
	public class VertexSupplier implements Supplier<Vertex<P>> {
		@Override
		public Vertex<P> get() {
			return new Vertex<P>();
		}	
	}
	
	public class EdgeSupplier implements Supplier<WeightedEdge<W>> {
		@Override
		public WeightedEdge<W> get() {
			return new WeightedEdge<W>();
		}	
	}	 
}