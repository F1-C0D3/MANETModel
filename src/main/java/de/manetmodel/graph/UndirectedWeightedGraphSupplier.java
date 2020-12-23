package de.manetmodel.graph;

import java.util.function.Supplier;

public class UndirectedWeightedGraphSupplier<P, W>
	implements Supplier<UndirectedWeightedGraph<Vertex<P>, P, WeightedEdge<W>, W>> {

    @Override
    public UndirectedWeightedGraph<Vertex<P>, P, WeightedEdge<W>, W> get() {

	return new UndirectedWeightedGraph<Vertex<P>, P, WeightedEdge<W>, W>(new VertexSupplier(), new EdgeSupplier());
    }

    public VertexSupplier getVertexSupplier() {
	return new VertexSupplier();
    }

    public EdgeSupplier getEdgeSupplier() {
	return new EdgeSupplier();
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