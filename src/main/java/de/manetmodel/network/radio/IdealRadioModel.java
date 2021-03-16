package de.manetmodel.network.radio;

import de.manetmodel.network.unit.DataRate;

public class IdealRadioModel implements IRadioModel {
    private DataRate transmissionBitrate;

    public IdealRadioModel(double transmissionRange, double interferenceRange, DataRate transmissionBitrate) {
	this.transmissionBitrate = transmissionBitrate;
    }

    public IdealRadioModel(double transmissionRange, DataRate transmissionBitrate) {
	this(transmissionRange, transmissionRange, transmissionBitrate);
    }

    @Override
    public double receptionPower(double distance) {
	// TODO Auto-generated method stub
	return 1d;
    }

    @Override
    public DataRate transmissionBitrate(double distance) {
	return transmissionBitrate;
    }

}