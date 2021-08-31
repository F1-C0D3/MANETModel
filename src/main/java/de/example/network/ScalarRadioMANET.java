package de.example.network;

import java.util.function.Supplier;

import de.example.elements.ScalarRadioFlow;
import de.example.elements.ScalarRadioLink;
import de.example.elements.ScalarRadioNode;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANET;
import de.manetmodel.network.mobility.MobilityModel;
import de.manetmodel.network.radio.IRadioModel;

public class ScalarRadioMANET extends
	MANET<ScalarRadioNode, ScalarRadioLink, LinkQuality, ScalarRadioFlow> {

    public ScalarRadioMANET(Supplier<ScalarRadioNode> vertexSupplier, Supplier<ScalarRadioLink> edgeSupplier,
	    Supplier<LinkQuality> edgeWeightSupplier,
	    Supplier<ScalarRadioFlow> flowSupplier,
	    IRadioModel<ScalarRadioNode, ScalarRadioLink, LinkQuality> radioModel,MobilityModel mobilityModel) {

	super(vertexSupplier, edgeSupplier, edgeWeightSupplier, flowSupplier, radioModel, mobilityModel);
	// TODO Auto-generated constructor stub
    }

}
