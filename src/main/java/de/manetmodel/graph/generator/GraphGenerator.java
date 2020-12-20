package de.manetmodel.graph.generator;

import java.util.List;
import java.util.Random;

import de.manetmodel.graph.Coordinate;
import de.manetmodel.graph.Edge;
import de.manetmodel.graph.Vertex;
import de.manetmodel.graph.WeightedUndirectedGraph;
import de.manetmodel.graph.generator.GraphProperties.DoubleRange;
import de.manetmodel.util.RandomNumbers;
import de.manetmodel.util.log.Log;

public class GraphGenerator<V extends Vertex, E extends Edge> {

    private Log log;
    private WeightedUndirectedGraph<V, E> graph;

    public GraphGenerator(WeightedUndirectedGraph<V, E> graph) {
	this.log = new Log();
	this.graph = graph;
    }

    public int generateNetworkGraph(NetworkGraphProperties properties) {

	int numberOfVertices = RandomNumbers.getRandom(properties.getVertexCount().min,
		properties.getVertexCount().max);
	int vertexCount = 0;
	int attemps = 0;
	V currentVertex = graph.addVertex(0, 0);

	while (vertexCount < numberOfVertices && attemps < 100) {

	    Coordinate coordinate = generateRandomCoordinate(currentVertex, properties.getVertexDistance());

	    if (properties.isInside(coordinate.x(), coordinate.y())
		    && !graph.hasVertexInRadius(coordinate, properties.getVertexDistance().min)) {

		V newVertex = graph.addVertex(coordinate.x(), coordinate.y());
		vertexCount++;
		attemps = 0;

		if (properties.getVertexDistance().min <= properties.getEdgeDistance().max)
		    graph.addEdge(currentVertex, newVertex);

		currentVertex = newVertex;

		List<V> verticesInRadius = graph.getVerticesInRadius(newVertex, properties.getEdgeDistance().max);

		for (V target : verticesInRadius)
		    graph.addEdge(newVertex, target);

	    } else
		currentVertex = graph.getVertex(RandomNumbers.getRandom(0, vertexCount));

	    attemps++;
	}
	return vertexCount;
    }

    public int generateRandomGraph(GraphProperties properties) {

	int numberOfVertices = RandomNumbers.getRandom(properties.getVertexCount().min,
		properties.getVertexCount().max);
	int vertexCount = 0;
	int attemps = 0;
	this.log.info(String.format("GraphProperties: (%s)", properties.toString()));

	if (properties.getVertexDistance().min > properties.getEdgeDistance().max)
	    this.log.warning(
		    "Given GraphProperties allows no edges because arguments have condition vertexDistance.min > edgeDistance.max");

	// Add first vertex at a random position within GraphProperties
	/*
	 * currentVertex = graph.addVertex(RandomNumbers.getRandom(properties.width.min,
	 * properties.width.max), RandomNumbers.getRandom(properties.height.min,
	 * properties.height.max));
	 */

	V currentVertex = graph.addVertex(0, 0);
	vertexCount++;
	this.log.info(
		String.format("Added Vertex %d at %s", vertexCount, graph.getFirstVertex().getPostion().toString()));

	// As long as graph's size doesn't match GraphProperties requirements
	// (vertexCount),
	// process vertex generation process
	while (vertexCount < numberOfVertices && attemps < 100) {

	    // Search a coordinate in GraphProperties which matches vertexDistance
	    // requirements
	    Coordinate coordinate = generateRandomCoordinate(currentVertex, properties.getVertexDistance());

	    if (properties.isInside(coordinate.x(), coordinate.y())
		    && !graph.hasVertexInRadius(coordinate, properties.getVertexDistance().min)) {

		// Add a new vertex to graph
		V newVertex = graph.addVertex(coordinate.x(), coordinate.y());
		vertexCount++;
		attemps = 0;
		this.log.info(String.format("Added Vertex %d at %s", vertexCount, newVertex.getPostion().toString()));

		// If..
		
		int edgeCount;

		if (properties.getVertexDistance().min <= properties.getEdgeDistance().max) {
		    graph.addEdge(currentVertex, newVertex);
		    edgeCount = RandomNumbers.getRandom(properties.getEdgeCount().min,
			    properties.getEdgeCount().max - 1);
		} else
		    edgeCount = RandomNumbers.getRandom(properties.getEdgeCount().min, properties.getEdgeCount().max);

		// Update currentVertex with newVertex
		currentVertex = newVertex;

		// Generate edges
		generateEdges(newVertex, edgeCount, properties);

	    } else {
		// Take a random vertex from graph
		currentVertex = graph.getVertex(RandomNumbers.getRandom(0, vertexCount));
	    }

	    attemps++;
	}

	return vertexCount;
    }

    private void generateEdges(V source, int edgeCount, GraphProperties properties) {

	// (1) Radius to gather vertices in environment, specified by a randomly chosen
	// number in interval [properties.edgeDistance.min, properties.edgeDistance.max]
	double edgeDistance = RandomNumbers.getRandom(properties.getEdgeDistance().min,
		properties.getEdgeDistance().max);

	// (2) Gather all vertices in radius, specified by a randomly chosen radius in
	// interval [properties.edgeDistance.min, properties.edgeDistance.max]
	List<V> verticesInRadius = graph.getVerticesInRadius(source, edgeDistance);

	if (verticesInRadius.size() > 0) {

	    // (3) Select n vertices randomly from vertex environment
	    List<V> randomVertices = RandomNumbers.selectNrandomOfM(verticesInRadius, edgeCount, new Random());

	    // (4) Add edges
	    for (V target : randomVertices)
		// Only add edge while target node's number of edges is below requirement given
		// by properties.edgeCount.max
		if (graph.getEdgesOf(target).size() < properties.getEdgeCount().max)
		    graph.addEdge(source, target);
	}
    }

    public Coordinate generateRandomCoordinate(V source, DoubleRange vertexDistanceRange) {

	Coordinate coordinate = null;
	double angleRadians, x, y;

	double distance = RandomNumbers.getRandom(vertexDistanceRange.min,
		vertexDistanceRange.max);
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

    public void generateGridGraph(GridGraphProperties properties) {

	V currentVertex = graph.addVertex(0, 0);
	int vertexCount = 1;

	while ((currentVertex.x() <= properties.getWidth().max - properties.getVertexDistance().max)) {
	    if (vertexCount > 1) {
		double xOffset = RandomNumbers.getRandom(properties.getVertexDistance().min,
			properties.getVertexDistance().max);
		V newVertex = graph.addVertex(currentVertex.x() + xOffset, 0);
		currentVertex = newVertex;
		vertexCount++;	
		List<V> verticesInRadius = graph.getVerticesInRadius(newVertex, properties.getEdgeDistance().max);
		for (V target : verticesInRadius)
		    graph.addEdge(newVertex, target);
	    }

	    while (currentVertex.y() <= (properties.getHeight().max - properties.getVertexDistance().max)) {
		double yOffset = RandomNumbers.getRandom(properties.getVertexDistance().min,
			properties.getVertexDistance().max);
		V newVertex = graph.addVertex(currentVertex.x(), currentVertex.y() + yOffset);
		currentVertex = newVertex;
		vertexCount++;	
		List<V> verticesInRadius = graph.getVerticesInRadius(newVertex, properties.getEdgeDistance().max);
		for (V target : verticesInRadius)
		    graph.addEdge(newVertex, target);
	    }
	}
    }
}