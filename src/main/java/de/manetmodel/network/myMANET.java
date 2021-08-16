package de.manetmodel.network;

import java.util.function.Supplier;

import de.manetmodel.network.mobility.MobilityModel;
import de.manetmodel.network.radio.IRadioModel;

public class myMANET extends MANET<Node, Link<LinkQuality>, LinkQuality, myFlow>{

    public myMANET(Supplier<Node> vertexSupplier, Supplier<Link<LinkQuality>> edgeSupplier,
	    Supplier<LinkQuality> edgeWeightSupplier, Supplier<myFlow> flowSupplier,
	    IRadioModel radioModel, MobilityModel mobilityModel) {
	super(vertexSupplier, edgeSupplier, edgeWeightSupplier, flowSupplier, radioModel, mobilityModel);
    }
}
