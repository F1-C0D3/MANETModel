package de.manetgraph.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import de.manetgraph.IManetEdge;
import de.manetgraph.IManetGraph;
import de.manetgraph.IManetVertex;
import de.manetgraph.ManetEdge;
import de.manetgraph.ManetGraph;
import de.manetgraph.ManetVertex;
import de.manetgraph.generator.Playground.Coordinate;
import de.manetgraph.generator.Playground.DoubleRange;
import de.manetgraph.generator.Playground.IntRange;
import de.manetgraph.util.Tuple;

public class ManetGraphGenerator {
		
	public static void getASimpleGraph(IManetGraph graph) {
				
		List<IManetVertex> vertices = new ArrayList<>();	
		IManetVertex source = graph.createVertex();
		source.setPosition(0,0);
		vertices.add(source);
				
		IManetVertex a = graph.createVertex();
		source.setPosition(7,4);
		vertices.add(a);
		
		IManetVertex b = graph.createVertex();
		source.setPosition(2,7);
		vertices.add(a);
		
		IManetVertex c = graph.createVertex();
		source.setPosition(3,12);
		vertices.add(c);
		
		IManetVertex target = graph.createVertex();
		source.setPosition(7,4);
		vertices.add(target);		
		
		graph.addVertices(vertices);
		
		List<Tuple<IManetVertex, IManetVertex>> tuples = new ArrayList<>();	
		tuples.add(new Tuple<IManetVertex, IManetVertex>(source, a));
		tuples.add(new Tuple<IManetVertex, IManetVertex>(source, b));
		tuples.add(new Tuple<IManetVertex, IManetVertex>(a, b));
		tuples.add(new Tuple<IManetVertex, IManetVertex>(b, c));
		tuples.add(new Tuple<IManetVertex, IManetVertex>(a, target));
		tuples.add(new Tuple<IManetVertex, IManetVertex>(b, target));
		graph.addEdges(tuples);			
	}
	
	
	public static ManetGraph<ManetVertex,ManetEdge> GenerateRandomGraph() {
		
		ManetGraph<ManetVertex,ManetEdge> graph = new ManetGraph<ManetVertex,ManetEdge>(ManetEdge.class);
		
		Playground pg = new Playground();		
		pg.height = new IntRange(0, 1000);
		pg.width = new IntRange(0, 1000);
		pg.edgeCount = new IntRange(1, 2);
		pg.vertexCount = new IntRange(50, 100);
		pg.vertexDistance = new DoubleRange(50d, 100d);	
		pg.edgeDistance = new DoubleRange(50d, 75d);
				
		ManetVertex currentVertex = new ManetVertex(0,0);
		graph.addVertex(currentVertex);
		
		while(graph.vertexSet().size() < pg.vertexCount.max) {
								
			Coordinate coordinate = generateRandomCoordinate(graph, currentVertex, pg, 100);
								
			if(coordinate != null)  {
				
				ManetVertex newVertex = new ManetVertex(coordinate.x, coordinate.y);
				graph.addVertex(newVertex);
	
				graph.addEdge(currentVertex, newVertex);
				
				currentVertex = newVertex;			
								
				int edgeCount = getRandom(pg.edgeCount.min, pg.edgeCount.max);
				
				List<ManetVertex> verticesInRadius = graph.getVerticesInRadius(currentVertex, pg.edgeDistance.max);
				
				if(verticesInRadius.size() < edgeCount) {
					for(ManetVertex vertex : verticesInRadius) 
						if(!graph.containsEdge(currentVertex, vertex))
							graph.addEdge(currentVertex, vertex);				
				}
				else {
					List<ManetVertex> randomVertices = selectRandomVertices(verticesInRadius, edgeCount);
					
					for(ManetVertex vertex : randomVertices) 
						if(!graph.containsEdge(currentVertex, vertex))
							graph.addEdge(currentVertex, vertex);
				}
			}
			else break;
		}	
				
		return graph;		
	}
	
	private static Coordinate generateRandomCoordinate(ManetGraph graph, ManetVertex source, Playground pg, int attemps) {
			
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
				if(graph.getVerticesInRadius(new ManetVertex(coordinate.x, coordinate.y), pg.vertexDistance.min).isEmpty()) 
					return coordinate;
			
			i++;	
		}
							
		return null;
	}
	
	private static List<ManetVertex> selectRandomVertices(List<ManetVertex> environment, int count){
		
		List<ManetVertex> vertices = new ArrayList<>(); 
				
		for(int i=0; i<count; i++) {		
			Random random = new Random();
			vertices.add(environment.get(random.nextInt(environment.size())));
		}
		
		return vertices;		
	}
	
	private static int getRandom(int min, int max){
		Random random = new Random();
		return random.nextInt(max-min) + min;
    }
	
	private static double getRandom(double min, double max){	
		Random random = new Random();	
		return min + (max - min) * random.nextDouble();
    }	
}
