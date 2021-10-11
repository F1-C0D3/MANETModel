package de.manetmodel.gui;

import de.jgraphlib.gui.printer.EdgePrinter;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;

public class LinkUtilizationPrinter<L extends Link<W>, W extends LinkQuality> extends EdgePrinter<L,W> {

    @Override
    public String print(L link) {
	double percentage = ((double) link.getUtilization().get() / (double) link.getTransmissionRate().get()) * 100d;	
	return String.format("%.2f%%", percentage);
    }

}
