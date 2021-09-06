package de.manetmodel.example.evaluator;

import de.manetmodel.evaluator.DoubleScope;
import de.manetmodel.example.elements.ScalarLinkQuality;
import de.manetmodel.example.elements.ScalarRadioLink;
import de.manetmodel.example.elements.ScalarRadioNode;
import de.manetmodel.example.radio.ScalarRadioModel;

public class ScalarLinkQualityEvaluator
	extends LinkQualityEvaluator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> {

    //private Function<Quadruple<Watt, Watt, Watt, Double>, Double> computeConfidenceRange;
    private ScalarRadioModel radioModel;

    private MobilityEvaluator<ScalarRadioNode> mobilityEvaluator;
    private ConfidenceRangeEvaluator confidenceRangeEvaluator;

    public ScalarLinkQualityEvaluator(DoubleScope scoreScope, ScalarRadioModel radioModel) {

	super(scoreScope);

	this.radioModel = radioModel;
	
	this.mobilityEvaluator = new MobilityEvaluator<ScalarRadioNode>(
		/* scoreScope */ new DoubleScope(0d, 1d),
		/* weight */ 1);

	this.confidenceRangeEvaluator = new ConfidenceRangeEvaluator(
		/* scoreScope */ new DoubleScope(0d, 1d), 
		/* weight */ 1);
	
	this.setPropertyScope(new DoubleScope(0d, mobilityEvaluator.getScoreScope().max + confidenceRangeEvaluator.getScoreScope().max));

	// -> Moved to class ConfidenceRangeEvaluator
	/*
	this.computeConfidenceRange = (Quadruple< // transmission power 
		Watt, // reception power 
		Watt, // reception threshold 
		Watt,  // carrier frequency 
		Double> radioParameters) -> {

	    double confidenceValue = 0d;

	    double confidenceMax = 0.85d;

	    Watt transmissionPower = radioParameters.getFirst();
	    dBm receptionPower = radioParameters.getSecond().todBm();
	    Watt receptionThreshold = radioParameters.getThird();
	    double carrierFrequency = radioParameters.getFourth();

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
		confidenceValue = Math
			.pow(((receptionPower.get() - receptionThresholddBm.get()) / confidenceThreshold.get()
				- receptionThresholddBm.get()), 10d);
	    }
	    return confidenceValue;
	};*/
    }

    @Override
    public boolean compute(ScalarRadioNode source, ScalarRadioLink link, ScalarRadioNode sink) {

	ScalarLinkQuality scalarLinkQuality = link.getWeight();

	// compute value for receptionConfidence
	/*scalarLinkQuality.setReceptionConfidence(
		this.computeConfidenceRange.apply(new Quadruple<Watt, Watt, Watt, Double>(source.getTransmissionPower(),
			link.getReceptionPower(), sink.getReceptionThreshold(), radioModel.getCarrierFrequency())));*/

	//scalarLinkQuality.setReceptionConfidence(confidenceRangeEvaluator.compute(source, link, sink, radioModel));
	
	scalarLinkQuality.setMobilityQuality(mobilityEvaluator.compute(source, sink));

	scalarLinkQuality.setScore(scalarLinkQuality.getMobilityQuality());

	return true;
    }

}
