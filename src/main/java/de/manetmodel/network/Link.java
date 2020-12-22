package de.manetmodel.network;

import java.util.HashSet;
import java.util.Set;

import de.manetmodel.graph.Edge;
import de.manetmodel.network.unit.DataRate;

public class Link<L extends Link> extends Edge {

    private double receptionPower;
    private DataRate transmissionRate;
    private double occupation;
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

    public void setInReceptionRange(Set<L> l) {
	interferedLinks.addAll(l);
    }

    public double getOccupation() {
	return this.occupation;
    }

    public double setOccupation(double o) {
	return this.occupation = o;
    }

    @Override
    public String toString() {
	return new StringBuffer("ID: ").append(this.ID).append(", vertexID: ").append(this.vertexID1)
		.append(", vertexID: ").append(this.vertexID2).append(", Utilization: ").append(this.getOccupation())
		.toString();
    }
}
