package de.manetmodel.network;

import de.jgraphlib.graph.elements.EdgeDistance;
import de.manetmodel.network.unit.DataRate;

public class LinkQuality extends EdgeDistance {

    private boolean isActive;

    // Reception power in dB
    private double receptionPower;

    // Rate as unit (bits, kbits, mbits,...)
    private DataRate transmissionRate;

    // Rate as unit (bits, kbits, mbits,...)
    private DataRate utilization;

    // Number of actively and passively utilized links inclusive own (in
    // interference range)
    private int numberOfUtilizedLinks;

    public LinkQuality() {
    }

    public double getReceptionPower() {
	return receptionPower;
    }

    public void setReceptionPower(double receptionPower) {
	this.receptionPower = receptionPower;
    }

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
	return String.format("%s", utilization.toString());
    }
}
