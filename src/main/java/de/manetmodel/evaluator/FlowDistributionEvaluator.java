package de.manetmodel.evaluator;

import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.MANET;

public class FlowDistributionEvaluator<L extends Link<?>, F extends Flow<?,L,?>> {
        
    private LinearStandardization linkScore;
    
    private UtilizationEvaluator utilizationEvaluator; 

    public FlowDistributionEvaluator(DoubleScope scoreScope, MANET<?,L,?,F> manet, LinkQualityEvaluator<?,L,?> linkQualityEvaluator, int utilizationWeight) {
	
	utilizationEvaluator = 
		new UtilizationEvaluator(
			new DoubleScope(linkQualityEvaluator.getScoreScope().min, linkQualityEvaluator.getScoreScope().max),  
			utilizationWeight * linkQualityEvaluator.getWeight(), 
			manet);
	
	linkScore = new LinearStandardization(new DoubleScope(0,100d), 1);
	
	linkScore.setPropertyScope(
		new DoubleScope(
			0d, 
			linkQualityEvaluator.getScoreScope().max + utilizationEvaluator.getScoreScope().max));	
    }
    
    public FlowDistributionEvaluation<F> evaluate(MANET<?,L,?,F> manet) {
		
	FlowDistributionEvaluation<F> flowDistributionEvaluation = new FlowDistributionEvaluation<F>();
	
	for(F flow : manet.getFlows()) {   	 
	    
	    double flowScore = 0;
	    
	    for(L link : flow.getEdges()) 				
		flowScore += linkScore.getScore(
			utilizationEvaluator.getScore(link.getUtilization().get()) + 
			link.getWeight().getScore());
	    
	    flowDistributionEvaluation.put(flow, flowScore);
	}
	    	    	    
	flowDistributionEvaluation.setScore(flowDistributionEvaluation.getScoreSum());	
	
	return flowDistributionEvaluation;
    } 
}
