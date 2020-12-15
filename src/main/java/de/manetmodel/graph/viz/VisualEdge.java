package de.manetmodel.graph.viz;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import de.manetmodel.graph.Coordinate;

public class VisualEdge {
        
    private final Coordinate startPosition;
    private final Coordinate targetPosition;   
    private Color color;
    private String text;  
    private List<VisualPath> visualPaths;
    
    public VisualEdge(Coordinate startPosition, Coordinate targetPosition, Color color, String text){
	this.startPosition = startPosition;
	this.targetPosition = targetPosition;
	this.color = color;
	this.text = text;
	this.visualPaths = new ArrayList<VisualPath>();
    }
    
    public Coordinate getStartPosition() {
	return this.startPosition;
    }
    
    public Coordinate getTargetPosition() {
	return this.targetPosition;
    }
    
    public Color getColor() {
	return this.color;
    }
    
    public String getText() {
	return this.text;
    }
    
    public List<VisualPath> getVisualPaths(){
	return this.visualPaths;
    }
    
    public void addVisualPath(VisualPath visualPath) {
	this.visualPaths.add(visualPath);
    }
    
    
}
