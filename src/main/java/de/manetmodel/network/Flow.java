package de.manetmodel.network;

import java.util.Comparator;
import java.util.Iterator;

import de.jgraphlib.graph.Path2D;
import de.jgraphlib.graph.Position2D;
import de.jgraphlib.graph.Vertex;
import de.jgraphlib.graph.WeightedEdge;
import de.jgraphlib.util.Tuple;
import de.manetmodel.network.unit.DataRate;

public class Flow<N extends Vertex<Position2D>, L extends WeightedEdge<W>, W extends LinkQuality>
	extends Path2D<N, L, W> {

    private int id;
    private static final long serialVersionUID = 1L;
    private DataRate rate;

    public Flow() {

    }

    public void setId(int id) {
	this.id = id;
    }

    public Flow(N source, N target, DataRate bitrate) {
	super(source, target);
	this.rate = bitrate;
    }

    public DataRate getDataRate() {
	return this.rate;
    }

    @Override
    public double getDistance() {
	double distance = 0;
	for (Tuple<L, N> tuple : this)
	    distance += tuple.getFirst().getWeight().getNumUtilizedLinks() * rate.get();
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

    public void setProperties(N source, N target, DataRate r) {
	this.rate = r;
	super.source = source;
	super.target = target;
	super.add(new Tuple<L, N>(null, source));
    }

    public Integer getId() {
	return id;
    }

    public static class FlowDataRateComparator<N extends Node,L extends Link<W>, W extends LinkQuality> implements Comparator<Flow<N, L, W>> {

	@Override
	public int compare(Flow<N, L, W> o1, Flow<N, L, W> o2) {
	    return Long.compare(o1.getDataRate().get(), o2.getDataRate().get());
	}

    }

}