package de.manetmodel.network.example;

import java.util.function.Supplier;

import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANET;
import de.manetmodel.network.Node;
import de.manetmodel.network.mobility.MobilityModel;
import de.manetmodel.network.radio.IRadioModel;

public class myMANET extends MANET<Node, Link<LinkQuality>, LinkQuality, myFlow>{

    public myMANET(
	    Supplier<Node> vertexSupplier, 
	    Supplier<Link<LinkQuality>> edgeSupplier,
	    Supplier<LinkQuality> edgeWeightSupplier, 
	    Supplier<myFlow> flowSupplier,
	    IRadioModel radioModel, 
	    MobilityModel mobilityModel) {
	
	super(vertexSupplier, edgeSupplier, edgeWeightSupplier, flowSupplier, radioModel, mobilityModel);
    }
    
    public myMANET(myMANET manet) {
	super(manet);
    }
    
    public myMANET copy() {
	return new myMANET(this);
    } 
}
