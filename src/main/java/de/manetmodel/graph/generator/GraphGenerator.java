package de.manetmodel.graph.generator;

import java.util.List;
import java.util.Random;

import de.manetmodel.graph.EdgeWeightBuilder;
import de.manetmodel.graph.Position2D;
import de.manetmodel.graph.UndirectedWeighted2DGraph;
import de.manetmodel.graph.Vertex;
import de.manetmodel.graph.WeightedEdge;
import de.manetmodel.graph.generator.GraphProperties.DoubleRange;
import de.manetmodel.util.RandomNumbers;
import de.manetmodel.util.log.Log;

public class GraphGenerator<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W> {

    private Log log;
    private UndirectedWeighted2DGraph<V, E, W> graph;
    private EdgeWeightBuilder<V, E, W> edgeWeightBuilder;

    public GraphGenerator(UndirectedWeighted2DGraph<V, E, W> graph) {
	this.log = new Log();
	this.graph = graph;
    }

    public GraphGenerator(UndirectedWeighted2DGraph<V, E, W> graph, EdgeWeightBuilder<V, E, W> edgeWeightBuilder) {
	this.log = new Log();
	this.graph = graph;
	this.edgeWeightBuilder = edgeWeightBuilder;
    }

    public int generateNetworkGraph(NetworkGraphProperties properties) {

	int numberOfVertices = RandomNumbers.getRandom(properties.getVertexCount().min,
		properties.getVertexCount().max);
	int vertexCount = 0, attemps = 0;
	V currentVertex = graph.addVertex(0, 0);

	while (vertexCount < numberOfVertices && attemps < 100) {

	    Position2D Position2D = generateRandomPosition2D(currentVertex, properties.getVertexDistance());

	    if (properties.isInside(Position2D.x(), Position2D.y())
		    && !graph.vertexInRadius(Position2D, properties.getVertexDistance().min)) {

		V newVertex = graph.addVertex(Position2D.x(), Position2D.y());
		vertexCount++;
		attemps = 0;

		if (properties.getVertexDistance().min <= properties.getEdgeDistance().max)
		    addEdge(currentVertex, newVertex);

		addEdges(newVertex, properties.getEdgeDistance().max);
		
		currentVertex = newVertex;
	    } else
		currentVertex = graph.getVertex(RandomNumbers.getRandom(0, vertexCount));

	    attemps++;
	}
	return vertexCount;
    }
    
    private void addEdges(V vertex, double radius) {
	List<V> verticesInRadius = graph.getVerticesInRadius(vertex, radius);

	for (V targetVertex : verticesInRadius)
	    addEdge(vertex, targetVertex);
    }

    private E addEdge(V v1, V v2) {
	if (edgeWeightBuilder != null)
	    return graph.addEdge(v1, v2, edgeWeightBuilder.get(v1, v2));
	else
	    return graph.addEdge(v1, v2);
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
		String.format("Added Vertex %d at %s", vertexCount, graph.getFirstVertex().getPosition().toString()));

	// As long as graph's size doesn't match GraphProperties requirements
	// (vertexCount),
	// process vertex generation process
	while (vertexCount < numberOfVertices && attemps < 100) {

	    // Search a Position2D in GraphProperties which matches vertexDistance
	    // requirements
	    Position2D Position2D = generateRandomPosition2D(currentVertex, properties.getVertexDistance());

	    if (properties.isInside(Position2D.x(), Position2D.y())
		    && !graph.vertexInRadius(Position2D, properties.getVertexDistance().min)) {

		// Add a new vertex to graph
		V newVertex = graph.addVertex(Position2D.x(), Position2D.y());
		vertexCount++;
		attemps = 0;
		this.log.info(String.format("Added Vertex %d at %s", vertexCount, newVertex.getPosition().toString()));

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

    public Position2D generateRandomPosition2D(V source, DoubleRange vertexDistanceRange) {

	Position2D Position2D = null;
	double angleRadians, x, y;

	double distance = RandomNumbers.getRandom(vertexDistanceRange.min, vertexDistanceRange.max);
	double angleDegrees = RandomNumbers.getRandom(0d, 360d);

	if ((angleDegrees >= 0d) && (angleDegrees < 90d)) {
	    angleRadians = Math.toRadians(angleDegrees);
	    x = distance * Math.cos(angleRadians);
	    y = distance * Math.sin(angleRadians);
	    Position2D = new Position2D(source.getPosition().x() + x, source.getPosition().y() + y);
	}

	if ((angleDegrees > 90d) && (angleDegrees <= 180d)) {
	    angleRadians = Math.toRadians(180 - angleDegrees);
	    x = distance * Math.cos(angleRadians);
	    y = distance * Math.sin(angleRadians);
	    Position2D = new Position2D(source.getPosition().x() - x, source.getPosition().y() + y);
	}

	if ((angleDegrees > 180d) && (angleDegrees <= 270d)) {
	    angleRadians = Math.toRadians(270 - angleDegrees);
	    x = distance * Math.sin(angleRadians);
	    y = distance * Math.cos(angleRadians);
	    Position2D = new Position2D(source.getPosition().x() - x, source.getPosition().y() - y);
	}

	if ((angleDegrees > 270d) && (angleDegrees <= 360d)) {
	    angleRadians = Math.toRadians(360 - angleDegrees);
	    x = distance * Math.cos(angleRadians);
	    y = distance * Math.sin(angleRadians);
	    Position2D = new Position2D(source.getPosition().x() + x, source.getPosition().y() - y);
	}

	return Position2D;
    }

    public void generateGridGraph(GridGraphProperties properties) {

	V currentVertex = graph.addVertex(0, 0);
	int vertexCount = 1;

	while ((currentVertex.getPosition().x() <= properties.getWidth().max - properties.getVertexDistance().max)) {
	    if (vertexCount > 1) {
		double xOffset = RandomNumbers.getRandom(properties.getVertexDistance().min,
			properties.getVertexDistance().max);
		V newVertex = graph.addVertex(currentVertex.getPosition().x() + xOffset, 0);
		currentVertex = newVertex;
		vertexCount++;
		List<V> verticesInRadius = graph.getVerticesInRadius(newVertex, properties.getEdgeDistance().max);
		for (V target : verticesInRadius)
		    graph.addEdge(newVertex, target);
	    }

	    while (currentVertex.getPosition()
		    .y() <= (properties.getHeight().max - properties.getVertexDistance().max)) {
		double yOffset = RandomNumbers.getRandom(properties.getVertexDistance().min,
			properties.getVertexDistance().max);
		V newVertex = graph.addVertex(currentVertex.getPosition().x(),
			currentVertex.getPosition().y() + yOffset);
		currentVertex = newVertex;
		vertexCount++;
		List<V> verticesInRadius = graph.getVerticesInRadius(newVertex, properties.getEdgeDistance().max);
		for (V target : verticesInRadius)
		    graph.addEdge(newVertex, target);
	    }
	}
    }
}