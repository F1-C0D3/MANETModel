package de.manetmodel.graph.viz;

import java.awt.Color;

import de.manetmodel.graph.Coordinate;

public class VisualEdge {
	
	private int ID;
	private int sourceID;
	private int targetID;
	private Color color;
	private String text;
	
	public VisualEdge(int ID, int sourceID, int targetID, String text, Color color) {
		this.ID = ID;
		this.sourceID = sourceID;
		this.targetID = targetID;
		this.text = text;
		this.color = color;
	}
	
	public int getID() {
		return this.ID;
	}
	
	public int getSource() {
		return this.sourceID;
	}
	
	public int getTarget() {
		return this.targetID;
	}
	
	public String getText() {
		return this.text;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return this.color;
	}
}
