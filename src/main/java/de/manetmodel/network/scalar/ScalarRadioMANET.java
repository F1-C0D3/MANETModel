package de.manetmodel.network.scalar;

import java.util.function.Supplier;

import de.manetmodel.evaluator.ScalarLinkQualityEvaluator;
import de.manetmodel.mobilitymodel.MobilityModel;
import de.manetmodel.network.MANET;
import de.manetmodel.radiomodel.RadioModel;

public class ScalarRadioMANET extends MANET<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow> {

    public ScalarRadioMANET(Supplier<ScalarRadioNode> vertexSupplier, Supplier<ScalarRadioLink> edgeSupplier,
	    Supplier<ScalarLinkQuality> edgeWeightSupplier, Supplier<ScalarRadioFlow> flowSupplier,
	    RadioModel<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> radioModel, MobilityModel mobilityModel,
	    ScalarLinkQualityEvaluator linkQualityEvaluator) {

	super(vertexSupplier, edgeSupplier, edgeWeightSupplier, flowSupplier, radioModel, mobilityModel, linkQualityEvaluator);
    }

}
