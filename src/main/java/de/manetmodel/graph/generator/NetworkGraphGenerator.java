package de.manetmodel.graph.generator;

import de.manetmodel.graph.EdgeDistance;
import de.manetmodel.graph.EdgeWeightSupplier;
import de.manetmodel.graph.Position2D;
import de.manetmodel.graph.UndirectedWeighted2DGraph;
import de.manetmodel.graph.Vertex;
import de.manetmodel.util.RandomNumbers;

public class NetworkGraphGenerator<W extends EdgeDistance> extends Weighted2DGraphGenerator<W> {

    public NetworkGraphGenerator(UndirectedWeighted2DGraph<W> graph) {
	super(graph);
    }

    public NetworkGraphGenerator(UndirectedWeighted2DGraph<W> graph, EdgeWeightSupplier<W> edgeWeightSupplier) {
	super(graph, edgeWeightSupplier);
    }

    public int generate(NetworkGraphProperties properties) {

	int numberOfVertices = RandomNumbers.getRandom(properties.getVertexCount().min,
		properties.getVertexCount().max);
	int vertexCount = 0, attemps = 0;
	Vertex<Position2D> currentVertex = graph.addVertex(0, 0);

	while (vertexCount < numberOfVertices && attemps < 100) {

	    Position2D Position2D = generateRandomPosition2D(currentVertex, properties.getVertexDistance());

	    if (properties.isInside(Position2D.x(), Position2D.y())
		    && !graph.vertexInRadius(Position2D, properties.getVertexDistance().min)) {

		Vertex<Position2D> newVertex = graph.addVertex(Position2D.x(), Position2D.y());
		vertexCount++;
		attemps = 0;

		if (properties.getVertexDistance().min <= properties.getEdgeDistance().max)
		    connectVerticesInRadius(newVertex, properties.getEdgeDistance().max);

		currentVertex = newVertex;
	    } else
		currentVertex = graph.getVertex(RandomNumbers.getRandom(0, vertexCount));

	    attemps++;
	}
	return vertexCount;
    }
}