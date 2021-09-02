package de.manetmodel.linkqualityevaluator;

import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.Node;

public class LinkQualityEvaluator<N extends Node, L extends Link<W>, W extends LinkQuality>
	implements ILinkWeightEvaluator<N, L, W> {

    public LinkQualityEvaluator() {
	// TODO Auto-generated constructor stub
    }

    @Override
    public void computeAndSetWeight(N source, N sink, L edge) {
	// TODO Auto-generated method stub
	
    }


}
