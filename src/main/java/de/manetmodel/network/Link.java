package de.manetmodel.network;

import java.util.HashSet;
import java.util.Set;

import de.manetmodel.graph.Edge;

public class Link<L extends Link> extends Edge {

    private double receptionPower;
    private double transmissionBitrate;
    private double utilization;
    private Set<L> interferedLinks;

    public Link() {
	interferedLinks = new HashSet<L>();
    }

    public void setReceptionPower(double receptionPower) {
	this.receptionPower = receptionPower;

    }

    public void setTransmissionRate(double transmissionBitrate) {
	this.transmissionBitrate = transmissionBitrate;

    }

    public Set<L> inReceptionRange() {
	return interferedLinks;
    }

    public void setInReceptionRange(Set<L> l) {
	interferedLinks.addAll(l);
    }

    public double increaseUtilizationBy(double u) {

	return transmissionBitrate - utilization;
    }

    private double getUtilization() {

	return utilization;
    }

    @Override
    public String toString() {
	return new StringBuffer("ID: ").append(this.ID).append(", vertexID: ").append(this.vertexID1)
		.append(", vertexID: ").append(this.vertexID2).append(", Utilization: ").append(this.getUtilization())
		.toString();
    }
}
