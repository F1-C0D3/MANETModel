package de.manetmodel.network;

import java.util.Comparator;
import java.util.Iterator;

import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.Tuple;
import de.manetmodel.network.unit.DataRate;

public class Flow<N extends Vertex<Position2D>, L extends WeightedEdge<W>, W extends LinkQuality>
	extends Path<N, L, W> {

    private int ID;
    private static final long serialVersionUID = 1L;
    private DataRate dataRate;

    public Flow(int ID, N source, N target, DataRate bitrate) {
	super(source, target);
	setID(ID);
	this.dataRate = bitrate;
    }

    public Flow(N source, N target, DataRate bitrate) {
	super(source, target);
	this.dataRate = bitrate;
    }

    public Flow() {
    }

    public void set(int ID, N source, N target, DataRate dataRate) {
	setID(ID);
	setSource(source);
	setTarget(target);
	setDataRate(dataRate);
	this.add(new Tuple<L, N>(null, source));
    }

    public void setID(int ID) {
	this.ID = ID;
    }

    public void setDataRate(DataRate dataRate) {
	this.dataRate = dataRate;
    }

    public DataRate getDataRate() {
	return this.dataRate;
    }

    public Integer getID() {
	return ID;
    }

    public static class FlowDataRateComparator<N extends Node, L extends Link<W>, W extends LinkQuality>
	    implements Comparator<Flow<N, L, W>> {

	@Override
	public int compare(Flow<N, L, W> o1, Flow<N, L, W> o2) {
	    return Long.compare(o1.getDataRate().get(), o2.getDataRate().get());
	}

    }

    @Override
    public String toString() {
	
	StringBuilder stringBuilder = new StringBuilder();
	
	for(int i=0; i < getVertices().size(); i++) {
	    if(i>0)
		stringBuilder.append(String.format("-%d-[%d]", getEdges().get(i-1).getID(), getVertices().get(i).getID()));    
	    else
		stringBuilder.append(String.format("[%d]", getVertices().get(i).getID()));
	}
	
	return stringBuilder.toString();
    }
}