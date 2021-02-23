package de.manetmodel.network;

import de.jgraphlib.graph.EdgeDistance;
import de.manetmodel.network.unit.DataRate;

public class LinkQuality extends EdgeDistance {

    // Amount of interfered links
    private int interference;
    
    // Reception power in dB
    private double receptionPower;
    
    // 
    private DataRate transmissionRate;
    
    //
    private DataRate utilization;
    
    public int getInterference() {
	return interference;
    }
    
    public void setInterference(int interference) {
	this.interference = interference;
    }
    
    public double getReceptionPower() {
	return receptionPower;
    }
    
    public void setReceptionPower(double receptionPower) {
	this.receptionPower = receptionPower;
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
	this.utilization.set(u.get());
    }
    
}
