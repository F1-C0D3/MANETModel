package de.manetmodel.example.radio;

import de.manetmodel.example.elements.ScalarLinkQuality;
import de.manetmodel.example.elements.ScalarRadioLink;
import de.manetmodel.example.elements.ScalarRadioNode;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.radio.IRadioModel;
import de.manetmodel.network.radio.RadioModel;
import de.manetmodel.network.unit.DataRate;
import de.manetmodel.network.unit.DataUnit;

public class ScalarRadioModel  implements IRadioModel<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> {

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
    
    public double getCarrierFrequency() {
	return this.carrierFrequency;
    }
    
    @Override
    public void setLinkRadioParameters(ScalarRadioLink link, double linkDistance) {

	double power = Propagation.pathLoss(linkDistance, Propagation.waveLength(carrierFrequency));
	DataRate transmissionRate = Propagation.upperBoundTransmissionBitrate(power, backgroundNoisePower, bandwidth);

	double receptionPower = transmissionPower
		* Propagation.pathLoss(linkDistance, Propagation.waveLength(carrierFrequency));

	link.setTransmissionRate(transmissionRate);
	link.setReceptionPower(receptionPower);

    }

    @Override
    public void setNodeRadioParameters(ScalarRadioNode node, double coverageRange) {
	double receptionThreshold = transmissionPower
		* Propagation.pathLoss(coverageRange, Propagation.waveLength(carrierFrequency));

	node.setTransmissionPower(transmissionPower);
	node.setReceptionThreshold(receptionThreshold);
	node.setCarrierFrequency(carrierFrequency);

    }

    public static class Propagation {

	public static double theoreticalDistance(double radioWavePower, double transmissionPower,
		double carrierFrequency) {
	    double distance = (transmissionPower * waveLength(carrierFrequency) * waveLength(carrierFrequency))
		    / (16 * Math.PI * Math.PI * radioWavePower);
	    return distance;
	}

	public static double pathLoss(double distance, double waveLength) {
	    return (waveLength * waveLength) / (16 * Math.PI * Math.PI * Math.pow(distance, 2d));
	}

	public static double waveLength(double frequency) {
	    return 299792458.0d / frequency;
	}

	public static double receptionPower(double linkDistance, double transmissionPower, double carrierFrequency) {
	    return transmissionPower * pathLoss(linkDistance, Propagation.waveLength(carrierFrequency));
	}

	public static DataRate upperBoundTransmissionBitrate(double receptionPower, double noise, double frequency) {
	    return new DataRate(
		    frequency * ((Math.log10(1d + (20 * Math.log10(receptionPower / noise))) / Math.log10(2) + 1e-10)),
		    DataUnit.Type.bit);
	}
    }

}
