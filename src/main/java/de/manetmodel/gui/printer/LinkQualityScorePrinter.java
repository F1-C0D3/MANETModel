package de.manetmodel.gui.printer;

import de.jgraphlib.gui.printer.EdgePrinter;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;

public class LinkQualityScorePrinter<L extends Link<W>, W extends LinkQuality>  extends EdgePrinter<L,W> {
   
    @Override
    public String print(L link) {	
	return String.format("%.2f", link.getWeight().getScore());	
    }   
}	
 