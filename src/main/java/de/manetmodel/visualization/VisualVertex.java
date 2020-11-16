package de.manetmodel.visualization;

import java.awt.Color;

import de.manetmodel.graph.Coordinate;

public class VisualVertex {

	int ID;
	Coordinate coordinate;
	Color backgroundColor;
	Color borderColor;
	
	public VisualVertex(int ID, Coordinate coordinate) {
		this.ID = ID;
		this.coordinate = coordinate;
	}
	
	public int getID() {
		return this.ID;
	}

	public double x() {
		return this.coordinate.x();
	}
	
	public double y() {
		return this.coordinate.y();
	}
}
