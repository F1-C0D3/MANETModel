package de.manetmodel.graph;

import de.manetmodel.util.Tuple;

public class Path2D<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance> extends Path<V, E> {

    public Path2D() {
	this.source = null;
	this.target = null;
    }
    
    public Path2D(V source, V target) {
	super.source = source;
	super.target = target;
	super.add(new Tuple<E, V>(null, source));
    }
    
    public double getDistance() {
	double distance = 0;
	
	for (Tuple<E, V> edgeVertexTuple : this)
	    if (edgeVertexTuple.getFirst() != null)
		distance += edgeVertexTuple.getFirst().getWeight().getDistance();
	
	return distance;
    }
}
