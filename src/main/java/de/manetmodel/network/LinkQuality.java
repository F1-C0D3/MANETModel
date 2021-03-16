package de.manetmodel.network;

import java.util.List;

import de.jgraphlib.graph.EdgeDistance;
import de.jgraphlib.util.Tuple;
import de.manetmodel.network.mobility.MovementPattern;
import de.manetmodel.network.unit.DataRate;

public class LinkQuality extends EdgeDistance {

    public LinkQuality() {
	transmissionRate = new DataRate(0L);
	utilization = new DataRate(0L);
	isActive = false;
    }

    boolean isActive;

    // Reception power in dB
    private double receptionPower;

    // Rate as unit (bits,kbits,mbits,...)
    private DataRate transmissionRate;

    // Rate as unit (bits,kbits,mbits,...)
    private DataRate utilization;

    // Number of actively and passively utilized links inclusive own (in
    // interference range)
    private int numUtilizedLinks;

    Tuple<List<MovementPattern>, List<MovementPattern>> sinkAndSourceMobility;

    public Tuple<List<MovementPattern>, List<MovementPattern>> getSinkAndSourceMobility() {
	return sinkAndSourceMobility;
    }

    public void setSinkAndSourceMobility(Tuple<List<MovementPattern>, List<MovementPattern>> sinkAndSourceMobility) {
	this.sinkAndSourceMobility = sinkAndSourceMobility;
    }

    public double getReceptionPower() {
	return receptionPower;
    }

    public void setReceptionPower(double receptionPower) {
	this.receptionPower = receptionPower;
    }

    public int getNumUtilizedLinks() {
	return numUtilizedLinks;
    }

    public void setNumUtilizedLinks(int utilizedLinks) {
	this.numUtilizedLinks = utilizedLinks;
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

    public void setIsActive(boolean isParticipant) {
	this.isActive = isParticipant;

    }

    public boolean getIsActive() {
	return this.isActive;
    }

}
