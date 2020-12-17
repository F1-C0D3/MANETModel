package de.manetmodel.network.radio;

public class ScalarRadioModel implements IRadioModel {

    private final double interferenceThreshold;
    private final double transmissionPower;
    private final double backgroundNoisePower;
    private final double bandwidth;
    private final double carrierFrequency;

    public ScalarRadioModel(double interferenceThreshold, double transmissionPower, double backgroundNoise,
	    double bandwidth, double carrierFrequency) {
	this.interferenceThreshold = interferenceThreshold;
	this.transmissionPower = transmissionPower;
	this.backgroundNoisePower = backgroundNoise;
	this.bandwidth = bandwidth;
	this.carrierFrequency = carrierFrequency;
    }

    /*
     * Computes bitrate of link based on Shannon-Hartley law
     */
    @Override
    public long transmissionBitrate(double distance) {
	double power = Propagation.pathLoss(distance, Propagation.waveLength(carrierFrequency));
	return (long) Propagation.upperBoundTransmissionBitrate(power, backgroundNoisePower, bandwidth);

    }

    /*
     * Compute reception power based on path loss rate
     */
    @Override
    public double receptionPower(double distance) {
	/** Example with 2.4Ghz **/
	return transmissionPower * Propagation.pathLoss(distance, Propagation.waveLength(carrierFrequency));
    }

    @Override
    public boolean interferencePresent(double distance) {
	// TODO Auto-generated method stub
	return receptionPower(distance) <= interferenceThreshold;
    }

}
