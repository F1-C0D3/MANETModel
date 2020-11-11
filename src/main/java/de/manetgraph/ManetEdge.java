package de.manetgraph;

import java.util.Objects;

import org.jgrapht.graph.DefaultWeightedEdge;

public class ManetEdge extends DefaultWeightedEdge implements IManetEdge, Comparable<ManetEdge> {

	int ID;
	
	public void setID(int ID) {
		this.ID = ID;
	}
	
	public int getID() {
		return this.ID;
	}
	
	@Override
	public double getWeight() {
		return super.getWeight();
	}

	public int compareTo(ManetEdge edge) {	
		return Double.compare(edge.getWeight(), this.getWeight());
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
	    return String.format("%d:[%s]- %.2f -[%s]", this.ID, this.getSource().toString(), this.getWeight(), this.getTarget().toString());
	}
	
}
