package de.manetgraph;

import java.util.Objects;

import org.jgrapht.graph.DefaultWeightedEdge;

import de.manetgraph.util.Tuple;

public class ManetEdge implements IManetEdge, Comparable<ManetEdge> {

	int ID;
	int vertexID1;
	int vertexID2;
	ManetCost cost;
	
	public void setID(int ID) {
		this.ID = ID;
		this.cost = new ManetCost();
	}
	
	public int getID() {
		return this.ID;
	}
		
	public void setVertexIDs(int vertexID1, int vertexID2) {
		this.vertexID1 = vertexID1;
		this.vertexID2 = vertexID2;
	}
	
	public Tuple<Integer,Integer> getVertexIDs(){
		return new Tuple<Integer,Integer>(this.vertexID1, vertexID2);
	}
	
	public int compareTo(ManetEdge edge) {	
		return Double.compare(edge.getDistance(), this.getDistance());
	}
	
	public int compareDistance() {
		return 0;
	}
	
	public int compareOccupation() {
		return 0;
	}
	
	public ManetCost getCost() {
		return this.cost;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null) return false;
	    if (getClass() != o.getClass()) return false;   
	    ManetEdge node = (ManetEdge) o;  
	    return Objects.equals(this.ID, node.ID);
	}
	
	@Override
	public String toString() {
	    return String.format("%d: [%d]- %.2f -[%d]", this.ID, this.vertexID1, this.getDistance(), this.vertexID2);
	}

	@Override
	public double getDistance() {
		return this.cost.getDistance();
	}

	@Override
	public double getOccupation() {
		return this.cost.getOccupation().size();
	}

}
