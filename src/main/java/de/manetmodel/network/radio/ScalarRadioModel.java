package de.manetmodel.network.radio;

import de.manetmodel.network.unit.DataRate;

public class ScalarRadioModel implements IRadioModel {

    private final double transmissionPower;
    private final double backgroundNoisePower;
    private final double bandwidth;
    private final double carrierFrequency;

    public ScalarRadioModel(double transmissionPower, double backgroundNoise, double bandwidth,
	    double carrierFrequency) {
	this.transmissionPower = transmissionPower;
	this.backgroundNoisePower = backgroundNoise;
	this.bandwidth = bandwidth;
	this.carrierFrequency = carrierFrequency;
    }

    /*
     * Computes bitrate of link based on Shannon-Hartley law
     */
    @Override
    public DataRate transmissionBitrate(double distance) {
	double power = Propagation.pathLoss(distance, Propagation.waveLength(carrierFrequency));
	return Propagation.upperBoundTransmissionBitrate(power, backgroundNoisePower, bandwidth);

    }

    /*
     * Compute reception power based on path loss rate
     */
    @Override
    public double receptionPower(double distance) {
	/** Example with 2.4Ghz **/
	return transmissionPower * Propagation.pathLoss(distance, Propagation.waveLength(carrierFrequency));
    }

}
