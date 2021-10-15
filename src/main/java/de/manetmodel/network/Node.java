package de.manetmodel.network;

import java.util.Arrays;
import java.util.List;

import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.manetmodel.mobilitymodel.MovementPattern;

public class Node extends Vertex<Position2D> {

    List<MovementPattern> mobility;

    public Node() {
    }

    public List<MovementPattern> getMobilityCharacteristic() {
	return mobility;
    }

    public void setMobilityCharacteristic(List<MovementPattern> mobility) {
	this.mobility = mobility;
    }
    
    public void appendMovementPattern(MovementPattern pattern) {
	mobility.add(pattern);
    }
    
    public MovementPattern getPreviousMobilityPattern () {
	if (mobility==null || mobility.size()==0)
	    return null;
	
	return mobility.get(mobility.size()-1);
    }
    
    public List<MovementPattern> getNPreviousMobilityPatterns (int n) {
	if (mobility==null || mobility.size()<n)
	    return null;
	
	return mobility.subList(mobility.size()-(1+n), mobility.size()-1);
    }
    
    public void setMobility(MovementPattern mobility) {
	this.mobility = Arrays.asList(mobility);
    }

    public Node(double x, double y) {
	this();
	super.setPosition(new Position2D(x, y));
    }

    @Override
    public String toString() {
	return new StringBuffer().append("Node id: ").append(this.getID()).toString();
    }

}
