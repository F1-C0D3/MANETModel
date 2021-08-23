package de.manetmodel.network.example;

import java.util.function.Supplier;

import de.jgraphlib.graph.suppliers.EdgeWeightSupplier;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.Node;

public class myMANETSupplier {

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

    private class NodeSupplier implements Supplier<Node> {
	@Override
	public Node get() {
	    return new Node();
	}
    }

    private class LinkSupplier implements Supplier<Link<LinkQuality>> {
	@Override
	public Link<LinkQuality> get() {
	    return new Link<LinkQuality>();
	}
    }

    private class LinkQualitySupplier extends EdgeWeightSupplier<LinkQuality> {
	@Override
	public LinkQuality get() {
	    return new LinkQuality();
	}
    }

    public class myFlowSupplier implements Supplier<myFlow> {
	@Override
	public myFlow get() {
	    return new myFlow();
	}
    }
}
