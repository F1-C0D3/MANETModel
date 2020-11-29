package de.manetmodel.visualization;

import java.awt.Color;

import de.manetmodel.graph.Coordinate;

public class VisualVertex {

	private int ID;
	private Coordinate coordinate;
	private Color backgroundColor;
	private Color borderColor;
	
	public VisualVertex(int ID, Coordinate coordinate, Color backgroundColor, Color borderColor) {
		this.ID = ID;
		this.coordinate = coordinate;
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
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
	
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public Color getBackgroundColor() {
		return this.backgroundColor;
	}
	
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}
	
	public Color getBorderColor() {
		return this.borderColor;
	}
}
