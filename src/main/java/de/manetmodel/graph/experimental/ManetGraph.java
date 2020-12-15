package de.manetmodel.graph.experimental;

import java.util.function.Function;
import java.util.function.Supplier;

public class ManetGraph<V extends ManetVertex, E extends ManetEdge<W>, W> extends UndirectedWeighted3DGraph<V, E, W> {

    public ManetGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier) {
	super(vertexSupplier, edgeSupplier);
    }

    public void initialize(Function<Double /*function parameter*/, Double /*return type*/> transmissionRange) {

    }

    public V addVertex(double x, double y) {
	return super.addVertex(new Position3D(x, y, 0));
    }
}
