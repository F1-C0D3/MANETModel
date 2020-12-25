package de.manetmodel.graph.generator;

import java.util.List;

import de.manetmodel.graph.EdgeDistance;
import de.manetmodel.graph.EdgeWeightSupplier;
import de.manetmodel.graph.Position2D;
import de.manetmodel.graph.UndirectedWeighted2DGraph;
import de.manetmodel.graph.Vertex;
import de.manetmodel.util.RandomNumbers;

public class GridGraphGenerator<W extends EdgeDistance>
	extends Weighted2DGraphGenerator<W> {

    public GridGraphGenerator(UndirectedWeighted2DGraph<W> graph) {
	super(graph);
    }
    
    public GridGraphGenerator(UndirectedWeighted2DGraph<W> graph, EdgeWeightSupplier<W> edgeWeightSupplier) {
	super(graph, edgeWeightSupplier);
    }

    public void generate(GridGraphProperties properties) {

	Vertex<Position2D> currentVertex = graph.addVertex(0, 0);
	int vertexCount = 1;

	while ((currentVertex.getPosition().x() <= properties.getWidth().max - properties.getVertexDistance().max)) {
	    if (vertexCount > 1) {
		double xOffset = RandomNumbers.getRandom(properties.getVertexDistance().min,
			properties.getVertexDistance().max);
		Vertex<Position2D> newVertex = graph.addVertex(currentVertex.getPosition().x() + xOffset, 0);
		currentVertex = newVertex;
		vertexCount++;
		List<Vertex<Position2D>> verticesInRadius = graph.getVerticesInRadius(newVertex, properties.getEdgeDistance().max);
		for (Vertex<Position2D> target : verticesInRadius)
		    graph.addEdge(newVertex, target);
	    }

	    while (currentVertex.getPosition()
		    .y() <= (properties.getHeight().max - properties.getVertexDistance().max)) {
		double yOffset = RandomNumbers.getRandom(properties.getVertexDistance().min,
			properties.getVertexDistance().max);
		Vertex<Position2D> newVertex = graph.addVertex(currentVertex.getPosition().x(),
			currentVertex.getPosition().y() + yOffset);
		currentVertex = newVertex;
		vertexCount++;
		List<Vertex<Position2D>> verticesInRadius = graph.getVerticesInRadius(newVertex, properties.getEdgeDistance().max);
		for (Vertex<Position2D> target : verticesInRadius)
		    graph.addEdge(newVertex, target);
	    }
	}
    }
}
