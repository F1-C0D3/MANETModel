package de.manetmodel.network;

import java.util.HashSet;
import java.util.Set;

import de.jgraphlib.graph.WeightedEdge;

public class Link<W extends LinkQuality> extends WeightedEdge<W> {

    // Links, that are actively and passively affected (in interference range)
    private Set<Integer> utilizedLinkIds;

    // Indicates, whether Link is occupied actively through transmission as part of
    // a flow
    public Link() {
	utilizedLinkIds = new HashSet<Integer>();
    }

    public Set<Integer> getUtilizedLinkIds() {
	return utilizedLinkIds;
    }

    public void setUtilizedLinks(Set<Integer> l) {
	utilizedLinkIds.addAll(l);
    }

    @Override
    public String toString() {
	return new StringBuffer("ID: ").append(getID()).toString();
    }

}
