package de.manetmodel.network;

import java.util.function.Supplier;
import de.jgraphlib.graph.suppliers.EdgeWeightSupplier;


public class MANETSupplier {

    public NodeSupplier getNodeSupplier() {
	return new NodeSupplier();
    }

    public LinkSupplier getLinkSupplier() {
	return new LinkSupplier();
    }

    public LinkPopertiesSupplier getLinkPropertySupplier() {
	return new LinkPopertiesSupplier();
    }

    public FlowSupplier getFlowSupplier() {
	return new FlowSupplier();
    }

    public static class NodeSupplier implements Supplier<Node> {
	@Override
	public Node get() {
	    return new Node();
	}
    }

    public static class LinkSupplier implements Supplier<Link<LinkQuality>> {
	@Override
	public Link<LinkQuality> get() {
	    return new Link<LinkQuality>();
	}
    }

    public static class LinkPopertiesSupplier extends EdgeWeightSupplier<LinkQuality> {
	@Override
	public LinkQuality get() {
	    return new LinkQuality();
	}
    }

    public static class FlowSupplier implements Supplier<Flow<Node, Link<LinkQuality>, LinkQuality>> {
	@Override
	public Flow<Node, Link<LinkQuality>, LinkQuality> get() {
	    // TODO Auto-generated method stub
	    return new Flow<Node, Link<LinkQuality>, LinkQuality>();
	}
    }
}