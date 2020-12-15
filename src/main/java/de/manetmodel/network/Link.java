package de.manetmodel.network;

import java.util.HashSet;
import java.util.Set;

import de.manetmodel.graph.Edge;

public class Link<L extends Link> extends Edge {

    private double receptionPower;
    private long transmissionBitrate;
    private long transmissionUtilization;
    private Set<L> interferedLinks;

    public Link() {
	interferedLinks = new HashSet<L>();
    }

    public void setReceptionPower(double receptionPower) {
	this.receptionPower = receptionPower;

    }

    public void setTransmissionRate(long transmissionBitrate) {
	this.transmissionBitrate = transmissionBitrate;

    }

    public long getTransmissionRate() {
	return this.transmissionBitrate;
    }

    public Set<L> inReceptionRange() {
	return interferedLinks;
    }

    public void setInReceptionRange(Set<L> l) {
	interferedLinks.addAll(l);
    }

    public long increaseUtilizationBy(long u) {
	this.transmissionUtilization += u;
	return transmissionBitrate - transmissionUtilization;
    }

    public double getUtilization() {
	return this.transmissionUtilization;
    }

    public double setUtilization(long u) {
	return this.transmissionUtilization = u;
    }

    @Override
    public String toString() {
	return new StringBuffer("ID: ").append(this.ID).append(", vertexID: ").append(this.vertexID1)
		.append(", vertexID: ").append(this.vertexID2).append(", Utilization: ").append(this.getUtilization())
		.toString();
    }
}
