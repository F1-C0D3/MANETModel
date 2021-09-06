package de.manetmodel.example.linkqualitycomputator;

import java.util.function.Function;

import de.jgraphlib.util.Quadruple;
import de.manetmodel.example.elements.ScalarLinkQuality;
import de.manetmodel.example.elements.ScalarRadioLink;
import de.manetmodel.example.elements.ScalarRadioNode;
import de.manetmodel.example.radio.ScalarRadioModel;
import de.manetmodel.linkqualityevaluator.ILinkWeightEvaluator;
import de.manetmodel.network.mobility.MobilityModel;
import de.manetmodel.network.unit.Watt;
import de.manetmodel.network.unit.dBm;

public class ScalarLinkQualityEvaluator
	implements ILinkWeightEvaluator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> {

    private Function<Double, Double> computeMobilityQuality;
    private Function<Quadruple<Watt, Watt, Watt, Double>, Double> computeConfidenceRange;

    private MobilityModel mobilityModel;
    private ScalarRadioModel radioModel;

    public ScalarLinkQualityEvaluator(MobilityModel mobilityModel, ScalarRadioModel radioModel) {
	this.mobilityModel = mobilityModel;
	this.radioModel = radioModel;

	this.computeConfidenceRange = (Quadruple</** transmission power **/
		Watt, /** reception power **/
		Watt, /** reception threshold **/
		Watt, /** carrier frequency **/
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
	};

    }

    @Override
    public void computeAndSetWeight(ScalarRadioNode source, ScalarRadioNode sink, ScalarRadioLink edge) {

	ScalarLinkQuality weight = edge.getWeight();

	// compute value for receptionConfidence
	weight.setReceptionConfidence(
		this.computeConfidenceRange.apply(new Quadruple<Watt, Watt, Watt, Double>(source.getTransmissionPower(),
			edge.getReceptionPower(), sink.getReceptionThreshold(), radioModel.getCarrierFrequency())));

    }

}
