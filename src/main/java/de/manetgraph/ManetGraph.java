package de.manetgraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import de.manetgraph.Playground.DoubleRange;
import de.manetgraph.Playground.IntRange;

public class ManetGraph<V extends ManetVertex, E extends ManetEdge> extends DefaultUndirectedWeightedGraph<V,E> {

	private int vertexCount;
	private int edgeCount;
	
	public ManetGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier) {
		super(vertexSupplier, edgeSupplier);	
		this.vertexCount = 0;
		this.edgeCount = 0;
	}
			
	public V addVertex(double x, double y) {
		V vertex = super.addVertex();
		vertexCount++;
		vertex.setID(vertexCount);
		vertex.setPosition(x, y);
		return vertex;
	}
	
	@Override
	public boolean addVertex(V vertex) {
		vertexCount++;
		vertex.setID(vertexCount);
		if(vertex.getPostion() == null) return false;
		return super.addVertex(vertex);		
	}
	
	@Override
	public E addEdge(V source, V target){		
		double cost = this.getDistance(source, target);							
		E edge = super.addEdge(source, target);		
		this.setEdgeWeight(edge, cost);	
		edge.setID(edgeCount++);	
		return edge;
	}
		
	public V getVertex(int ID) {		
		for(V vertex : this.vertexSet())
			if(vertex.getID() == ID) 
				return vertex;
		
		return null;
	}
	
	public List<V> getVerticesInRadius(Coordinate coordinate, double radius) {	
		List<V> vertices = new ArrayList<>();	
		
		for(V vertex : this.vertexSet()) 
			if(this.getDistance(coordinate, vertex.getPostion()) <= radius)
				vertices.add(vertex);		
				
		return vertices;
	}
	
	public List<V> getVerticesInRadius(V source, double radius) {	
		List<V> vertices = new ArrayList<>();	
		
		for(V vertex : this.vertexSet()) 
			if(!vertex.equals(source) && this.getDistance(source.getPostion(), vertex.getPostion()) <= radius)
				vertices.add(vertex);		
				
		return vertices;
	}

	public double getDistance(V source, V target) {
		return Math.sqrt(Math.pow(source.x()- target.x(), 2) + Math.pow(source.y()- target.y(), 2));
	}
	
	public double getDistance(Coordinate source, Coordinate target) {
		return Math.sqrt(Math.pow(source.x()- target.x(), 2) + Math.pow(source.y()- target.y(), 2));
	}
					
	@Override 
	public String toString() {	
		String str = "Not implemented yet";
		return str;
	}
	
	public int generateRandomGraph() {	
		Generator generator = new Generator();	
		return generator.generateRandomGraph();		
	}
	
	public int generateGridGraph() {
		Generator generator = new Generator();	
		return generator.generateGridGraph();		
	}
	
	public boolean generateSimpleGraph() {
		Generator generator = new Generator();	
		generator.generateSimpleGraph();		
		return true;
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
			
			return vertexCount;
		}
		
		private void generateEdges(V source, Playground pg) {
			
			int edgeCount = getRandom(pg.edgeCount.min, pg.edgeCount.max);
			double edgeDistance = getRandom(pg.edgeDistance.min, pg.edgeDistance.max);
			
			List<V> verticesInRadius = getVerticesInRadius(source, edgeDistance);
			
			if(verticesInRadius.size() < edgeCount) {
				for(V vertex : verticesInRadius) 
					if(!containsEdge(source, vertex))
						addEdge(source, vertex);				
			}
			else {
				List<V> randomVertices = getRandomNofM(edgeCount, verticesInRadius);
				for(V vertex : randomVertices) 
					if(!containsEdge(source, vertex))
						addEdge(source, vertex);
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
			
			while(vertexSet().size() < pg.vertexCount.max) {
									
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
							if(!containsEdge(currentVertex, vertex))
								addEdge(currentVertex, vertex);				
					}
					else {
						List<V> randomVertices = getRandomNofM(edgeCount, verticesInRadius);
						
						for(V vertex : randomVertices) 
							if(!containsEdge(currentVertex, vertex))
								addEdge(currentVertex, vertex);
					}
				}
				else break;
			}
			
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
