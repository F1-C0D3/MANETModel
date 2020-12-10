package de.manetmodel.graph.generator;

import java.util.List;
import java.util.Random;

import de.manetmodel.graph.Coordinate;
import de.manetmodel.graph.Edge;
import de.manetmodel.graph.Playground;
import de.manetmodel.graph.Vertex;
import de.manetmodel.graph.WeightedUndirectedGraph;
import de.manetmodel.graph.Playground.DoubleRange;
import de.manetmodel.graph.Playground.IntRange;
import de.manetmodel.util.RandomNumbers;

public class GraphGenerator<V extends Vertex, E extends Edge> 
{		
	private WeightedUndirectedGraph<V,E> graph;
	
	public GraphGenerator(WeightedUndirectedGraph<V,E> graph) 
	{
		this.graph = graph;
	}
	
	public int generateRandomGraph(Playground playground)
	{
		graph.addVertex(0, 0);
		int vertexCount = 1;

		while (graph.getVertices().size() < playground.vertexCount.max)
		{
			V randomVertex = graph.getVertex(RandomNumbers.getRandom(0, graph.getVertices().size()));
			Coordinate coordinate = generateRandomCoordinate(randomVertex, playground, 100);

			List<V> verticesInRadius = graph.getVerticesInRadius(coordinate, playground.edgeDistance.max);
			int edgeCount = RandomNumbers.getRandom(playground.edgeCount.min, playground.edgeCount.max);

			if (verticesInRadius.size() < edgeCount)
			{
				V newVertex = graph.addVertex(coordinate.x(), coordinate.y());
				vertexCount++;

				for (V vertex : verticesInRadius)			
					graph.addEdge(newVertex, vertex);
			}
		}

		return vertexCount;
	}

	public void generateGridGraph(int width, int height, double edgeDistance, int numberOfNodes)
	{		
		Playground playground = new Playground();
		playground.height = new IntRange(0, height);
		playground.width = new IntRange(0, width);
		playground.edgeCount = new IntRange(4, 4);
		playground.vertexCount = new IntRange(numberOfNodes, numberOfNodes);
		playground.edgeDistance = new DoubleRange(edgeDistance, edgeDistance);

		V currentVertex = graph.addVertex(0, 0);
		int vertexCount = 1;

		while ((currentVertex.x() <= playground.width.max - playground.edgeDistance.max) && vertexCount < playground.vertexCount.max)
		{		
			if (vertexCount > 1)
			{
				double xOffset = RandomNumbers.getRandom(playground.edgeDistance.min, playground.edgeDistance.max);
				V newVertex = graph.addVertex(currentVertex.x() + xOffset, 0);
				currentVertex = newVertex;
				vertexCount++;
				this.generateEdges(newVertex, playground);
			}

			while (currentVertex.y() <= (playground.height.max - playground.edgeDistance.max) && vertexCount < playground.vertexCount.max)
			{
				double yOffset = RandomNumbers.getRandom(playground.edgeDistance.min, playground.edgeDistance.max);
				V newVertex = graph.addVertex(currentVertex.x(), currentVertex.y() + yOffset);
				currentVertex = newVertex;
				vertexCount++;
				this.generateEdges(newVertex, playground);
			}
		}
	}	

	private void generateEdges(V source, Playground pg)
	{
		int edgeCount = RandomNumbers.getRandom(pg.edgeCount.min, pg.edgeCount.max);
		double edgeDistance = RandomNumbers.getRandom(pg.edgeDistance.min, pg.edgeDistance.max);

		List<V> verticesInRadius = graph.getVerticesInRadius(source, edgeDistance);

		if (verticesInRadius.size() < edgeCount) {
			for (V vertex : verticesInRadius)
				graph.addEdge(source, vertex);
		} 
		else {
			List<V> randomVertices = RandomNumbers.selectNrandomOfM(verticesInRadius, edgeCount, new Random());
			
			for (V vertex : randomVertices)
				graph.addEdge(source, vertex);
		}
	}	
	
	private Coordinate generateRandomCoordinate(V source, Playground pg, int attemps)
	{
		Coordinate coordinate = null;

		do
		{
			double distance = RandomNumbers.getRandom(pg.edgeDistance.min, pg.edgeDistance.max);
			double angleDegrees = RandomNumbers.getRandom(0d, 360d);
			double angleRadians, x, y;

			if ((angleDegrees >= 0d) && (angleDegrees < 90d))
			{
				angleRadians = Math.toRadians(angleDegrees);
				x = distance * Math.cos(angleRadians);
				y = distance * Math.sin(angleRadians);
				coordinate = new Coordinate(source.x() + x, source.y() + y);
			}

			if ((angleDegrees > 90d) && (angleDegrees <= 180d))
			{
				angleRadians = Math.toRadians(180 - angleDegrees);
				x = distance * Math.cos(angleRadians);
				y = distance * Math.sin(angleRadians);
				coordinate = new Coordinate(source.x() - x, source.y() + y);
			}

			if ((angleDegrees > 180d) && (angleDegrees <= 270d))
			{
				angleRadians = Math.toRadians(270 - angleDegrees);
				x = distance * Math.sin(angleRadians);
				y = distance * Math.cos(angleRadians);
				coordinate = new Coordinate(source.x() - x, source.y() - y);
			}

			if ((angleDegrees > 270d) && (angleDegrees <= 360d))
			{
				angleRadians = Math.toRadians(360 - angleDegrees);
				x = distance * Math.cos(angleRadians);
				y = distance * Math.sin(angleRadians);
				coordinate = new Coordinate(source.x() + x, source.y() - y);
			}
			
		} while (!pg.isInside(coordinate.x(), coordinate.y()));

		return coordinate;
	}
		
	/* Move this to class which needs an AlmostDeadEndGraph */
	public void getAlmostDeadEndGraph()
	{
		V source = graph.addVertex(0, 0);
		V a = graph.addVertex(7, 4);
		V b = graph.addVertex(2, 7);
		V c = graph.addVertex(7, 0);
		V d = graph.addVertex(6, 3);
		V e = graph.addVertex(5, 4);
		V f = graph.addVertex(4, 5);
		V g = graph.addVertex(3, 6);
		V target = graph.addVertex(10, 10);

		graph.addEdge(source, a);
		graph.addEdge(source, b);
		graph.addEdge(source, c);
		graph.addEdge(source, d);
		graph.addEdge(source, e);
		graph.addEdge(source, f);
		graph.addEdge(source, g);
		graph.addEdge(a, target);
	}
}
