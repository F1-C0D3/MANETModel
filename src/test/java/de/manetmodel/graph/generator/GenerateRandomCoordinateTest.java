package de.manetmodel.graph.generator;


import org.junit.Test;

import de.manetmodel.app.VisualGraphApp;
import de.manetmodel.graph.Vertex;

public class GenerateRandomCoordinateTest {

    @Test
    public void GenerateRandomCoordinateTest() {

	WeightedUndirectedGraph<Vertex, Edge> graph = new WeightedUndirectedGraph<Vertex, Edge>(
		new WeightedUndirectedGraphSupplier.VertexSupplier(),
		new WeightedUndirectedGraphSupplier.EdgeSupplier());

	GraphGenerator<Vertex, Edge> generator = new GraphGenerator<Vertex, Edge>(graph);

	Vertex first = graph.addVertex(50, 50);

	for (int i = 0; i < 100; i++) {

	    Playground pg = new Playground();
	    pg.height = new IntRange(0, 100);
	    pg.width = new IntRange(0, 100);
	    pg.edgeCount = new IntRange(2, 4);
	    pg.vertexCount = new IntRange(100, 100);
	    pg.vertexDistance = new DoubleRange(25d, 50d);
	    pg.edgeDistance = new DoubleRange(25d, 50d);
	    
	    Coordinate coordinate = generator.generateRandomCoordinate(first, pg);   
	    Vertex vertex = graph.addVertex(coordinate.x(), coordinate.y());	    
	}
	
	VisualGraphApp<Vertex, Edge> app = new VisualGraphApp<Vertex, Edge>(graph);
	app.run();
    }
}
