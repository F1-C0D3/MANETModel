package de.manetmodel.gui;

import de.jgraphlib.gui.printer.EdgePrinter;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;

public class LinkQualityPrinter extends EdgePrinter<Link<LinkQuality>, LinkQuality>{

    @Override
    public String print(Link<LinkQuality> link) {	
		
	double percentage = ((double) link.getWeight().getUtilization().get() / (double) link.getWeight().getTransmissionRate().get()) * 100d;	
	
	if(link.getWeight().isActive())
	    return String.format("%d: %.2f%%", link.getID(), percentage);
	else
	    return String.format("%d: (%.2f%%)", link.getID(), percentage);
    }
}	
 