package de.manetmodel.example.network;

import java.util.function.Supplier;

import de.manetmodel.example.elements.ScalarLinkQuality;
import de.manetmodel.example.elements.ScalarRadioFlow;
import de.manetmodel.example.elements.ScalarRadioLink;
import de.manetmodel.example.elements.ScalarRadioNode;
import de.manetmodel.network.MANET;
import de.manetmodel.network.mobility.MobilityModel;
import de.manetmodel.network.radio.IRadioModel;

public class ScalarRadioMANET extends
	MANET<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow> {

    public ScalarRadioMANET(Supplier<ScalarRadioNode> vertexSupplier, Supplier<ScalarRadioLink> edgeSupplier,
	    Supplier<ScalarLinkQuality> edgeWeightSupplier,
	    Supplier<ScalarRadioFlow> flowSupplier,
	    IRadioModel<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> radioModel,MobilityModel mobilityModel) {

	super(vertexSupplier, edgeSupplier, edgeWeightSupplier, flowSupplier, radioModel, mobilityModel);
	// TODO Auto-generated constructor stub
    }

}
