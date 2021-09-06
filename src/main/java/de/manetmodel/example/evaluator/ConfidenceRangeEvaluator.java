package de.manetmodel.example.evaluator;

import de.manetmodel.evaluator.DoubleScope;
import de.manetmodel.example.elements.ScalarRadioLink;
import de.manetmodel.example.elements.ScalarRadioNode;
import de.manetmodel.example.radio.ScalarRadioModel;
import de.manetmodel.network.unit.Watt;
import de.manetmodel.network.unit.dBm;

public class ConfidenceRangeEvaluator extends PropertyStandardization {

    public ConfidenceRangeEvaluator(DoubleScope scoreScope, double weight) {
	super(scoreScope, weight);
    }

    public double compute(ScalarRadioNode source, ScalarRadioLink link, ScalarRadioNode sink,
	    ScalarRadioModel scalarRadioModel) {

	double confidenceValue = 0d;
	double confidenceMax = 0.85d;

	Watt transmissionPower = source.getTransmissionPower();
	dBm receptionPower = sink.getReceptionThreshold().todBm();
	Watt receptionThreshold = sink.getReceptionThreshold();
	double carrierFrequency = scalarRadioModel.getCarrierFrequency();

	double theoreticalMaxDistance = ScalarRadioModel.Propagation.theoreticalDistance(receptionThreshold,
		transmissionPower, carrierFrequency);

	double maxConfidenceDistance = theoreticalMaxDistance * confidenceMax;

	dBm confidenceThreshold = ScalarRadioModel.Propagation
		.receptionPower(maxConfidenceDistance, transmissionPower, carrierFrequency).todBm();

	dBm theoreticalMaxReceptionPower = new dBm(0d);

	if (receptionPower.get() > confidenceThreshold.get())
	    confidenceValue = 1d - ((receptionPower.get() - confidenceThreshold.get())
		    / (theoreticalMaxReceptionPower.get() - confidenceThreshold.get()));
	else {
	    dBm receptionThresholddBm = receptionThreshold.todBm();
	    confidenceValue = Math.pow(((receptionPower.get() - receptionThresholddBm.get()) / confidenceThreshold.get()
		    - receptionThresholddBm.get()), 10d);	   
	}
		
	super.setPropertyScope(new DoubleScope(0d, maxConfidenceDistance));
	
	return getScore(confidenceValue);
    }
}	
