package de.manetmodel.network.radio;

public class Propagation {

    public Propagation() {
	// TODO Auto-generated constructor stub

    }

    public static double pathLoss(double distance, double waveLength) {
	return (waveLength * waveLength) / (16 * Math.PI * Math.PI * Math.pow(distance, 2d));
    }

    public static double waveLength(double frequency) {
	return 299792458.0d / frequency;
    }

    public static double upperBoundTransmissionBitrate(double receptionPower, double noise, double frequency) {
	return (frequency * ((Math.log10(1d + (20 * Math.log10(receptionPower / noise))) / Math.log10(2) + 1e-10)));
    }

}
