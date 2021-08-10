package de.manetmodel.network;

import de.jgraphlib.graph.elements.WeightedEdge;
import de.manetmodel.network.unit.DataRate;

public class Link<W extends LinkQuality> extends WeightedEdge<W> {

    public Link() {}
    
    @Override
    public String toString() {
	return new StringBuffer("Link id: ").append(getID()).toString();
    }
    
    public DataRate getOverUtilization() {	
	if(getWeight().getUtilization().get() > getWeight().getTransmissionRate().get())
	    return new DataRate(getWeight().getUtilization().get() - getWeight().getTransmissionRate().get());
	else
	    return new DataRate(0);	
    }
}
