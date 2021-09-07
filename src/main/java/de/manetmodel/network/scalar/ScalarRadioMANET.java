package de.manetmodel.network.scalar;

import java.util.function.Supplier;

import de.manetmodel.evaluator.DoubleScope;
import de.manetmodel.evaluator.ScalarLinkQualityEvaluator;
import de.manetmodel.mobilitymodel.MobilityModel;
import de.manetmodel.network.MANET;

public class ScalarRadioMANET extends MANET<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow> {

    public ScalarRadioMANET(
	    Supplier<ScalarRadioNode> vertexSupplier, 
	    Supplier<ScalarRadioLink> edgeSupplier,
	    Supplier<ScalarLinkQuality> edgeWeightSupplier, 
	    Supplier<ScalarRadioFlow> flowSupplier,
	    ScalarRadioModel radioModel, 
	    MobilityModel mobilityModel) {
	
	super(vertexSupplier, edgeSupplier, edgeWeightSupplier, flowSupplier, radioModel, mobilityModel, new ScalarLinkQualityEvaluator(new DoubleScope(0d,1d), radioModel, mobilityModel));	
    }  
}
