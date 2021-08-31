package de.manetmodel.network;

import java.util.List;

import de.jgraphlib.graph.elements.EdgeDistance;
import de.manetmodel.network.mobility.MovementPattern;

public class LinkQuality extends EdgeDistance {

    private List<MovementPattern> sinkNodeMobility;
    private double linkConfidenceValue;

    public List<MovementPattern> getSinkNodeMobility() {
	return sinkNodeMobility;
    }

    public void setSinkNodeMobility(List<MovementPattern> sinkNodeMobility) {
	this.sinkNodeMobility = sinkNodeMobility;
    }

    public double getLinkConfidenceValue() {
	return linkConfidenceValue;
    }

    public void setLinkConfidenceValue(double linkConfidenceValue) {
	this.linkConfidenceValue = linkConfidenceValue;
    }

    public LinkQuality() {
    }


}