package de.manetmodel.network;

import java.util.Comparator;
import java.util.Iterator;

import de.jgraphlib.graph.algorithms.DijkstraShortestPath;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.Tuple;
import de.manetmodel.network.unit.DataRate;

public class Flow<N extends Vertex<Position2D>, L extends WeightedEdge<W>, W extends LinkQuality>
	extends DijkstraShortestPath<N, L, W> {

    private int ID;
    private static final long serialVersionUID = 1L;
    private DataRate dataRate;

    public Flow(int ID, N source, N target, DataRate bitrate) {
	super(source, target);
	this.dataRate = bitrate;
    }
    
    public Flow(N source, N target, DataRate bitrate) {
	super(source, target);
	this.dataRate = bitrate;
    }
    
    public Flow() {}
    
    public void set(int ID, N source, N target, DataRate dataRate) {
	setID(ID);
	setSource(source);
	setTarget(target);
	setDataRate(dataRate);
	this.add(new Tuple<L,N>(null, source));
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
 
    public double getDistance() {
	double distance = 0;
	for (Tuple<L, N> tuple : this)
	    distance += tuple.getFirst().getWeight().getNumberOfUtilizedLinks() * dataRate.get();
	return distance;
    }

    @Override
    public String toString() {
	StringBuffer meta = new StringBuffer("(s,t): ").append("(").append(this.getSource().getID()).append(",")
		.append(this.getTarget().getID()).append(")");
	StringBuffer pathString = new StringBuffer().append("[");
	Iterator<Tuple<L, N>> iter = this.iterator();

	while (iter.hasNext()) {
	    Tuple<L, N> ln = iter.next();
	    pathString.append(ln.getSecond().getID());

	    if (iter.hasNext()) {
		pathString.append(", ");
	    }
	}
	return meta.append(pathString.append("]")).toString();
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

}