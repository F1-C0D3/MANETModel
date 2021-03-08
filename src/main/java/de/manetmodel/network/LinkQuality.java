package de.manetmodel.network;

import com.opencsv.bean.CsvBindByName;

import de.jgraphlib.graph.EdgeDistance;
import de.manetmodel.network.unit.DataRate;

public class LinkQuality extends EdgeDistance {

    public LinkQuality() {
	transmissionRate = new DataRate(0L);
	utilization = new DataRate(0L);
    }

    // Amount of interfered links
    private int interference;

    // Reception power in dB
    private double receptionPower;

    // Rate as unit (bits,kbits,mbits,...)
    private DataRate transmissionRate;

    // Rate as unit (bits,kbits,mbits,...)
    @CsvBindByName(column = "utilization")
    private DataRate utilization;

    // Number of actively and passively utilized links inclusive own (in
    // interference range)
    @CsvBindByName(column = "numUtilizedLinks")
    private int numUtilizedLinks;

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

}
