package de.manetmodel.visualization;

import java.awt.Color;
import java.util.ArrayList;

public class VisualGraph {

	Color playgroundColor;
	Color vertexColor;
	Color edgeColor;
	
	ArrayList<VisualVertex> vertices;
	ArrayList<VisualEdge> edges;

	public VisualGraph(Color playgroundColor, Color vertexColor, Color edgeColor) {
		this.playgroundColor = playgroundColor;
		this.vertexColor = vertexColor;
		this.edgeColor = edgeColor;
		this.vertices = new ArrayList<VisualVertex>();
		this.edges = new ArrayList<VisualEdge>();
	}
	
	public void addVertex(VisualVertex vertex) {
		this.vertices.add(vertex);
	}
	
	public void addEdge(VisualEdge edge) {
		this.edges.add(edge);
	}
	
	public ArrayList<VisualVertex> getVertices() {
		return this.vertices;
	}
	
	public ArrayList<VisualEdge> getEdges() {
		return this.edges;
	}	
}
