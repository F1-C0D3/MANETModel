 package de.manetgraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import de.manetgraph.Playground.DoubleRange;
import de.manetgraph.Playground.IntRange;
import de.manetgraph.util.Triple;
import de.manetgraph.util.Tuple;

public class ManetGraph<V extends ManetVertex, E extends ManetEdge>{

	private final Supplier<V> vertexSupplier;
	private final Supplier<E> edgeSupplier;
	
	private int vertexCount;
	private int edgeCount;
	
	private ArrayList<V> vertices;
	private ArrayList<E> edges;
	
	ArrayList<ArrayList<Tuple<Integer,Integer>>> vertexAdjacencies;
	ArrayList<Tuple<Integer,Integer>> edgeAdjacencies;
	
	private ArrayList<ManetPath> paths;
	
	public ManetGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier) {
		this.vertexSupplier = vertexSupplier;
		this.edgeSupplier = edgeSupplier;	
		this.vertexCount = 0;
		this.edgeCount = 0;
		this.vertices = new ArrayList<V>();
		this.edges = new ArrayList<E>();
		this.vertexAdjacencies = new ArrayList<ArrayList<Tuple<Integer,Integer>>>();
		this.edgeAdjacencies = new ArrayList<Tuple<Integer,Integer>>();
	}

	public V addVertex(double x, double y) {
		V vertex = this.vertexSupplier.get();
		vertex.setID(vertexCount++);
		vertex.setPosition(x, y);
		this.vertices.add(vertex);
		this.vertexAdjacencies.add(new ArrayList<Tuple<Integer,Integer>>());
		return vertex;
	}
	
	public boolean addVertex(V vertex) {
		if(vertex.getPostion() == null) return false;
		vertex.setID(vertexCount++);
		this.vertices.add(vertex);
		return true;	
	}
	
	public boolean containsEdge(V source, V target) {
		for(Tuple<Integer,Integer> adjacency : vertexAdjacencies.get(source.getID())) 
			if(adjacency.getSecond() == target.getID()) 
				return true;				
		return false;
	}
	
	public E addEdge(V source, V target){	
		if(containsEdge(source, target)) return null;
		E edge = this.edgeSupplier.get();
		edge.setID(edgeCount++);	
		edge.getCost().setDistance(this.getDistance(source, target));	
		edge.setVertexIDs(source.getID(), target.getID());
		this.edges.add(edge);	
		this.vertexAdjacencies.get(source.getID()).add(new Tuple<Integer,Integer>(edge.getID(), target.getID()));
		this.vertexAdjacencies.get(target.getID()).add(new Tuple<Integer,Integer>(edge.getID(), source.getID()));
		this.edgeAdjacencies.add(new Tuple<Integer,Integer>(source.getID(), target.getID()));
		return edge;
	}
		
	public V getVertex(int ID) {		
		return this.vertices.get(ID);
	}
	
	public E getEdge(V source, V target) {			
		for(Tuple<Integer,Integer> adjacency : vertexAdjacencies.get(source.getID())) 
			if(adjacency.getSecond() == target.getID()) 
				return this.edges.get(adjacency.getFirst());				
		return null;
	}
	
	public Tuple<V,V> getVerticesOf(E edge){
		Tuple<Integer,Integer> vertexIDs = this.edgeAdjacencies.get(edge.getID());
		return new Tuple <V,V>(this.vertices.get(vertexIDs.getFirst()),this.vertices.get(vertexIDs.getSecond()));
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
	
	public List<V> getVertices() {
		return this.vertices;
	}
	
	public List<E> getEdges() {
		return this.edges;
	}
	
	public double getDistance(V source, V target) {
		return Math.sqrt(Math.pow(source.x() - target.x(), 2) + Math.pow(source.y() - target.y(), 2));
	}
	
	public double getDistance(Coordinate source, Coordinate target) {
		return Math.sqrt(Math.pow(source.x() - target.x(), 2) + Math.pow(source.y() - target.y(), 2));
	}
	
	public List<V> getVerticesInRadius(Coordinate coordinate, double radius) {	
		List<V> vertices = new ArrayList<>();	
		for(V vertex : this.vertices) 
			if(this.getDistance(coordinate, vertex.getPostion()) <= radius)
				vertices.add(vertex);					
		return vertices;
	}
	
	public List<V> getVerticesInRadius(V source, double radius) {	
		List<V> vertices = new ArrayList<>();		
		for(V vertex : this.vertices) 
			if(!vertex.equals(source) && this.getDistance(source.getPostion(), vertex.getPostion()) <= radius)
				vertices.add(vertex);					
		return vertices;
	}
	
	public V getFirstVertex() {
		return this.vertices.get(0);
	}
	
	public V getLastVertex() {
		return this.vertices.get(vertexCount-1);	
	}
	
	public List<V> getNextHopsOf(V vertex) {	
		List<V> nextHops = new ArrayList<V>();
		for(Tuple<Integer,Integer> adjacency : vertexAdjacencies.get(vertex.getID())) 
			nextHops.add(this.vertices.get(adjacency.getSecond()));
		return nextHops;
	}
					
	@Override 
	public String toString() {	
		String str = "Not implemented yet";
		return str;
	}
	
	public void generateSimpleGraph() {
		Generator generator = new Generator();	
		generator.generateSimpleGraph();
	}
	
	public int generateRandomGraph() {	
		Generator generator = new Generator();	
		return generator.generateRandomGraph();		
	}
	
	public int generateGridGraph() {
		Generator generator = new Generator();	
		return generator.generateGridGraph();		
	}
		
	public class IO {
		
		public void exportGraph() {
			
		}
		
		public void importGraph() {
			
		}	
	}

	public class Generator {
		
		public void generateSimpleGraph() {
			
			V source = addVertex(0, 0);
			V a =  addVertex(7, 4);
			V b = addVertex(2, 7);
			V c = addVertex(3, 12);
			V target = addVertex(10, 10);
					
			addEdge(source, a);
			addEdge(source, b);
			addEdge(a, b);
			addEdge(b, c);
			addEdge(a, target);
			addEdge(b, target);
			
			generateOccupation();
		}
		
		public int generateGridGraph() {
			
			Playground pg = new Playground();		
			pg.height = new IntRange(0, 1000);
			pg.width = new IntRange(0, 1000);
			pg.edgeCount = new IntRange(4, 4);
			pg.vertexCount = new IntRange(50, 100);
			pg.vertexDistance = new DoubleRange(75d, 75d);	
			pg.edgeDistance = new DoubleRange(100d, 100d);
			
			V currentVertex = addVertex(0, 0);
			int vertexCount = 1;
			
			while((currentVertex.x() <= pg.width.max-pg.vertexDistance.max) && vertexCount < pg.vertexCount.max) {
					
				if(vertexCount > 1) {
					double xOffset = getRandom(pg.vertexDistance.min, pg.vertexDistance.max);
					V newVertex = addVertex(currentVertex.x() + xOffset, 0);
					currentVertex = newVertex;	
					vertexCount++;
					generateEdges(newVertex, pg);
				}
				
				while(currentVertex.y() <= (pg.height.max - pg.vertexDistance.max)){					
					double yOffset = getRandom(pg.vertexDistance.min, pg.vertexDistance.max);
					V newVertex = addVertex(currentVertex.x(), currentVertex.y() + yOffset);
					currentVertex = newVertex;							
					vertexCount++;
					generateEdges(newVertex, pg);
				}				
			}
			
			generateOccupation();
			
			return vertexCount;
		}
		
		private void generateEdges(V source, Playground pg) {
			
			int edgeCount = getRandom(pg.edgeCount.min, pg.edgeCount.max);
			double edgeDistance = getRandom(pg.edgeDistance.min, pg.edgeDistance.max);
			
			List<V> verticesInRadius = getVerticesInRadius(source, edgeDistance);
			
			if(verticesInRadius.size() < edgeCount) {
				for(V vertex : verticesInRadius) 
					addEdge(source, vertex);				
			}
			else {
				List<V> randomVertices = getRandomNofM(edgeCount, verticesInRadius);
				for(V vertex : randomVertices) 
					addEdge(source, vertex);
			}		
		}
		
		public void generateOccupation() {
			
			for(E edge : edges) {
			
				Tuple<V,V> vertices = getVerticesOf(edge);
				List<V> nextHops = getNextHopsOf(vertices.getFirst());
			
				for(V v : nextHops) 
					if(!v.equals(vertices.getSecond())) 
						for(E e : getEdgesOf(v))
							edge.getCost().addOccupation(e.getID());

				nextHops = getNextHopsOf(vertices.getSecond());
						
				for(V v : nextHops) 
					if(!v.equals(vertices.getSecond())) 
						for(E e : getEdgesOf(v))
							edge.getCost().addOccupation(e.getID());
			}
		}
		
		public int generateRandomGraph() {
						
			Playground pg = new Playground();		
			pg.height = new IntRange(0, 10000);
			pg.width = new IntRange(0, 10000);
			pg.edgeCount = new IntRange(2, 4);
			pg.vertexCount = new IntRange(50, 100);
			pg.vertexDistance = new DoubleRange(50d, 100d);	
			pg.edgeDistance = new DoubleRange(50d, 75d);
					
			V currentVertex = addVertex(0, 0);
			int vertexCount = 1;
			
			while(getVertices().size() < pg.vertexCount.max) {
									
				Coordinate coordinate = generateRandomCoordinate(currentVertex, pg, 100);
									
				if(coordinate != null)  {
								
					V newVertex = addVertex(coordinate.x(), coordinate.y());
					addEdge(currentVertex, newVertex);		
					currentVertex = newVertex;	
					vertexCount++;
					int edgeCount = getRandom(pg.edgeCount.min, pg.edgeCount.max);
					
					List<V> verticesInRadius = getVerticesInRadius(currentVertex, pg.edgeDistance.max);
					
					if(verticesInRadius.size() < edgeCount) {
						
						for(V vertex : verticesInRadius) 
							addEdge(currentVertex, vertex);				
					}
					else {
						List<V> randomVertices = getRandomNofM(edgeCount, verticesInRadius);
						
						for(V vertex : randomVertices) 
							addEdge(currentVertex, vertex);
					}
				}
				else break;
			}
			
			generateOccupation();
			return vertexCount;
		}
		
		private Coordinate generateRandomCoordinate(V source, Playground pg, int attemps) {
				
			Coordinate coordinate = null;	
						
			int i = 1;
			
			while(i <= attemps) {
							
				double distance = getRandom(pg.vertexDistance.min, pg.vertexDistance.max);				
				double angleDegrees = getRandom(0d, 360d);				
				double angleRadians, x, y;
				
				if((angleDegrees >= 0d) && (angleDegrees < 90d)) {
					angleRadians = Math.toRadians(angleDegrees);
					x = distance * Math.cos(angleRadians);
					y = distance * Math.sin(angleRadians);
					coordinate = new Coordinate(source.x() + x, source.y() + y);
				}
				
				if((angleDegrees > 90d) && (angleDegrees <= 180d)) {
					angleRadians = Math.toRadians(180-angleDegrees);
					x = distance * Math.cos(angleRadians);
					y = distance * Math.sin(angleRadians);
					coordinate = new Coordinate(source.x() - x, source.y() + y);		
				}
				
				if((angleDegrees > 180d) && (angleDegrees <= 270d)) {
					angleRadians = Math.toRadians(270-angleDegrees);
					x = distance * Math.sin(angleRadians);
					y = distance * Math.cos(angleRadians);
					coordinate = new Coordinate(source.x() - x, source.y() - y);
				}
								
				if((angleDegrees > 270d) && (angleDegrees <= 360d)) {
					angleRadians = Math.toRadians(360-angleDegrees);
					x = distance * Math.cos(angleRadians);
					y = distance * Math.sin(angleRadians);
					coordinate = new Coordinate(source.x() + x, source.y() - y);	
				}
							
				if(pg.isInside(coordinate)) 						
					if(getVerticesInRadius(coordinate, pg.vertexDistance.min).isEmpty()) 
						return coordinate;
					
				i++;	
			}
								
			return null;
		}
		
		private List<V> getRandomNofM(int n, List<V> m){
			
			List<V> vertices = new ArrayList<>(); 
					
			for(int i=0; i<n; i++) {		
				Random random = new Random();
				vertices.add(m.get(random.nextInt(m.size())));
			}
			
			return vertices;		
		}
		
		private int getRandom(int min, int max){
			if(min==max) return min;
			Random random = new Random();
			return random.nextInt(max-min) + min;
	    }
		
		private double getRandom(double min, double max){	
			if(min==max) return min;
			Random random = new Random();	
			return min + (max - min) * random.nextDouble();
	    }		
	}
}
