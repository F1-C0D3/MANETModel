package de.manetmodel.network;

import de.jgraphlib.graph.elements.EdgeDistance;

public class LinkQuality extends EdgeDistance {
  
    double score;
    
    public LinkQuality() {}

    public void setScore(double score) {
	this.score = score;
    }
    
    public double getScore() {
	return score;
    }
    
}