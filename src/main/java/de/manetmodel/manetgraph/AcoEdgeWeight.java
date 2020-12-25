package de.manetmodel.manetgraph;

import de.manetmodel.graph.EdgeDistance;

public class AcoEdgeWeight extends EdgeDistance {

    double pheromone;
    
    public AcoEdgeWeight(double distance) {
	super(distance);
    }  
}
