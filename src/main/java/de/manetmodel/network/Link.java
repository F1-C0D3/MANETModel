package de.manetmodel.network;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.manetmodel.graph.IManetElement;
import de.manetmodel.graph.ManetEdge;

public class Link<N extends Node, Q> extends ManetEdge implements IManetElement
{
	private Q linkQuality;
	/*
	 * Links that are occupied due to transmission
	 */
	private Set<N> interferredNodes;

	public Link()
	{
		interferredNodes = new HashSet<N>();
	}

	public void setInterferredNodes(List<N> list)
	{
		interferredNodes.addAll(list);
	}

	public Set<N> getInterferenceNodes()
	{
		return this.interferredNodes;
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
