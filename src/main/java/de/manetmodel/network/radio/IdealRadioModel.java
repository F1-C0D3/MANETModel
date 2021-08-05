package de.manetmodel.network.radio;

import de.manetmodel.network.unit.DataRate;

public class IdealRadioModel implements IRadioModel {
    private DataRate transmissionBitrate;
    private double transmissionRange;

    public IdealRadioModel(double transmissionRange, double interferenceRange, DataRate transmissionBitrate) {
	this.transmissionBitrate = transmissionBitrate;
	this.transmissionRange = transmissionRange;
    }

    public IdealRadioModel(double transmissionRange, DataRate transmissionBitrate) {
	this(transmissionRange, transmissionRange, transmissionBitrate);
    }

    @Override
    public double receptionPower(double distance) {

	if (distance > transmissionRange) {
	    return 0d;
	} else if (distance == transmissionRange) {
	    return 0.000001d;
	} else {

	    return ((distance - 0d) / (transmissionRange - 0d));
	}

    }

    private double normalize(double value, double min, double max) {
	return 1 - ((value - min) / (max - min));
    }

    @Override
    public DataRate transmissionBitrate(double distance) {
	return transmissionBitrate;
    }

}