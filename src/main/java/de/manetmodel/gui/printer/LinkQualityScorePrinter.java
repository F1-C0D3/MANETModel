package de.manetmodel.gui.printer;

import de.jgraphlib.gui.printer.EdgeWeightPrinter;
import de.manetmodel.network.LinkQuality;

public class LinkQualityScorePrinter<W extends LinkQuality> extends EdgeWeightPrinter<W>{
   
    @Override
    public String print(W edgeWeight) {	
	return String.format("%.2f", edgeWeight.getScore());	
    }   
}	
 