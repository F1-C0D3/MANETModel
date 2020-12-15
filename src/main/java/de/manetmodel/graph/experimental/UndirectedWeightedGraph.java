package de.manetmodel.graph.experimental;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import de.manetmodel.util.Tuple;

public class UndirectedWeightedGraph<V extends Vertex<P>, E extends WeightedEdge<W>, P, W>
{
	int vertexCount;
	int edgeCount;
	ArrayList<V> vertices;
	ArrayList<E> edges;
	private final Supplier<V> vertexSupplier;
	private final Supplier<E> edgeSupplier;
	private ArrayList<ArrayList<Tuple<Integer,Integer>>> vertexAdjacencies;
	private ArrayList<Tuple<Integer,Integer>> edgeAdjacencies;
		
	public UndirectedWeightedGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier) {
		this.vertexSupplier = vertexSupplier;
		this.edgeSupplier = edgeSupplier;
		this.vertexAdjacencies = new ArrayList<ArrayList<Tuple<Integer,Integer>>>();
		this.edgeAdjacencies = new ArrayList<Tuple<Integer,Integer>>();
	}
	
	public V addVertex(P position) {		
		V v = vertexSupplier.get();
		v.setID(vertexCount++);
		v.setPosition(position);
		vertices.add(v);	
		return v;	
	}
	
	public V getVertex(V v) {
		return this.vertices.get(v.getID());
	}
	
	public Tuple<V,V> getVerticesOf(E edge){
		Tuple<Integer,Integer> vertexIDs = this.edgeAdjacencies.get(edge.getID());
		return new Tuple <V,V>(this.vertices.get(vertexIDs.getFirst()), this.vertices.get(vertexIDs.getSecond()));
	} 
	
	public E getEdge(V source, V target) {			
		for(Tuple<Integer,Integer> adjacency : vertexAdjacencies.get(source.getID())) 
			if(adjacency.getSecond() == target.getID()) 
				return this.edges.get(adjacency.getFirst());				
		return null;
	}
	
	public boolean containsEdge(V source, V target) {
		for(Tuple<Integer,Integer> adjacency : vertexAdjacencies.get(source.getID())) 
			if(adjacency.getSecond() == target.getID()) 
				return true;				
		return false;
	}
	
	public E addEdge(V source, V target, W weight){	
		if(containsEdge(source, target)) return null;
		E edge = this.edgeSupplier.get();
		edge.setID(edgeCount++);	
		edge.setWeight(weight);
		this.edges.add(edge);	
		this.vertexAdjacencies.get(source.getID()).add(new Tuple<Integer,Integer>(edge.getID(), target.getID()));
		this.vertexAdjacencies.get(target.getID()).add(new Tuple<Integer,Integer>(edge.getID(), source.getID()));
		this.edgeAdjacencies.add(new Tuple<Integer,Integer>(source.getID(), target.getID()));
		return edge;
	}
	
	public List<E> getEdgesOf(V vertex){
		List<E> edges = new ArrayList<E>();
		for(Tuple<Integer,Integer> adjacency : vertexAdjacencies.get(vertex.getID())) 
			edges.add(this.edges.get(adjacency.getFirst()));	
		return edges;	
	}
	
	public V getTargetOf(V vertex, E edge) {
		Tuple<Integer,Integer> vertexIDs = this.edgeAdjacencies.get(edge.getID());
		if(vertex.getID() == vertexIDs.getFirst())
			return this.vertices.get(vertexIDs.getSecond());
		else if (vertex.getID() == vertexIDs.getSecond())
			return this.vertices.get(vertexIDs.getFirst());		
		return null;
	}	
}