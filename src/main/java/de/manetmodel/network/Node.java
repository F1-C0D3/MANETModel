package de.manetmodel.network;

import java.util.HashSet;
import java.util.Set;

import de.manetmodel.graph.EdgeDistance;
import de.manetmodel.graph.Position2D;
import de.manetmodel.graph.Vertex;

public class Node<W extends EdgeDistance> extends Vertex<Position2D> {

    private double receptionSensitivity;
    private Set<Link<W>> interferredLinks;

    public Node() {
	interferredLinks = new HashSet<Link<W>>();
    }

    public Node(double x, double y) {
	this();
	super.setPosition(new Position2D(x, y));
    }

    public void setInterferedLink(Link<W> l) {
	interferredLinks.add(l);
    }

    public Set<Link<W>> getInterferedLinks() {
	return interferredLinks;
    }

    @Override
    public String toString() {
	return new StringBuffer().append("ID: ").append(this.getID()).append(", #iLinks: ")
		.append(interferredLinks.size()).toString();
    }

}
