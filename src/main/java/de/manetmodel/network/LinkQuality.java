package de.manetmodel.network;

import java.util.List;

import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.util.Tuple;
import de.manetmodel.network.mobility.MovementPattern;
import de.manetmodel.network.unit.DataRate;

public class LinkQuality extends EdgeDistance {

    boolean isActive;

    // Reception power in dB
    private double receptionPower;

    // Rate as unit (bits, kbits, mbits,...)
    private DataRate transmissionRate;

    // Rate as unit (bits, kbits, mbits,...)
    private DataRate utilization;

    // Number of actively and passively utilized links inclusive own (in
    // interference range)
    private int numberOfUtilizedLinks;

    Tuple<List<MovementPattern>, List<MovementPattern>> sinkAndSourceMobility;
    
    public LinkQuality() {}
      
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
