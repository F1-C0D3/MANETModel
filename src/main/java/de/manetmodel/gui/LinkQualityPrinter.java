package de.manetmodel.gui;

import de.jgraphlib.gui.EdgeWeightPrinter;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;

public class LinkQualityPrinter extends EdgeWeightPrinter<LinkQuality>{

    @Override
    public String print(LinkQuality edgeWeight) {
	StringBuilder stringBuilder = new StringBuilder();	
	stringBuilder.append(Long.toString(edgeWeight.getUtilization().get()));
	stringBuilder.append("/");
	stringBuilder.append(Long.toString(edgeWeight.getTransmissionRate().get()));
	return stringBuilder.toString();
    }
}
