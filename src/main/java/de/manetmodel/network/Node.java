package de.manetmodel.network;

import java.util.HashSet;
import java.util.Set;

import de.manetmodel.graph.Vertex;

public class Node<L extends Link> extends Vertex {
    private double receptionSensitivity;
    private Set<L> interferredLinks;

    public Node() {
	interferredLinks = new HashSet<L>();
    }

    public void setInterferedLink(L l) {
	interferredLinks.add(l);
    }

    public Set<L> getInterferedLinks() {
	return interferredLinks;
    }

}
