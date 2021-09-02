package de.manetmodel.example.linkqualitycomputator;

import java.util.function.Function;

import de.jgraphlib.util.Quadruple;
import de.manetmodel.example.elements.ScalarLinkQuality;
import de.manetmodel.example.elements.ScalarRadioLink;
import de.manetmodel.example.elements.ScalarRadioNode;
import de.manetmodel.example.radio.ScalarRadioModel;
import de.manetmodel.linkqualityevaluator.ILinkWeightEvaluator;
import de.manetmodel.network.mobility.MobilityModel;

public class ScalarLinkQualityEvaluator
	implements ILinkWeightEvaluator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> {

    private Function<Double, Double> computeMobilityQuality;
    private Function<Quadruple<Double, Double, Double, Double>, Double> computeConfidenceRange;

    private MobilityModel mobilityModel;
    private ScalarRadioModel radioModel;

    public ScalarLinkQualityEvaluator(MobilityModel mobilityModel, ScalarRadioModel radioModel) {
	this.mobilityModel = mobilityModel;
	this.radioModel = radioModel;

	this.computeConfidenceRange = (Quadruple</** transmission power **/
		Double, /** reception power **/
		Double, /** reception threshold **/
		Double, /** carrier frequency **/
		Double> radioParameters) -> {

	    double confidenceValue = 1d;

	   
//	    double confidenceMax = 0.65d;
//
//	    double transmissionPower = radioParameters.getFirst();
//	    double receptionPower = radioParameters.getSecond();
//	    double receptionThreshold = radioParameters.getThird();
//	    double carrierFrequency = radioParameters.getFourth();
//
//	    double theoreticalMaxDistance = ScalarRadioModel.Propagation.theoreticalDistance(receptionThreshold,
//		    transmissionPower, carrierFrequency);
//
//	    double maxConfidenceDistance = theoreticalMaxDistance * confidenceMax;
//
//	    double threshold = ScalarRadioModel.Propagation.receptionPower(maxConfidenceDistance, transmissionPower,
//		    carrierFrequency);
//
//	    double theoreticalMaxReceptionPower = ScalarRadioModel.Propagation.receptionPower(0d, transmissionPower,
//		    carrierFrequency);
//
//	    if (receptionPower < threshold)
//		confidenceValue = 1d - ((receptionPower - receptionThreshold) / (threshold - receptionThreshold));
//	    else
//		confidenceValue = 1d - ((receptionPower - threshold) / (theoreticalMaxReceptionPower - threshold));

	    return confidenceValue;
	};

    }

    @Override
    public void computeAndSetWeight(ScalarRadioNode source, ScalarRadioNode sink, ScalarRadioLink edge) {

	ScalarLinkQuality weight = edge.getWeight();

	// compute value for receptionConfidence
	weight.setReceptionConfidence(this.computeConfidenceRange
		.apply(new Quadruple<Double, Double, Double, Double>(source.getTransmissionPower(),
			edge.getReceptionPower(), sink.getReceptionThreshold(), radioModel.getCarrierFrequency())));

    }

}
