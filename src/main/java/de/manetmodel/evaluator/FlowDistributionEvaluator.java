package de.manetmodel.evaluator;

import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.MANET;
import de.manetmodel.units.DataRate;

public class FlowDistributionEvaluator<L extends Link<?>, F extends Flow<?,L,?>> {
        
    private LinearStandardization linkScore;      
    private LinearStandardization utilization;
    private LinearStandardization overUtilization;

    public FlowDistributionEvaluator(DoubleScope scoreScope, LinkQualityEvaluator<?,L,?> linkQualityEvaluator, int utilizationWeight, int overUtilizationWeight) {
	
	utilization = new LinearStandardization(
		new DoubleScope(
			linkQualityEvaluator.getScoreScope().min, 
			linkQualityEvaluator.getScoreScope().max), 
		1);
	
	overUtilization = new LinearStandardization(
		new DoubleScope(
			linkQualityEvaluator.getScoreScope().min, 
			linkQualityEvaluator.getScoreScope().max), 
		1);
	
	linkScore = new LinearStandardization(
		new DoubleScope(
			scoreScope.min(), 
			scoreScope.max()), 
		1);
	
	linkScore.setPropertyScope(
		new DoubleScope(
			0d, 
			linkQualityEvaluator.getScoreScope().max + 
			utilization.getScoreScope().max +
			overUtilization.getScoreScope().max));	
    }
           
    public FlowDistributionEvaluation<F> evaluate(MANET<?,L,?,F> manet) {
		
	FlowDistributionEvaluation<F> flowDistributionEvaluation = new FlowDistributionEvaluation<F>();
	
	for(F flow : manet.getFlows()) {   	 
	    
	    double flowScore = 0;
	    
	    for(L link : flow.getEdges()) {	
			
		utilization.setPropertyScope(
			new DoubleScope(
				link.getTransmissionRate().get(),
				0d));					
		
		overUtilization.setPropertyScope(
			new DoubleScope(
				manet.getMaxUtilizationOf(link).get(), 	
				0d));
		
		double overUtilizationScore = overUtilization.getScore(link.getUtilization().get());
		double utilizationScore = utilization.getScore(link.getUtilization().get());
		
		System.out.println(String.format("link %d: utilization %.2f %% utilizationScore %.2f overUtilizationScore %.2f", 
			link.getID(), 
			(double)link.getUtilization().get()/(double) link.getTransmissionRate().get(),
			utilizationScore,
			overUtilizationScore));
		
		flowScore += linkScore.getScore(
			utilization.getScore(link.getUtilization().get()) + 
			overUtilization.getScore(link.getUtilization().get()) +
			link.getWeight().getScore());
	    }
	    
	    flowDistributionEvaluation.put(flow, flowScore);
	}
	    	    	    
	flowDistributionEvaluation.setScore(flowDistributionEvaluation.getScoreSum());	
	
	return flowDistributionEvaluation;
    } 
}
