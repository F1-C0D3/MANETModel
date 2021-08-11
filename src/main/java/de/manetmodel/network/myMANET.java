package de.manetmodel.network;

import java.util.function.Supplier;

import de.manetmodel.network.mobility.MobilityModel;
import de.manetmodel.network.radio.IRadioModel;

public class myMANET extends MANET<Node, Link<LinkQuality>, LinkQuality, Flow<Node,Link<LinkQuality>, LinkQuality>>{

    public myMANET(Supplier<Node> vertexSupplier, Supplier<Link<LinkQuality>> edgeSupplier,
	    Supplier<LinkQuality> edgeWeightSupplier, Supplier<Flow<Node, Link<LinkQuality>, LinkQuality>> flowSupplier,
	    IRadioModel radioModel, MobilityModel mobilityModel) {
	super(vertexSupplier, edgeSupplier, edgeWeightSupplier, flowSupplier, radioModel, mobilityModel);
    }
}
