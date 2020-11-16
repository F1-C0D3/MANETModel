package de.manetmodel.graph;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import de.manetmodel.util.Tuple;

public class ManetEdge implements IManetElement, IManetEdge, Comparable<ManetEdge> {

	int ID;
	int vertexID1;
	int vertexID2;
	double distance;
	Set<Integer> occupation;
	
	public void setID(int ID) {
		this.ID = ID;
		this.occupation = new HashSet<Integer>();
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
	
	@Override
	public double getDistance() {
		return this.distance;
	}
	
	public double setDistance(double distance) {
		return this.distance = distance;
	}

	@Override
	public Set<Integer> getOccupation() {
		return this.occupation;
	}
	
	public void setOccupation(Set<Integer> occupation) {
		this.occupation = occupation;
	}
	
	public void addOccupation(int edgeID) {	
		this.occupation.add(edgeID);
	}
	
	public int compareTo(ManetEdge edge) {	
		return Double.compare(edge.getDistance(), this.getDistance());
	}
	
	public int compareDistanceTo(ManetEdge edge) {
		return Double.compare(edge.getDistance(), this.getDistance());
	}
	
	public int compareOccupationTo(ManetEdge edge) {
		return Integer.compare(edge.getOccupation().size(), this.occupation.size());
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
}
