package de.manetmodel.network.example;

import java.util.function.Supplier;

import de.jgraphlib.graph.suppliers.EdgeWeightSupplier;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.Node;

public class _MANETSupplier {

    public NodeSupplier getNodeSupplier() {
	return new NodeSupplier();
    }

    public LinkSupplier getLinkSupplier() {
	return new LinkSupplier();
    }

    public LinkQualitySupplier getLinkQualitySupplier() {
	return new LinkQualitySupplier();
    }

    public myFlowSupplier getMyFlowSupplier() {
	return new myFlowSupplier();
    }

    private class NodeSupplier implements Supplier<_Node> {
	@Override
	public _Node get() {
	    return new _Node();
	}
    }

    private class LinkSupplier implements Supplier<_Link> {
	@Override
	public _Link get() {
	    return new _Link();
	}
    }

    private class LinkQualitySupplier extends EdgeWeightSupplier<_LinkQuality> {
	@Override
	public _LinkQuality get() {
	    return new _LinkQuality();
	}
    }

    public class myFlowSupplier implements Supplier<_Flow> {
	@Override
	public _Flow get() {
	    return new _Flow();
	}
    }
}
