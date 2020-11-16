package de.manetmodel.visualization;

import java.awt.Color;

import de.manetmodel.graph.Coordinate;

public class VisualEdge {
	
	int ID;
	Coordinate source;
	Coordinate target;
	Color color;
	String text;
	
	public VisualEdge(int ID, Coordinate source, Coordinate target, String text) {
		this.ID = ID;
		this.source = source;
		this.target = target;
		this.text = text;
	}
	
	public int getID() {
		return this.ID;
	}
	
	public Coordinate getSource() {
		return this.source;
	}
	
	public Coordinate getTarget() {
		return this.target;
	}
	
	public String getText() {
		return this.text;
	}
}
