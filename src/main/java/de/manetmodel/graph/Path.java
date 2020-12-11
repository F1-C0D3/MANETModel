package de.manetmodel.graph;

import java.util.LinkedList;

import de.manetmodel.util.Tuple;

public class Path<N extends Vertex, L extends Edge> extends LinkedList<Tuple<L, N>> implements Comparable<Path<N, L>> {
    double distance;
    private N source;
    private N target;

    public Path() {
	this.source = null;
	this.target = null;
    }

    public Path(N source) {
	this.source = source;
	super.add(new Tuple<L, N>(null, source));
	this.target = null;
    }

    public Path(N source, N target) {
	this.source = source;
	super.add(new Tuple<L, N>(null, source));
	this.target = target;
    }

    public N getSource() {
	return this.source;
    }

    public N getTarget() {
	return this.target;
    }

    public boolean isShorter(Path<N, L> path) {
	if (path != null)
	    return this.distance < path.getDistance();
	return false;
    }

    public boolean clearPath() {
	if (this.size() > 1) {

	    Path<N, L> tmp = new Path<N, L>();
	    tmp.addAll(this.subList(1, this.size()));
	    this.removeAll(tmp);
	    return true;
	}
	return false;
    }

    public double getDistance() {
	return this.distance;
    }

    @Override
    public boolean add(Tuple<L, N> linkAndNode) {
	super.add(linkAndNode);
	this.distance += linkAndNode.getFirst().getDistance();
	return true;
    }

    @Override
    public String toString() {
	String str = "";
	for (Tuple<L, N> linkAndNode : this) {
	    if (linkAndNode.getFirst() != null)
		str += String.format("- %.2f -", linkAndNode.getFirst().getDistance());
	    str += String.format("[%s]", linkAndNode.getSecond().toString());
	}
	return str;
    }

    public int compareDistanceTo(Path<N, L> path) {
	return Double.compare(this.distance, path.getDistance());
    }

    @Override
    public int compareTo(Path<N, L> path) {
	return Double.compare(this.distance, path.getDistance());
    }
}
