package de.manetgraph;

import java.util.Objects;

public class ManetVertex implements IManetVertex {

	int ID;
	Coordinate coordinate;
	
	public ManetVertex(double x, double y) {
		this.coordinate = new Coordinate(x,y);
	}	
	
	public double x() {
		return this.coordinate.x();
	}
	
	public double y() {
		return this.coordinate.y();
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
	    return String.format("%d:(%.2f/%.2f)", this.ID, this.coordinate.x(), this.coordinate.y());
	}

	@Override
	public Coordinate getPostion() {
		return this.coordinate;
	}

	@Override
	public void setPosition(double x, double y) {
		this.coordinate = new Coordinate(x,y);		
	}
	
}
