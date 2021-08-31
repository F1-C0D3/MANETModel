package de.manetmodel.gui;

import de.jgraphlib.gui.printer.EdgeWeightPrinter;
import de.manetmodel.network.LinkQuality;

public class LinkQualityPrinter<W extends LinkQuality> extends EdgeWeightPrinter<W>{
   
    @Override
    public String print(W edgeWeight) {
//	double percentage = ((double) edgeWeight.getUtilization().get() / (double) edgeWeight.getTransmissionRate().get()) * 100d;	
//	return String.format("%.2f%%", percentage);
	
	//Paramteres not yet obtained. 
	return edgeWeight.getDistance().toString();
	
	
    }
    
}	
 