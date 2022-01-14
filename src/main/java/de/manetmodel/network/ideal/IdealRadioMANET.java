package de.manetmodel.network.ideal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import de.manetmodel.evaluator.LinkQualityEvaluator;
import de.manetmodel.evaluator.ScalarLinkQualityEvaluator;
import de.manetmodel.mobilitymodel.MobilityModel;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANET;

public class IdealRadioMANET extends MANET<IdealRadioNode,IdealRadioLink, IdealLinkQuality, IdealRadioFlow> {

    public IdealRadioMANET(
	    Supplier<IdealRadioNode> vertexSupplier, 
	    Supplier<IdealRadioLink> edgeSupplier,
	    Supplier<IdealLinkQuality> edgeWeightSupplier, 
	    Supplier<IdealRadioFlow> flowSupplier,
	    IdealRadioModel radioModel, 
	    MobilityModel mobilityModel,
	    LinkQualityEvaluator<IdealRadioNode, IdealRadioLink, IdealLinkQuality> evaluator) {
	
	super(vertexSupplier, edgeSupplier, edgeWeightSupplier, flowSupplier, radioModel, mobilityModel, evaluator);	
    }  
    
    public IdealRadioMANET(IdealRadioMANET manet) {
	super(manet);
    }
    
    public IdealRadioMANET copy() {
	return new IdealRadioMANET(this);
    }
    
    public List<IdealRadioMANET> nCopies(int n){
	List<IdealRadioMANET> copies = new ArrayList<IdealRadioMANET>();
	for(int i=0; i<n; i++) copies.add(copy());	
	return copies;
    }
}
