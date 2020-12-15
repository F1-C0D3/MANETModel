package de.manetmodel.graph.viz;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import de.manetmodel.graph.Coordinate;

public class VisualVertex {

    private final Coordinate position;
    private Color backgroundColor;
    private Color borderColor;
    private String text;
    private List<VisualPath> visualPaths;

    public VisualVertex(Coordinate position, Color backgroundColor, Color borderColor, String text) {
	this.position = position;
	this.backgroundColor = backgroundColor;
	this.borderColor = borderColor;
	this.text = text;
	this.visualPaths = new ArrayList<VisualPath>();
    }

    public Coordinate getPosition() {
	return this.position;
    }

    public Color getBackgroundColor() {
	return this.backgroundColor;
    }

    public Color getBorderColor() {
	return this.borderColor;
    }

    public String getText() {
	return this.text;
    }

    public List<VisualPath> getVisualPaths() {
	return this.visualPaths;
    }

    public void addVisualPath(VisualPath visualPath) {
	this.visualPaths.add(visualPath);
    }
}
