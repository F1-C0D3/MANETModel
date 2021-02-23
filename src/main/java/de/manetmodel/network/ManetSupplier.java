package de.manetmodel.network;

import java.util.function.Supplier;

import de.jgraphlib.graph.EdgeWeightSupplier;

public class ManetSupplier {

    public NodeSupplier getNodeSupplier() {
	return new NodeSupplier();
    }

    public LinkSupplier getLinkSupplier() {
	return new LinkSupplier();
    }

    public LinkPopertiesSupplier getLinkPropertySupplier() {
	return new LinkPopertiesSupplier();
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

    private class LinkPopertiesSupplier extends EdgeWeightSupplier<LinkQuality> {
	@Override
	public LinkQuality get() {
	    return new LinkQuality();
	}
    }
}