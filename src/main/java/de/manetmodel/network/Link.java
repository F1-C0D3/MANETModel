package de.manetmodel.network;

import java.util.HashSet;
import java.util.Set;

import de.manetmodel.graph.WeightedEdge;
import de.manetmodel.network.unit.DataRate;

public class Link<W> extends WeightedEdge<W> {

    private double receptionPower;
    private DataRate transmissionRate;
    private DataRate utilization;
    private boolean isActive; // true if link is part of a path

    private Set<Link<W>> interferedLinks;

    public Link() {
	this.interferedLinks = new HashSet<Link<W>>();
	this.utilization = new DataRate(0L);
	isActive = false;
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

    public Set<Link<W>> inReceptionRange() {
	return interferedLinks;
    }

    public void setInterferedLinks(Set<Link<W>> l) {
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

    public void setToParticipant(boolean isParticipant) {
	this.isActive = isParticipant;

    }

    public boolean isParticipant() {
	return this.isActive;
    }
}
