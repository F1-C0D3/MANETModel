package de.manetmodel.network;

import de.jgraphlib.graph.elements.WeightedEdge;

public class Link<W extends LinkQuality> extends WeightedEdge<W> {

    public Link() {}
    
    @Override
    public String toString() {
	return new StringBuffer("Link id: ").append(getID()).toString();
    }
}
