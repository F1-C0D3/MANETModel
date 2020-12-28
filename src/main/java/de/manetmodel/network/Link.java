package de.manetmodel.network;

import java.util.HashSet;
import java.util.Set;

import de.manetmodel.graph.WeightedEdge;
import de.manetmodel.network.unit.DataRate;

public class Link<L, W> extends WeightedEdge<W> {

    private double receptionPower;
    private DataRate transmissionRate;
    private DataRate utilization;

    private Set<L> interferedLinks;

    public Link() {
	interferedLinks = new HashSet<L>();
    }

    public void setReceptionPower(double receptionPower) {
	this.receptionPower = receptionPower;
    }

    public void setTransmissionRate(DataRate transmissionBitrate) {
	this.transmissionRate = transmissionBitrate;

    }

    public DataRate getTransmissionRate() {
	return this.transmissionRate;
    }

    public double getReceptionPower() {
	return this.receptionPower;
    }

    public Set<L> inReceptionRange() {
	return interferedLinks;
    }

    public void setInterferedLinks(Set<L> l) {
	interferedLinks.addAll(l);
    }

    public DataRate getUtilization() {
	return this.utilization;
    }

    public void setUtilization(DataRate u) {
	this.utilization.set(u.get());
    }

    public void increaseUtilizationBy(DataRate u) {
	this.utilization.set(utilization.get() + u.get());
    }

    @Override
    public String toString() {
	return new StringBuffer("ID: ").append(getID()).append(", Utilization: ").append(this.getUtilization())
		.toString();
    }
}
