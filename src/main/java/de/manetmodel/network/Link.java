package de.manetmodel.network;

import java.util.List;
import java.util.Set;

import de.manetmodel.graph.IManetElement;
import de.manetmodel.graph.ManetEdge;

public class Link<N, Q> extends ManetEdge implements IManetElement
{
	private Q linkQuality;
	/*
	 * Links that are occupied due to transmission
	 */
	private Set<N> interferredLinks;

	public void setInterferredNodes(List<N> list)
	{
		interferredLinks.addAll(list);
	}

	public Q getLinkQuality()
	{
		return this.linkQuality;
	}

	public void setLinkQuality(Q linkQuality)
	{
		this.linkQuality = linkQuality;
	}

}
