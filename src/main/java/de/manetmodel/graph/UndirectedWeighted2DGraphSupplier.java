package de.manetmodel.graph;

import java.util.function.Supplier;

public class UndirectedWeighted2DGraphSupplier<W>
	implements Supplier<UndirectedWeightedGraph<Vertex<Position2D>, Position2D, WeightedEdge<W>, W>> {
    
    @Override
    public UndirectedWeightedGraph<Vertex<Position2D>, Position2D, WeightedEdge<W>, W> get() {
	
	return new UndirectedWeightedGraph<Vertex<Position2D>, Position2D, WeightedEdge<W>, W>(new VertexSupplier(), new EdgeSupplier());
    }
    
    public VertexSupplier getVertexSupplier() {
	return new VertexSupplier();
    }
    
    public EdgeSupplier getEdgeSupplier() {
	return new EdgeSupplier();
    }

    public class VertexSupplier implements Supplier<Vertex<Position2D>> {
	@Override
	public Vertex<Position2D> get() {
	    return new Vertex<Position2D>();
	}
    }

    public class EdgeSupplier implements Supplier<WeightedEdge<W>> {
	@Override
	public WeightedEdge<W> get() {
	    return new WeightedEdge<W>();
	}
    }
}