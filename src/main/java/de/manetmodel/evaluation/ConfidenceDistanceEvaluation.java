package de.manetmodel.evaluation;

import de.manetmodel.evaluator.DoubleScope;
import de.manetmodel.evaluator.LinearStandardization;
import de.manetmodel.network.scalar.ScalarRadioLink;
import de.manetmodel.network.scalar.ScalarRadioModel;
import de.manetmodel.network.scalar.ScalarRadioNode;
import de.manetmodel.units.Watt;
import de.manetmodel.units.dBm;

public class ConfidenceDistanceEvaluation extends LinearStandardization {

    private ScalarRadioModel radioModel;
    public ConfidenceDistanceEvaluation(DoubleScope scoreScope, double weight,ScalarRadioModel radioModel) {
	super(scoreScope, weight);
	this.radioModel = radioModel;
	this.setPropertyScope(new DoubleScope(0d, 100d));
    }

    public double compute(ScalarRadioNode source, ScalarRadioLink link, ScalarRadioNode sink) {

	double confidenceValue = 0d;
	double confidenceMax = 0.80d;
	double confidenceMin = 0.20d;
	
	double maxTransmissionDistance = radioModel.getLinkMaxTransmissionRange();
	double minTransmissionDistance = radioModel.getLinkMinConnumicationRange();
	

	Watt transmissionPower = source.getTransmissionPower();
	dBm receptionPower = link.getReceptionPower().todBm();
	Watt receptionThreshold = sink.getReceptionThreshold();
	double carrierFrequency = radioModel.getCarrierFrequency();

	double theoreticalMaxDistance = ScalarRadioModel.Propagation.theoreticalDistance(receptionThreshold,
		transmissionPower, carrierFrequency);

	double maxConfidenceDistance = theoreticalMaxDistance * confidenceMax;
	double minConfidenceDistance = theoreticalMaxDistance * confidenceMin;

	dBm confidenceThresholdMax = ScalarRadioModel.Propagation
		.receptionPower(maxConfidenceDistance, transmissionPower, carrierFrequency).todBm();

	dBm confidenceThresholdMin = ScalarRadioModel.Propagation
		.receptionPower(minConfidenceDistance, transmissionPower, carrierFrequency).todBm();

	dBm theoreticalMaxReceptionPower = transmissionPower.todBm();

	if (receptionPower.get() > confidenceThresholdMin.get())
	    confidenceValue = Math.pow(((receptionPower.get() - theoreticalMaxReceptionPower.get())
		    / (confidenceThresholdMax.get() - theoreticalMaxReceptionPower.get())), 8d);
	else if (receptionPower.get() < confidenceThresholdMax.get()) {
	    dBm receptionThresholddBm = receptionThreshold.todBm();

	    confidenceValue = Math.pow(((receptionPower.get() - receptionThresholddBm.get())
		    / (confidenceThresholdMax.get() - (receptionThresholddBm.get()))), 8d);
	} else {
	    confidenceValue =((link.getWeight().getDistance()-minTransmissionDistance)/(maxTransmissionDistance-(maxTransmissionDistance-maxConfidenceDistance)-minTransmissionDistance));
	}
	
	confidenceValue = 1d - confidenceValue;

//	double confidenceValue = ((link.getWeight().getDistance()-35d)/65d);
	
	return confidenceValue;
    }
}
