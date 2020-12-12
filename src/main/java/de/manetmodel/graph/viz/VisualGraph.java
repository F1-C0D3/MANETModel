package de.manetmodel.graph.viz;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.manetmodel.graph.Edge;
import de.manetmodel.graph.Path;
import de.manetmodel.graph.Vertex;
import de.manetmodel.graph.WeightedUndirectedGraph;
import de.manetmodel.util.Tuple;

public class VisualGraph<V extends Vertex, E extends Edge> {

    private ArrayList<VisualVertex> vertices;
    private ArrayList<VisualEdge> edges;

    private Color backgroundColor = Color.WHITE;
    private Color vertexBackgroundColor = Color.LIGHT_GRAY;
    private Color vertexBorderColor = Color.BLACK;
    private Color edgeColor = Color.BLACK;

    VisualEdgeText<E> edgeText = new VisualEdgeDistanceText<E>();

    public VisualGraph(WeightedUndirectedGraph<V, E> graph) {
	this.vertices = new ArrayList<VisualVertex>();
	this.edges = new ArrayList<VisualEdge>();

	for (V vertex : graph.getVertices())
	    this.vertices.add(new VisualVertex(vertex.getPostion(), vertexBackgroundColor, vertexBorderColor, Integer.toString(vertex.getID())));

	for (E edge : graph.getEdges()) {
	    Tuple<V,V> vertices = graph.getVerticesOf(edge);
	    this.edges.add(new VisualEdge(vertices.getFirst().getPostion(), vertices.getSecond().getPostion(), edgeColor, edgeText.get(edge)));
	}
    }
    
    public List<VisualVertex> getVertices() {
	return this.vertices;
    }
    
    public List<VisualEdge> getEdges() {
	return this.edges;
    }
    
    public Color getBackgroundColor() {
	return this.backgroundColor;
    }

    public void addPath(Path<V, E> path) {
	Random random = new Random();
	Color visualPathColor = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());	
	this.addPath(path, visualPathColor);
    }
    
    public void addPath(Path<V, E> path, Color color) {

	VisualPath visualPath = new VisualPath(color);
		
	for (Tuple<E, V> edgeAndVertex : path) {

	    E edge = edgeAndVertex.getFirst();
	    V vertex = edgeAndVertex.getSecond();

	    if (edge != null)
		edges.get(edge.getID()).addVisualPath(visualPath);

	    if (vertex != null)
		vertices.get(vertex.getID()).addVisualPath(visualPath);
	}
    }
}
