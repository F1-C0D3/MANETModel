package de.manetmodel.network;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.manetmodel.network.mobility.MovementPattern;

public class Node extends Vertex<Position2D> {

    double speed;
    
    private Set<Link<?>> interferredLinks;

    List<MovementPattern> mobility;

    public Node() {
	interferredLinks = new HashSet<Link<?>>();
    }

    public Node(double x, double y) {
	this();
	super.setPosition(new Position2D(x, y));
    }

    public void setInterferedLink(Link<?> l) {
	interferredLinks.add(l);
    }

    public Set<Link<?>> getInterferedLinks() {
	return interferredLinks;
    }

    public List<MovementPattern> getPrevMobility() {
	return mobility;
    }

    public void setPrevMobility(List<MovementPattern> prevMobility) {
	this.mobility = prevMobility;
    }
    
    public double getSpeed() {
	return speed;
    }

    @Override
    public String toString() {
	return new StringBuffer().append("Node id: ").append(this.getID()).toString();
    }

}
