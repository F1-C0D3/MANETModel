package de.manetmodel.network;

import java.util.HashSet;
import java.util.Set;

import de.manetmodel.graph.IManetElement;
import de.manetmodel.graph.ManetVertex;

public class Node<L extends Link> extends ManetVertex implements IManetElement
{
	private double receptionSensitivity;
	private Set<L> interferredLinks;

	public Node()
	{
		interferredLinks = new HashSet<L>();
	}

	public void setInterferedLink(L l)
	{
		interferredLinks.add(l);
	}

	public Set<L> getInterferedLinks()
	{
		return interferredLinks;
	}
}
