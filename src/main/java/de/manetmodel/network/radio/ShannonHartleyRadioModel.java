package de.manetmodel.network.radio;

public class ShannonHartleyRadioModel implements IRadioModel {

    private final double noiseThresholdPower;
    private final double transmissionPower;
    private final double backgroundNoisePower;

    public ShannonHartleyRadioModel(double noiseThresholdPower, double transmissionPower, double backgroundNoise) {
	this.noiseThresholdPower = noiseThresholdPower;
	this.transmissionPower = transmissionPower;
	this.backgroundNoisePower = backgroundNoise;
    }

    /*
     * Computes bitrate of link based on Shannon-Hartley law
     */
    @Override
    public long computeTransmissionBitrate(double distance) {
	double power = computeReception(distance);
	return (long) (2000000d * ((Math.log10(1d + (20 * Math.log10(power / 1e-11))) / Math.log10(2) + 1e-10)));
    }

    @Override
    public double computeReception(double distance) {
	/** Example with 2.4Ghz **/
	double waveLength = (299792458.0 / 2412000000d);
	return transmissionPower * (waveLength * waveLength) / (16 * Math.PI * Math.PI * Math.pow(distance, 2d));
    }

    /*
     * Not implemented yet!
     */
    @Override
    public boolean interferencePresent(double distance) {
	// TODO Auto-generated method stub
	return computeReception(distance) <= noiseThresholdPower;
    }

}
