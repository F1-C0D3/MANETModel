package de.manetmodel.graph.generator;

import java.util.List;

import de.manetmodel.graph.EdgeDistance;
import de.manetmodel.graph.EdgeWeightSupplier;
import de.manetmodel.graph.Position2D;
import de.manetmodel.graph.UndirectedWeighted2DGraph;
import de.manetmodel.graph.Vertex;
import de.manetmodel.graph.generator.GraphProperties.DoubleRange;
import de.manetmodel.util.RandomNumbers;
import de.manetmodel.util.log.Log;

public abstract class Weighted2DGraphGenerator<W extends EdgeDistance> {

    protected Log log;
    protected UndirectedWeighted2DGraph<W> graph;
    protected EdgeWeightSupplier<W> edgeWeightSupplier;

    public Weighted2DGraphGenerator(UndirectedWeighted2DGraph<W> graph) {
	this.log = new Log();
	this.graph = graph;
    }

    public Weighted2DGraphGenerator(UndirectedWeighted2DGraph<W> graph, EdgeWeightSupplier<W> edgeWeightSupplier) {
	this.log = new Log();
	this.graph = graph;
	this.edgeWeightSupplier = edgeWeightSupplier;
    }

    public Boolean edgeWeightSupplier() {
	return edgeWeightSupplier != null;
    }

    protected void connectVerticesInRadius(Vertex<Position2D> vertex, double radius) {
	List<Vertex<Position2D>> verticesInRadius = graph.getVerticesInRadius(vertex, radius);
	for (Vertex<Position2D> targetVertex : verticesInRadius)
	    if (edgeWeightSupplier()) {
		W edgeWeight = edgeWeightSupplier.get();
		edgeWeight.setDistance(graph.getDistance(vertex.getPosition(), targetVertex.getPosition()));
		graph.addEdge(vertex, targetVertex, edgeWeight);
	    }
	    else
		graph.addEdge(vertex, targetVertex);
    }

    protected Position2D generateRandomPosition2D(Vertex<Position2D> source, DoubleRange vertexDistanceRange) {

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
}
