package de.manetmodel.network;

import java.util.HashSet;
import java.util.Set;

import de.manetmodel.graph.EdgeDistance;
import de.manetmodel.graph.WeightedEdge;
import de.manetmodel.network.unit.DataRate;

public class Link<W> extends WeightedEdge<W> {

    private double receptionPower;
    private DataRate transmissionRate;
    private double occupation;
    
    private Set<Link<W>> interferedLinks; 

    public Link() {
	interferedLinks = new HashSet<Link<W>>();
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

    public void setInReceptionRange(Set<Link<W>> l) {
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
	return new StringBuffer("ID: ").append(getID()).append(", Utilization: ").append(this.getOccupation())
		.toString();
    }
}
