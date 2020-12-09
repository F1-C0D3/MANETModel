package de.manetmodel.graph;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import de.manetmodel.util.Tuple;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ManetEdge implements IManetEdge, Comparable<ManetEdge> {

	@XmlElement
	int ID;
	@XmlElement
	int vertexID1;
	@XmlElement
	int vertexID2;
	@XmlElement
	double distance;
	@XmlElement
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
	
	public double getDistance() {
		return this.distance;
	}
	
	public double setDistance(double distance) {
		return this.distance = distance;
	}

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
		return new StringBuffer("ID: ").append(this.ID)
				.append(", vertexID: ").append(this.vertexID1)
				.append(", vertexID: ").append(this.vertexID2)
                .append(", distance: ").append(this.distance).toString();
	}
}
