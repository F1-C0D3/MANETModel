package de.manetmodel.evaluator;

import de.manetmodel.network.scalar.ScalarRadioLink;
import de.manetmodel.network.scalar.ScalarRadioModel;
import de.manetmodel.network.scalar.ScalarRadioNode;
import de.manetmodel.units.Watt;
import de.manetmodel.units.dBm;

public class ConfidenceRangeEvaluator extends LinearStandardization {

    public ConfidenceRangeEvaluator(DoubleScope scoreScope, double weight) {
	super(scoreScope, weight);
    }

    public double compute(ScalarRadioNode source, ScalarRadioLink link, ScalarRadioNode sink,
	    ScalarRadioModel scalarRadioModel) {

	double confidenceValue = 0d;
	double confidenceMax = 0.85d;

	Watt transmissionPower = source.getTransmissionPower();
	dBm receptionPower = link.getReceptionPower().todBm();
	Watt receptionThreshold = sink.getReceptionThreshold();
	double carrierFrequency = scalarRadioModel.getCarrierFrequency();

	double theoreticalMaxDistance = ScalarRadioModel.Propagation.theoreticalDistance(receptionThreshold,
		transmissionPower, carrierFrequency);

	double maxConfidenceDistance = theoreticalMaxDistance * confidenceMax;

	dBm confidenceThreshold = ScalarRadioModel.Propagation
		.receptionPower(maxConfidenceDistance, transmissionPower, carrierFrequency).todBm();

	dBm theoreticalMaxReceptionPower = transmissionPower.todBm();

	if (receptionPower.get() > confidenceThreshold.get())
	    confidenceValue = Math.pow(((receptionPower.get() - theoreticalMaxReceptionPower.get())
		    / (confidenceThreshold.get() - theoreticalMaxReceptionPower.get())), 4);
	else {
	    dBm receptionThresholddBm = receptionThreshold.todBm();

	    confidenceValue = Math.pow(((receptionPower.get() - receptionThresholddBm.get())
		    / (confidenceThreshold.get() - (receptionThresholddBm.get()))), 10d);
	}

	this.setPropertyScope(new DoubleScope(0d, 1d));

	return getScore(confidenceValue);
    }
}
