package de.manetmodel.network;

import java.util.Arrays;
import java.util.List;

import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.manetmodel.network.mobility.MovementPattern;

public class Node extends Vertex<Position2D> {

    List<MovementPattern> mobility;

    public Node() {
    }

    public List<MovementPattern> getMobility() {
	return mobility;
    }

    public void setMobility(List<MovementPattern> mobility) {
	this.mobility = mobility;
    }
    
    public void setMobility(MovementPattern mobility) {
	this.mobility = Arrays.asList(mobility);
    }

    public Node(double x, double y) {
	this();
	super.setPosition(new Position2D(x, y));
    }

    public List<MovementPattern> getPrevMobility() {
	return mobility;
    }

    public void setPrevMobility(List<MovementPattern> prevMobility) {
	this.mobility = prevMobility;
    }

    @Override
    public String toString() {
	return new StringBuffer().append("Node id: ").append(this.getID()).toString();
    }

}
