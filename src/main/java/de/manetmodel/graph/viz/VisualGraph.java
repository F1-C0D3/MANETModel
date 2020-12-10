package de.manetmodel.graph.viz;

import java.awt.Color;
import java.util.ArrayList;

import de.manetmodel.graph.Edge;
import de.manetmodel.graph.Path;
import de.manetmodel.graph.Vertex;
import de.manetmodel.util.Tuple;

public class VisualGraph<V extends Vertex, E extends Edge> {

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
	
	public void addPath(Path<V,E> path, Color color) {
		
		for (Tuple<E, V> edgeAndVertex : path)
		{
			if (edgeAndVertex.getFirst() != null)
				edges.get(edgeAndVertex.getFirst().getID()).setColor(color);
			
			vertices.get(edgeAndVertex.getSecond().getID()).setBackgroundColor(color);
		}	
	}
}
