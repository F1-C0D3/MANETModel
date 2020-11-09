package de.manetgraph;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.jgrapht.graph.SimpleWeightedGraph;
import de.manetgraph.util.Tuple;

public class ManetGraph<V extends IManetVertex, E extends IManetEdge> extends SimpleWeightedGraph<V,E> implements IManetGraph<V,E> {

	int vertexCount;
	int edgeCount;
	
	public ManetGraph(Class<? extends V> vertexClass, Class<? extends E> edgeClass) {
		super(edgeClass);
		this.vertexCount = 1;
		this.edgeCount = 1;
		
	}
	
	@Override
	public boolean addVertex(V vertex) {
		vertex.setID(vertexCount++);
		return super.addVertex(vertex);		
	}
	
	@Override
	public void addVertices(List<V> vertices) {
		for(V vertex : vertices) 
			this.addVertex(vertex);
	}
	
	@Override
	public E addEdge(V source, V target){
		
		double cost = this.getDistance(source, target);					
		
		E edge = super.addEdge(source, target);	
		
		this.setEdgeWeight(edge, cost);	
		
		edge.setID(edgeCount++);
		
		return edge;
	}
	
	public void addEdges(List<Tuple<V,V>> vertices) {
		for(Tuple<V, V> pair : vertices) 
			this.addEdge(pair.getFirst(), pair.getSecond());		
	}
	
	public V getVertex(int ID) {		
		for(V vertex : this.vertexSet())
			if(vertex.getID() == ID) 
				return vertex;
		
		return null;
	}
	
	public List<V> getVerticesInRadius(V source, double radius) {
		
		List<V> vertices = new ArrayList<>();
		
		for(V vertex : this.vertexSet()) 
			if(!source.equals(vertex) && this.getDistance(source, vertex) <= radius)
				vertices.add(vertex);		
		
		return vertices;
	}
			
	public double getDistance(V source, V target) {
		return Math.sqrt(Math.pow(target.x()-source.x(), 2) + Math.pow(target.y()-source.y(), 2));
	}
				
	@Override 
	public String toString() {	
		String str = "Not implemented yet";
		return str;
	}

	@Override
	public V createVertex() {
		return this.getVertexSupplier().get();
	}
}
