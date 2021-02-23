package de.manetmodel.network;

import java.util.Iterator;

import de.jgraphlib.graph.EdgeDistance;
import de.jgraphlib.graph.Path2D;
import de.jgraphlib.graph.Position2D;
import de.jgraphlib.graph.Vertex;
import de.jgraphlib.graph.WeightedEdge;
import de.jgraphlib.util.Tuple;
import de.manetmodel.network.unit.DataRate;

public class Flow<N extends Vertex<Position2D>, L extends WeightedEdge<W>, W extends EdgeDistance> extends Path2D<N,L,W> {

    private static final long serialVersionUID = 1L;
    private DataRate rate;

    public Flow(N source, N target, DataRate bitrate) {
	super(source, target);
	this.rate = bitrate;
    }

    public DataRate getDataRate() {
	return this.rate;
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

}