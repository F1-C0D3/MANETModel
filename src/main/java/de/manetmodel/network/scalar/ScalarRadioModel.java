package de.manetmodel.network.scalar;

import de.manetmodel.radiomodel.RadioModel;
import de.manetmodel.units.DataRate;
import de.manetmodel.units.DataUnit;
import de.manetmodel.units.Watt;

public class ScalarRadioModel extends RadioModel<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> {

    private final Watt transmissionPower;
    private final Watt backgroundNoisePower;
    private final double bandwidth;
    private final double carrierFrequency;

    public ScalarRadioModel(Watt transmissionPower, Watt backgroundNoise, double bandwidth, double carrierFrequency,double linkMaxTransmissionRange) {
	super(new DataRate(0),linkMaxTransmissionRange);
	this.transmissionPower = transmissionPower;
	this.backgroundNoisePower = backgroundNoise;
	this.bandwidth = bandwidth;
	this.carrierFrequency = carrierFrequency;
    }

    public double getCarrierFrequency() {
	return this.carrierFrequency;
    }

    @Override
    public void setLinkRadioParameters(ScalarRadioLink link, double linkDistance) {

	Watt pathloss = Propagation.pathLoss(linkDistance, Propagation.waveLength(carrierFrequency));
	DataRate transmissionRate = Propagation.upperBoundTransmissionBitrate(pathloss, backgroundNoisePower,
		bandwidth);

	Watt receptionPower = Propagation.receptionPower(linkDistance, transmissionPower, carrierFrequency);
	link.setTransmissionRate(transmissionRate);
	link.setReceptionPower(receptionPower);
    }

    @Override
    public void setNodeRadioParameters(ScalarRadioNode node, double coverageRange) {
	Watt receptionThreshold = Propagation.receptionPower(coverageRange, transmissionPower, carrierFrequency);
	node.setTransmissionPower(transmissionPower);
	node.setReceptionThreshold(receptionThreshold);
	node.setCarrierFrequency(carrierFrequency);
    }

    public static class Propagation {

	public static double theoreticalDistance(Watt radioWavePower, Watt transmissionPower, double carrierFrequency) {
	    double distance = Math
		    .sqrt((transmissionPower.get() * waveLength(carrierFrequency) * waveLength(carrierFrequency))
			    / (16 * Math.PI * Math.PI * radioWavePower.get()));
	    return distance;
	}

	public static Watt pathLoss(double distance, double waveLength) {
	    return new Watt((waveLength * waveLength) / (16 * Math.PI * Math.PI * Math.pow(distance, 2d)));
	}

	public static double waveLength(double frequency) {
	    return 299792458.0d / frequency;
	}

	public static Watt receptionPower(double linkDistance, Watt transmissionPower, double carrierFrequency) {

	    return new Watt(transmissionPower.get()
		    * Math.min(1d, pathLoss(linkDistance, Propagation.waveLength(carrierFrequency)).get()));
//	    
//	    return new dBm(10 * (Math.log10(1000d * (transmissionPower.get()
//		    * pathLoss(linkDistance, Propagation.waveLength(carrierFrequency)).get()))));
	}

	public static DataRate upperBoundTransmissionBitrate(Watt receptionPower, Watt noise, double frequency) {
	    return new DataRate(
		    frequency * ((Math.log10(1d + (20 * Math.log10(receptionPower.get() / noise.get()))) / Math.log10(2)
			    + 1e-10)),
		    DataUnit.Type.bit);
	}
    }

}
