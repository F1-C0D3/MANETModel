package de.manetmodel.evaluator;

import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.Node;

public abstract class LinkQualityEvaluator<N extends Node, L extends Link<W>, W extends LinkQuality> extends LinearStandardization {
    
    public LinkQualityEvaluator(DoubleScope scoreScope, double weight) {
	super(scoreScope, weight);
    }
    
    public LinkQualityEvaluator(DoubleScope scoreScope) {
	super(scoreScope, 1);
    }

    public abstract boolean compute(N source, L link, N sink);    
}