package de.manetmodel.linkqualityevaluator;

import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.Node;

public interface ILinkWeightEvaluator<V extends Node, L extends Link<W>, W extends LinkQuality> {

	public abstract void computeAndSetWeight(V source, V sink, L edge);
}
