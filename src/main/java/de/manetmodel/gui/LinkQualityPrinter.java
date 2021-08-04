package de.manetmodel.gui;

import de.jgraphlib.gui.EdgePrinter;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;

public class LinkQualityPrinter extends EdgePrinter<Link<LinkQuality>, LinkQuality>{

    @Override
    public String print(Link<LinkQuality> link) {	
	/*StringBuilder stringBuilder = new StringBuilder();	
	stringBuilder.append(Long.toString(link.getWeight().getUtilization().get()));
	stringBuilder.append("/");
	stringBuilder.append(Long.toString(link.getWeight().getTransmissionRate().get()));
	return stringBuilder.toString();*/
	
	double percentage = ((double) link.getWeight().getUtilization().get() / (double) link.getWeight().getTransmissionRate().get()) * 100d;	
	return String.format("%.2f %%", percentage);
    }
}	
