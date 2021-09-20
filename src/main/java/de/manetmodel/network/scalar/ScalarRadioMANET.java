package de.manetmodel.network.scalar;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

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
	    MobilityModel mobilityModel,
	    ScalarLinkQualityEvaluator evaluator) {
	
	super(vertexSupplier, edgeSupplier, edgeWeightSupplier, flowSupplier, radioModel, mobilityModel, evaluator);	
    }  
    
    public ScalarRadioMANET(ScalarRadioMANET manet) {
	super(manet);
    }
    
    public ScalarRadioMANET copy() {
	return new ScalarRadioMANET(this);
    }
    
    public List<ScalarRadioMANET> nCopies(int n){
	List<ScalarRadioMANET> copies = new ArrayList<ScalarRadioMANET>();
	for(int i=0; i<n; i++) copies.add(copy());	
	return copies;
    }
}
