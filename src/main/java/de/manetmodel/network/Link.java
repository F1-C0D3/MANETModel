package de.manetmodel.network;

import de.jgraphlib.graph.elements.WeightedEdge;
import de.manetmodel.units.DataRate;

public class Link<W extends LinkQuality> extends WeightedEdge<W> {

    public Link() {
    }

    private boolean isActive;

    private DataRate transmissionRate;


    // Rate as unit (bits, kbits, mbits,...)
    private DataRate utilization;
    
    private int numberOfUtilizedLinks;


    public int getNumberOfUtilizedLinks() {
	return numberOfUtilizedLinks;
    }

    public void setNumberOfUtilizedLinks(int numberOfUtilizedLinks) {
	this.numberOfUtilizedLinks = numberOfUtilizedLinks;
    }

    public DataRate getTransmissionRate() {
	return this.transmissionRate;
    }

    public void setTransmissionRate(DataRate transmissionBitrate) {
	this.transmissionRate = transmissionBitrate;
    }

    public DataRate getUtilization() {
	return this.utilization;
    }

    public void setUtilization(DataRate u) {
	this.utilization = u;
    }

    public void setActive() {
	this.isActive = true;
    }

    public void setPassive() {
	this.isActive = false;
    }

    public boolean isActive() {
	return this.isActive;
    }

    @Override
    public String toString() {
	return new StringBuffer("Link id: ").append(getID()).toString();
    }

    public DataRate getOverUtilization() {
	if (utilization.get() > this.transmissionRate.get())
	    return new DataRate(utilization.get() - this.transmissionRate.get());
	else
	    return new DataRate(0);
    }
}
