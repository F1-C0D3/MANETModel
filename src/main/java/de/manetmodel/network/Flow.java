package de.manetmodel.network;

import java.util.Iterator;

import de.manetmodel.graph.Path;
import de.manetmodel.util.Tuple;

public class Flow<N extends Node, L extends Link> extends Path<N, L> {
    private double bitrate;

    public Flow(N source, N target) {
	this(source, target, 0d);
    }

    public Flow(N source, N target, double bitrate) {
	super(source, target);
	this.bitrate = bitrate;
    }

    public double getBitrate() {
	return this.bitrate;
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