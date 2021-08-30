package de.manetmodel.evaluator;

import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANET;
import de.manetmodel.network.Node;

public abstract class FlowDistributionEvaluator<N extends Node, L extends Link<W>, W extends LinkQuality, F extends Flow<N,L,W>> {

    private DoubleScope scoreScope;
    
    public FlowDistributionEvaluator(DoubleScope scoreScope) {
	this.scoreScope = scoreScope;
    }
    
    public abstract <M extends MANET<N,L,W,F>> FlowDistributionEvaluation<F> evaluate(M manet); 
}
