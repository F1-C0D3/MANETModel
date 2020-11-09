package de.manetgraph;

import java.util.Objects;

public class ManetVertex implements IManetVertex {

	int ID;
	double x;
	double y;
	
	public ManetVertex(double x, double y) {
		this.x = x;
		this.y = y;
	}	
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public int getID() {
		return this.ID;
	}
	
	public void setID(int ID) {
		this.ID = ID;
	}
	
	public double getWeight() {
		return 0;
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
	    return String.format("%d:(%.2f/%.2f)", this.ID, this.x, this.y);
	}
	
}
