package de.manetmodel.graph;

import java.util.Objects;
import java.util.Set;

public class ManetVertex implements IManetVertex, IManetElement{

	private int ID;
	private Coordinate coordinate;
	
	public ManetVertex() {}	
	
	public ManetVertex(double x, double y) {
		this.coordinate = new Coordinate(x,y);
	}
	
	public int getID() {
		return this.ID;
	}
	
	public void setID(int ID) {
		this.ID = ID;
	}
	
	public double x() {
		return this.coordinate.x();
	}
	
	public double y() {
		return this.coordinate.y();
	}
	
	public Coordinate getPostion() {
		return this.coordinate;
	}

	public void setPosition(double x, double y) {
		this.coordinate = new Coordinate(x,y);		
	}
	
	public double getDistance() {
		return 0;
	}
	
	public Set<Integer> getOccupation() {
		return null;
	}

	@Override
	public boolean equals(Object o) {
	    if (this == o)  return true;
	    if (o == null) return false;
	    if (getClass() != o.getClass()) return false;    
	    ManetVertex node = (ManetVertex) o;
	    return Objects.equals(this.ID, node.ID);
	}
	
	@Override
	public String toString() {
	    return String.format("%d:(%.2f/%.2f)", this.ID, this.coordinate.x(), this.coordinate.y());
	}	
}
