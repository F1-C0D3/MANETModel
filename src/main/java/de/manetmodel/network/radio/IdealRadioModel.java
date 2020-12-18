package de.manetmodel.network.radio;

public class IdealRadioModel implements IRadioModel {
    private double transmissionRange;
    private double interferenceRange;
    private long transmissionBitrate;

    public IdealRadioModel(double transmissionRange, double interferenceRange, long transmissionBitrate) {
	this.transmissionRange = transmissionRange;
	this.interferenceRange = interferenceRange;
	this.transmissionBitrate = transmissionBitrate;
    }

    public IdealRadioModel(double transmissionRange, long transmissionBitrate) {
	this(transmissionRange, transmissionRange, transmissionBitrate);
    }

    @Override
    public double receptionPower(double distance) {
	// TODO Auto-generated method stub
	return 1d;
    }

    @Override
    public boolean interferencePresent(double distance) {
	if (distance <= interferenceRange)
	    return true;
	return false;
    }

    @Override
    public long transmissionBitrate(double distance) {
	return transmissionBitrate;
    }
}