package de.manetgraph;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.jgrapht.graph.SimpleWeightedGraph;

import de.manetgraph.util.Tuple;

//public class ManetGraph<V extends ManetVertex, E extends ManetEdge> extends SimpleWeightedGraph<V, E>
public class ManetGraph extends SimpleWeightedGraph<ManetVertex, ManetEdge>{
	
	int vertexCount;
	int edgeCount;
		
	public ManetGraph() {
		super(ManetEdge.class);			
		this.vertexCount = 1;
		this.edgeCount = 1;
	}
	
	public class ManetEdgeSupplier implements Supplier<ManetEdge> {
		public ManetEdge get() {
			return new ManetEdge();
		}	
	}
	
	@Override
	public boolean addVertex(ManetVertex vertex) {
		vertex.setID(vertexCount++);
		return super.addVertex(vertex);		
	}
	
	public void addVertices(List<ManetVertex> vertexList) {
		for(ManetVertex vertex : vertexList) 
			this.addVertex(vertex);
	}
	
	@Override
	public ManetEdge addEdge(ManetVertex source, ManetVertex target){
		
		double cost = this.getDistance(source, target);					
		
		ManetEdge edge = super.addEdge(source, target);	
		
		this.setEdgeWeight(edge, cost);	
		
		edge.setID(edgeCount++);
		
		return edge;
	}
	
	public void addEdges(List<Tuple<ManetVertex,ManetVertex>> vertices) {
		for(Tuple<ManetVertex, ManetVertex> pair : vertices) 
			this.addEdge(pair.getFirst(), pair.getSecond());		
	}
	
	public ManetVertex getVertex(int ID) {		
		for(ManetVertex vertex : this.vertexSet())
			if(vertex.getID() == ID) 
				return vertex;
		
		return null;
	}
	
	public List<ManetVertex> getVerticesInRadius(ManetVertex source, double radius) {
		
		List<ManetVertex> vertices = new ArrayList<>();
		
		for(ManetVertex vertex : this.vertexSet()) 
			if(!source.equals(vertex) && this.getDistance(source, vertex) <= radius)
				vertices.add(vertex);		
		
		return vertices;
	}
			
	public double getDistance(ManetVertex source, ManetVertex target) {
		return Math.sqrt(Math.pow(target.getX()-source.getX(), 2) + Math.pow(target.getY()-source.getY(), 2));
	}
				
	@Override 
	public String toString() {	
		String str = "Not implemented yet";
		return str;
	}
}
