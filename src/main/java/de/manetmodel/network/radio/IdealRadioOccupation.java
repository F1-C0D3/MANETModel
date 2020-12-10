package de.manetmodel.network.radio;

public class IdealRadioOccupation extends RadioOccupationModel {
    private double transmissionRange;
    private double interferenceRange;
    private double transmissionBitrate;

    public IdealRadioOccupation(double transmissionRange, double interferenceRange, double transmissionBitrate) {
	this.transmissionRange = transmissionRange;
	this.interferenceRange = interferenceRange;
	this.transmissionBitrate = transmissionBitrate;
    }

    public IdealRadioOccupation(double transmissionRange, double transmissionBitrate) {
	this(transmissionRange, transmissionRange, transmissionBitrate);
    }

    @Override
    public double computeTransmissionBitrate() {
	// TODO Auto-generated method stub
	return transmissionBitrate;
    }

    @Override
    public double computeReception(double distance) {
	// TODO Auto-generated method stub
	return 1d;
    }

    @Override
    public boolean interferencePresent(double distance) {
	if (distance < interferenceRange)
	    return true;
	return false;
    }
}