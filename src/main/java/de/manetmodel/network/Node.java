package de.manetmodel.network;

import java.util.HashSet;
import java.util.Set;

import de.manetmodel.graph.EdgeWeight;
import de.manetmodel.graph.Position2D;
import de.manetmodel.graph.Vertex;

public class Node<L extends Link<L, W>, W extends EdgeWeight> extends Vertex<Position2D> {

    private double receptionSensitivity;
    private Set<L> interferredLinks;

    public Node(double x, double y) {
	super.setPosition(new Position2D(x, y));
	interferredLinks = new HashSet<L>();
    }

    public void setInterferedLink(L l) {
	interferredLinks.add(l);
    }

    public Set<L> getInterferedLinks() {
	return interferredLinks;
    }

    @Override
    public String toString() {
	return new StringBuffer().append("ID: ").append(this.getID()).append(", #iLinks: ")
		.append(interferredLinks.size()).toString();
    }

}
