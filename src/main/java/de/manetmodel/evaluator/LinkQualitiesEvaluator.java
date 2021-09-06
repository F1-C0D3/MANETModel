package de.manetmodel.evaluator;

import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANET;
import de.manetmodel.network.Node;

public class LinkQualitiesEvaluator<N extends Node, L extends Link<W>, W extends LinkQuality, F extends Flow<N,L,W>> 
	extends FlowDistributionEvaluator<N,L,W,F> {
    
    PropertyEvaluator<W> receptionPower;
    PropertyEvaluator<Node> mobility;  
    PropertyEvaluator<LinkQuality> utilization;
    PropertyEvaluator<LinkQuality> confidenceArea;
    
    public LinkQualitiesEvaluator(
	    DoubleScope scoreScope, 
	    int receptionPowerWeight,
	    int mobilityWeight,
	    int utilizationWeight,
	    int confidenceArea) {
	
	super(scoreScope);
	
		//receptionPower = 
		//	    new PropertyEvaluator<W>(
		//		    /*property value*/	(W w) -> {return (double) w.getReceptionPower();}, 		
		//		    /*property scope*/ 	new DoubleScope(0 /*manet.getMinReceptionPower*/, 10/*manet.getMaxReceptionPower*/),
		//		    /*score scope*/	new DoubleScope(0d, 1d),
		//		    /*weight*/		receptionPowerWeight);
	    
	    //mobility = 
	    //    new PropertyEvaluator<Node>(
	    //	    /*property value*/	(Node n) -> {return (double) n.getSpeed();}, 		
	    //	    /*property scope*/ 	new DoubleScope(0 /*manet.getMaxSpeed*/,10/*manet.getMinSpeed*/),
	    //	    /*score scope*/	new DoubleScope(0d, 10d),
	    //	    /*weight*/		mobilityWeight);    
	   
	    //utilization = 
	    //new PropertyEvaluator<LinkQuality>(
	    //	    /*score*/		(LinkQuality w) -> {return (double) w.getUtilization().get();}, 
	    //	    /*property min*/	(LinkQuality w) -> {return 0d;},
	    //	    /*property max*/	(LinkQuality w) -> {return (double) w.getTransmissionRate().get();},
	    //	    /*score scope*/	new DoubleScope(0d, 10d),
	    //	    /*weight*/		utilizationWeight);  
	    
	    //confidenceArea = 
	    
	    
	    // Eigenschaft für Bewertung von Störungen
    }
    
    public <M extends MANET<N,L,W,F>> FlowDistributionEvaluation<F> evaluate(M manet) {
	
	FlowDistributionEvaluation<F> evaluation = new FlowDistributionEvaluation<F>();
	
	for(F flow : manet.getFlows()) {
	       	    
	    double score = 0;
	    
	    // Evaluate link properties
	    for(L link : flow.getEdges()) 
		score += 
			receptionPower.getScore(link.getWeight()) +
			utilization.getScore(link.getWeight());
	    
	    // Evaluate node properties
	    for(N node : flow.getVertices()) 
		score += mobility.getScore(node);
	    	    
	    // Evaluate the flow
	    evaluation.put(score, flow);
	}
	
	// Evaluate all flows in context
	
	// Sum of all FlowDistributionEvaluation scores + ?
	
	evaluation.setScore(0d /*flow distribution score*/);
		
	return evaluation;
    }  
}
