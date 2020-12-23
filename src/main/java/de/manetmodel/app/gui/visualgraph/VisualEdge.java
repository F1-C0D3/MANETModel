package de.manetmodel.app.gui.visualgraph;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import de.manetmodel.graph.Position2D;

public class VisualEdge {
        
    private final Position2D startPosition;
    private final Position2D targetPosition;   
    private Color color;
    private String text;  
    private List<VisualPath> visualPaths;
    
    public VisualEdge(Position2D startPosition, Position2D targetPosition, Color color, String text){
	this.startPosition = startPosition;
	this.targetPosition = targetPosition;
	this.color = color;
	this.text = text;
	this.visualPaths = new ArrayList<VisualPath>();
    }
    
    public Position2D getStartPosition() {
	return this.startPosition;
    }
    
    public Position2D getTargetPosition() {
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
