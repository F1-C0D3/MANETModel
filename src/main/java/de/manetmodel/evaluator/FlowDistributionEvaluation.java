package de.manetmodel.evaluator;

import java.util.TreeMap;

import de.manetmodel.network.Flow;

public class FlowDistributionEvaluation<F extends Flow<?,?,?>> extends TreeMap<Double, F>{

    private double score;   
    
    public void setScore(double score) {
	this.score = score;
    }
    
    public double getScore() {
	return this.score;
    }
    
}
