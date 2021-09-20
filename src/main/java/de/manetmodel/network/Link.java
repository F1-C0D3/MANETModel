package de.manetmodel.network;

import de.jgraphlib.graph.elements.WeightedEdge;
import de.manetmodel.units.DataRate;

public class Link<W extends LinkQuality> extends WeightedEdge<W> {
    
    private boolean isActive;
    private DataRate transmissionRate;
    private DataRate utilization;
    private int numberOfUtilizedLinks;
    
    public Link() {}

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

    public String toString() {
	return new StringBuffer("Link id: ").append(getID()).toString();
    }
    
    public boolean isOverutilized() {
	return utilization.get() > transmissionRate.get();	
    }

    public DataRate getOverUtilization() {
	if (utilization.get() > transmissionRate.get())
	    return new DataRate(utilization.get() - transmissionRate.get());
	else
	    return new DataRate(0);
    }
}
