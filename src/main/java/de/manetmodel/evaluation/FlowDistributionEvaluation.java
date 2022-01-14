package de.manetmodel.evaluation;

import java.util.HashMap;

import de.manetmodel.network.Flow;

public class FlowDistributionEvaluation<F extends Flow<?,?,?>> extends HashMap<F, Double>{
   
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    double score;
    double scoreSum;
    
    public FlowDistributionEvaluation() {
	scoreSum = 0;
    }
    
    public void setScore(double score) {
	this.score = score;
    }
    
    public double getScore() {
	return score;
    }
    
    public double getScoreSum() {
	return scoreSum;
    }
    
    public Double put(F flow, Double score){		
	scoreSum += score;
	return super.put(flow, score);
    }
}
