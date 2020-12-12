package de.manetmodel.graph.generator;

import java.util.List;
import java.util.Random;

import de.manetmodel.graph.Coordinate;
import de.manetmodel.graph.Edge;
import de.manetmodel.graph.Playground;
import de.manetmodel.graph.Playground.DoubleRange;
import de.manetmodel.graph.Playground.IntRange;
import de.manetmodel.graph.Vertex;
import de.manetmodel.graph.WeightedUndirectedGraph;
import de.manetmodel.util.RandomNumbers;
import de.manetmodel.util.log.Log;

public class GraphGenerator<V extends Vertex, E extends Edge> {

    private Log log;
    private WeightedUndirectedGraph<V, E> graph;
    private Playground pg;

    public GraphGenerator(WeightedUndirectedGraph<V, E> graph) {
	this.log = new Log();
	this.graph = graph;
    }

    public int generateRandomGraph(Playground pg) {
	
	if(pg.vertexDistance.max <= pg.edgeDistance.max)
	
	this.pg = pg;
	this.log.info(String.format("Playground = %s", pg.toString()));

	int vertexCount = 0;
	int attemps = 0;

	// Add first vertex at a random position within playground
	/* currentVertex = graph.addVertex(RandomNumbers.getRandom(pg.width.min, pg.width.max),
		RandomNumbers.getRandom(pg.height.min, pg.height.max));*/
	
	V currentVertex = graph.addVertex(0,0);

	vertexCount++;

	this.log.info(
		String.format("Added Vertex %d at %s", vertexCount, graph.getFirstVertex().getPostion().toString()));

	// As long as graph's size doesn't match playground requirements (vertexCount),
	// process vertex generation process
	while (vertexCount < pg.vertexCount.min && attemps < 100) {

	    // Take a random vertex from graph
	    V randomVertex = graph.getVertex(RandomNumbers.getRandom(0, vertexCount));

	    // Search a coordinate in playground which matches vertexDistance requirements
	    Coordinate coordinate = generateRandomCoordinate(randomVertex, pg);
	    
	    if(pg.isInside(coordinate.x(), coordinate.y()) && !graph.hasVertexInRadius(coordinate, pg.vertexDistance.min)) {

		int edgeCount;
		
		// Add a new vertex to graph
		V newVertex = graph.addVertex(coordinate.x(), coordinate.y());
			
		if(pg.vertexDistance.max <= pg.edgeDistance.max) {
		    graph.addEdge(randomVertex, newVertex);
		    edgeCount = RandomNumbers.getRandom(pg.edgeCount.min, pg.edgeCount.max);
		}
		else edgeCount = RandomNumbers.getRandom(pg.edgeCount.min, pg.edgeCount.max);
		
		//currentVertex = newVertex;
		
		vertexCount++;

		this.log.info(String.format("Added Vertex %d at %s", vertexCount, newVertex.getPostion().toString()));
		
		// Generate edges
		generateEdges(newVertex, edgeCount);
		
		currentVertex = newVertex;
		
	    } 
	    else {
		currentVertex = graph.getVertex(RandomNumbers.getRandom(0, vertexCount));
	    }
	}

	return vertexCount;
    }

    private void generateEdges(V source, int edgeCount) {

	// (2) Radius to gather vertices in environment, specified by a randomly chosen
	// number in interval [pg.edgeDistance.min, pg.edgeDistance.max]
	double edgeDistance = RandomNumbers.getRandom(pg.edgeDistance.min, pg.edgeDistance.max);

	// (3) Gather all vertices in radius, specified by a randomly chosen radius in
	// interval [pg.edgeDistance.min, pg.edgeDistance.max]
	List<V> verticesInRadius = graph.getVerticesInRadius(source, edgeDistance);

	if (verticesInRadius.size() > 0) {

	    // (4) Select n (1) vertices randomly from vertex environment (3)
	    List<V> randomVertices = RandomNumbers.selectNrandomOfM(verticesInRadius, edgeCount, new Random());
    
	    // (5) Add edges
	    for (V target : randomVertices)
		// Only add edge while target node's number of edges is below requirement given
		// by pg.edgeCount.max
		if (graph.getEdgesOf(target).size() < pg.edgeCount.max)
		    graph.addEdge(source, target);
	}
    }

    public Coordinate generateRandomCoordinate(V source, Playground pg) {

	Coordinate coordinate = null;
	double angleRadians, x, y;
	
	double distance = RandomNumbers.getRandom(pg.vertexDistance.min, pg.vertexDistance.max);
	double angleDegrees = RandomNumbers.getRandom(0d, 360d);
	
	if ((angleDegrees >= 0d) && (angleDegrees < 90d)) {
	    angleRadians = Math.toRadians(angleDegrees);
	    x = distance * Math.cos(angleRadians);
	    y = distance * Math.sin(angleRadians);
	    coordinate = new Coordinate(source.x() + x, source.y() + y);
	}

	if ((angleDegrees > 90d) && (angleDegrees <= 180d)) {
	    angleRadians = Math.toRadians(180 - angleDegrees);
	    x = distance * Math.cos(angleRadians);
	    y = distance * Math.sin(angleRadians);
	    coordinate = new Coordinate(source.x() - x, source.y() + y);
	}

	if ((angleDegrees > 180d) && (angleDegrees <= 270d)) {
	    angleRadians = Math.toRadians(270 - angleDegrees);
	    x = distance * Math.sin(angleRadians);
	    y = distance * Math.cos(angleRadians);
	    coordinate = new Coordinate(source.x() - x, source.y() - y);
	}

	if ((angleDegrees > 270d) && (angleDegrees <= 360d)) {
	    angleRadians = Math.toRadians(360 - angleDegrees);
	    x = distance * Math.cos(angleRadians);
	    y = distance * Math.sin(angleRadians);
	    coordinate = new Coordinate(source.x() + x, source.y() - y);
	}

	return coordinate;
    }

    public void generateGridGraph(int width, int height, double edgeDistance, int numberOfNodes) {

	pg = new Playground();
	pg.height = new IntRange(0, height);
	pg.width = new IntRange(0, width);
	pg.edgeCount = new IntRange(4, 4);
	pg.vertexCount = new IntRange(numberOfNodes, numberOfNodes);
	pg.edgeDistance = new DoubleRange(edgeDistance, edgeDistance);

	V currentVertex = graph.addVertex(0, 0);
	int vertexCount = 1;

	while ((currentVertex.x() <= pg.width.max - pg.edgeDistance.max) && vertexCount < pg.vertexCount.max) {
	    if (vertexCount > 1) {
		double xOffset = RandomNumbers.getRandom(pg.edgeDistance.min, pg.edgeDistance.max);
		V newVertex = graph.addVertex(currentVertex.x() + xOffset, 0);
		currentVertex = newVertex;
		vertexCount++;
		this.generateEdges(newVertex, 4);
	    }

	    while (currentVertex.y() <= (pg.height.max - pg.edgeDistance.max) && vertexCount < pg.vertexCount.max) {
		double yOffset = RandomNumbers.getRandom(pg.edgeDistance.min, pg.edgeDistance.max);
		V newVertex = graph.addVertex(currentVertex.x(), currentVertex.y() + yOffset);
		currentVertex = newVertex;
		vertexCount++;
		this.generateEdges(newVertex, 4);
	    }
	}
    }

}