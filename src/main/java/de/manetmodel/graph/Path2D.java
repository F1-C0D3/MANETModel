package de.manetmodel.graph;

import de.manetmodel.util.Tuple;

public class Path2D<W extends EdgeDistance> extends Path<Position2D, W> {

    public double getDistance() {
	double distance = 0;
	
	for (Tuple<WeightedEdge<W>, Vertex<Position2D>> edgeVertexTuple : this)
	    if (edgeVertexTuple.getFirst() != null)
		distance += edgeVertexTuple.getFirst().getWeight().getDistance();
	
	return distance;
    }
}
