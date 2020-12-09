package de.manetmodel.graph;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import de.manetmodel.graph.viz.VisualEdge;
import de.manetmodel.graph.viz.VisualGraph;
import de.manetmodel.graph.viz.VisualVertex;
import de.manetmodel.util.RandomNumbers;
import de.manetmodel.util.Topology;
import de.manetmodel.util.Tuple;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ManetGraph<V extends ManetVertex, E extends ManetEdge>
{
	@XmlTransient
	private final Supplier<V> vertexSupplier;
	@XmlTransient
	private final Supplier<E> edgeSupplier;

	@XmlElement(name="VertexCount")
	private int vertexCount;
	@XmlElement(name="EdgeCount")
	private int edgeCount;
	
	@XmlElementWrapper(name="Vertices")
	@XmlElement(name="Vertex")
	private ArrayList<V> vertices;
	@XmlElementWrapper(name="Edges")
	@XmlElement(name="Edge")
	private ArrayList<E> edges;
	
	@XmlElementWrapper(name="VertexAdjacencies")
	@XmlElement(name="VertexAdjacency")
	private ArrayList<VertexAdjacency> vertexAdjacencies;
	@XmlElementWrapper(name="EdgeAdjacencies")
	@XmlElement(name="EdgeAdjacency")
	private ArrayList<EdgeAdjacency> edgeAdjacencies;
	
	public ManetGraph() 
	{
		this.vertexSupplier = null;
		this.edgeSupplier = null;
		this.vertexCount = 0;
		this.edgeCount = 0;
		this.vertices = new ArrayList<V>();
		this.edges = new ArrayList<E>();
		this.vertexAdjacencies = new ArrayList<VertexAdjacency>();
		this.edgeAdjacencies = new ArrayList<EdgeAdjacency>();
	}	
	
	public ManetGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier)
	{
		this.vertexSupplier = vertexSupplier;
		this.edgeSupplier = edgeSupplier;
		this.vertexCount = 0;
		this.edgeCount = 0;
		this.vertices = new ArrayList<V>();
		this.edges = new ArrayList<E>();
		this.vertexAdjacencies = new ArrayList<VertexAdjacency>();
		this.edgeAdjacencies = new ArrayList<EdgeAdjacency>();
	}

	public V addVertex(double x, double y)
	{
		V vertex = this.vertexSupplier.get();
		vertex.setID(vertexCount++);
		vertex.setPosition(x, y);
		this.vertices.add(vertex);
		this.vertexAdjacencies.add(new VertexAdjacency());
		return vertex;
	}

	public boolean addVertex(V vertex)
	{
		if(this.vertexSupplier == null)
			return false;
		if (vertex.getPostion() == null)
			return false;
		
		vertex.setID(vertexCount++);
		this.vertices.add(vertex);
		this.vertexAdjacencies.add(new VertexAdjacency());
		return true;
	}
	
	public V getVertex(int ID)
	{
		return this.vertices.get(ID);
	}

	public Tuple<V, V> getVerticesOf(E edge)
	{
		Tuple<Integer, Integer> vertexIDs = this.edgeAdjacencies.get(edge.getID()).getVertexIDs();
		return new Tuple<V, V>(this.vertices.get(vertexIDs.getFirst()), this.vertices.get(vertexIDs.getSecond()));
	}

	public E getEdge(V source, V target)
	{
		for (EdgeVertexMapping edgeVertexMapping : vertexAdjacencies.get(source.getID()).getEdgeVertexMappings())
			if (edgeVertexMapping.getVertexID().equals(target.getID()))
				return this.edges.get(edgeVertexMapping.getEdgeID());
		
		return null;
	}

	public boolean containsEdge(V source, V target)
	{
		for (EdgeVertexMapping edgeVertexMapping : vertexAdjacencies.get(source.getID()).getEdgeVertexMappings())
			if (edgeVertexMapping.getVertexID().equals(target.getID()))
				return true;
		
		return false;
	}

	public E addEdge(V source, V target)
	{
		if(this.edgeSupplier == null) 
			return null;
		if (containsEdge(source, target)) 
			return null;
		
		E edge = this.edgeSupplier.get();
		edge.setID(edgeCount++);
		edge.setDistance(getDistance(source, target));
		edge.setVertexIDs(source.getID(), target.getID());
		this.edges.add(edge);
		this.vertexAdjacencies.get(source.getID()).add(new EdgeVertexMapping(edge.getID(), target.getID()));
		this.vertexAdjacencies.get(target.getID()).add(new EdgeVertexMapping(edge.getID(), source.getID()));
		this.edgeAdjacencies.add(new EdgeAdjacency(source.getID(), target.getID()));
		return edge;
	}

	public List<E> getEdgesOf(V vertex)
	{
		List<E> edges = new ArrayList<E>();
		for (EdgeVertexMapping edgeVertexMapping : vertexAdjacencies.get(vertex.getID()).getEdgeVertexMappings())
			edges.add(this.edges.get(edgeVertexMapping.getEdgeID()));
		
		return edges;
	}

	public V getTargetOf(V vertex, E edge)
	{
		Tuple<Integer, Integer> vertexIDs = this.edgeAdjacencies.get(edge.getID()).getVertexIDs();
		if (vertex.getID() == vertexIDs.getFirst())
			return this.vertices.get(vertexIDs.getSecond());
		else if (vertex.getID() == vertexIDs.getSecond())
			return this.vertices.get(vertexIDs.getFirst());
		return null;
	}

	public List<V> getVertices()
	{
		return this.vertices;
	}

	public List<E> getEdges()
	{
		return this.edges;
	}

	public double getDistance(V source, V target)
	{
		return Math.sqrt(Math.pow(source.x() - target.x(), 2) + Math.pow(source.y() - target.y(), 2));
	}

	public double getDistance(Coordinate source, Coordinate target)
	{
		return Math.sqrt(Math.pow(source.x() - target.x(), 2) + Math.pow(source.y() - target.y(), 2));
	}

	public List<V> getVerticesInRadius(Coordinate coordinate, double radius)
	{
		List<V> vertices = new ArrayList<>();
		for (V vertex : this.vertices)
			if (this.getDistance(coordinate, vertex.getPostion()) <= radius)
				vertices.add(vertex);
		return vertices;
	}

	public List<V> getVerticesInRadius(V source, double radius)
	{
		List<V> vertices = new ArrayList<V>();
		for (V vertex : this.vertices)
			if (!vertex.equals(source) && this.getDistance(source.getPostion(), vertex.getPostion()) <= radius)
				vertices.add(vertex);
		return vertices;
	}

	public V getFirstVertex()
	{
		return this.vertices.get(0);
	}

	public V getLastVertex()
	{
		return this.vertices.get(vertexCount - 1);
	}

	public List<V> getNextHopsOf(V vertex)
	{
		List<V> nextHops = new ArrayList<V>();
		for (EdgeVertexMapping edgeVertexMapping : vertexAdjacencies.get(vertex.getID()).getEdgeVertexMappings())
			nextHops.add(this.vertices.get(edgeVertexMapping.getVertexID()));
		return nextHops;
	}

	public void generateSimpleGraph()
	{
		Generator generator = new Generator();
		generator.generateSimpleGraph();
	}

	public void generateTrapeziumGraph()
	{
		Generator generator = new Generator();
		generator.generateTrapeziumGraph();
	}

	public void generateAlmostDeadEndGraph()
	{
		Generator generator = new Generator();
		generator.generateAlmostDeadEndGraph();
	}

	public int generateRandomGraph(Playground pg)
	{
		Generator generator = new Generator();
		return generator.generateRandomGraph(pg);
	}

	public int generateGridGraph(Playground pg)
	{
		Generator generator = new Generator();
		return generator.generateGridGraph(pg);
	}

	public Tuple<IManetVertex, IManetVertex> getVerticesOf(IManetEdge edge)
	{
		Tuple<Integer, Integer> vertexIDs = this.edgeAdjacencies.get(edge.getID()).getVertexIDs();
		return new Tuple<IManetVertex, IManetVertex>(this.vertices.get(vertexIDs.getFirst()),
				this.vertices.get(vertexIDs.getSecond()));
	}

	private ArrayList<VertexAdjacency> getVertexAdjacencies()
	{
		return this.vertexAdjacencies;
	}

	public Iterator<V> vertexIterator()
	{
		Iterator<V> iterator = new Iterator<V>()
		{
			private int i = 0;

			@Override
			public boolean hasNext()
			{
				return i < vertices.size() && vertices.get(i) != null;
			}

			@Override
			public V next()
			{
				return vertices.get(i++);
			}
		};
		return iterator;
	}

	public Iterator<E> edgeIterator()
	{
		Iterator<E> iterator = new Iterator<E>()
		{
			private int i = 0;
			
			@Override
			public boolean hasNext()
			{
				return i < edges.size() && edges.get(i) != null;
			}

			@Override
			public E next()
			{
				return edges.get(i++);
			}
		};
		return iterator;
	}

	public VisualGraph<V,E> toVisualGraph()
	{
		VisualGraph<V,E> graph = new VisualGraph<V,E>(Color.WHITE, Color.LIGHT_GRAY, Color.BLACK);

		for (V vertex : vertices)
			graph.addVertex(new VisualVertex(vertex.getID(), vertex.getPostion(), Color.LIGHT_GRAY, Color.BLACK));

		for (E edge : edges)
		{
			Tuple<V, V> vertices = getVerticesOf(edge);
			graph.addEdge(
				new VisualEdge(
						edge.getID(), 
						vertices.getFirst().getID(), 
						vertices.getSecond().getID(), 
						String.format("%d / %.2f", /*edge.getOccupation().size()*/0, edge.getDistance()),
						Color.BLACK));
		}

		return graph;
	}	
		
	@Override
	public String toString()
	{
		StringBuffer stringBuffer = new StringBuffer();
						
		for(V vertex : this.vertices)
			stringBuffer.append(vertex);
		
		for(E edge : this.edges)
			stringBuffer.append(edge);
		
		// private ArrayList<ArrayList<Tuple<Integer, Integer>>> vertexAdjacencies;
		// private ArrayList<Tuple<Integer, Integer>> edgeAdjacencies;
				
		return stringBuffer.toString();
	}

	public class IO
	{
		public ArrayList<VertexAdjacency> exportPrimitiveVertexAdjacencies()
		{
			return getVertexAdjacencies();
		}		
	}

	public class Generator
	{
		public void generateSimpleGraph()
		{
			V source = addVertex(0d, 0d);
			V a = addVertex(41.21, 56.24);
			V b = addVertex(76.51, 8.77);
			V c = addVertex(92.88, 38.26);
			V target = addVertex(147.49, 99.35);
			addEdge(source, a);
			addEdge(source, a);
			addEdge(source, b);
			addEdge(a, b);
			addEdge(a, c);
			addEdge(b, c);
			addEdge(c, target);
		}

		public void generateAlmostDeadEndGraph()
		{
			V source = addVertex(0, 0);
			V a = addVertex(7, 4);
			V b = addVertex(2, 7);
			V c = addVertex(7, 0);
			V d = addVertex(6, 3);
			V e = addVertex(5, 4);
			V f = addVertex(4, 5);
			V g = addVertex(3, 6);
			V target = addVertex(10, 10);

			addEdge(source, a);
			addEdge(source, b);
			addEdge(source, c);
			addEdge(source, d);
			addEdge(source, e);
			addEdge(source, f);
			addEdge(source, g);

			addEdge(a, target);
		}

		public void generateTrapeziumGraph()
		{
			V source = addVertex(0, 0);
			V a = addVertex(7, 4);
			V b = addVertex(2, 7);
			V target = addVertex(10, 10);

			addEdge(source, a);
			addEdge(source, b);
			addEdge(a, b);
			addEdge(a, target);
			addEdge(b, target);
		}

		public int generateGridGraph(Playground pg){
			
			/*Playground pg = new Playground();
			pg.height = new IntRange(0, 1000);
			pg.width = new IntRange(0, 1000);
			pg.edgeCount = new IntRange(4, 4);
			pg.vertexCount = new IntRange(50, 50);
			pg.edgeDistance = new DoubleRange(100d, 100d);*/

			V currentVertex = addVertex(0, 0);
			int vertexCount = 1;

			while ((currentVertex.x() <= pg.width.max - pg.edgeDistance.max) && vertexCount < pg.vertexCount.max){
				
				if (vertexCount > 1){
					double xOffset = RandomNumbers.getRandom(pg.edgeDistance.min, pg.edgeDistance.max);
					V newVertex = addVertex(currentVertex.x() + xOffset, 0);
					currentVertex = newVertex;
					vertexCount++;
					generateEdges(newVertex, pg);
				}

				while (currentVertex.y() <= (pg.height.max - pg.edgeDistance.max) && vertexCount < pg.vertexCount.max){
					double yOffset = RandomNumbers.getRandom(pg.edgeDistance.min, pg.edgeDistance.max);
					V newVertex = addVertex(currentVertex.x(), currentVertex.y() + yOffset);
					currentVertex = newVertex;
					vertexCount++;
					generateEdges(newVertex, pg);
				}
			}
			return vertexCount;
		}

		private void generateEdges(V source, Playground pg)
		{

			int edgeCount = RandomNumbers.getRandom(pg.edgeCount.min, pg.edgeCount.max);
			double edgeDistance = RandomNumbers.getRandom(pg.edgeDistance.min, pg.edgeDistance.max);

			List<V> verticesInRadius = getVerticesInRadius(source, edgeDistance);

			if (verticesInRadius.size() < edgeCount) {
				for (V vertex : verticesInRadius)
					addEdge(source, vertex);
			} 
			else {
				List<V> randomVertices = getRandomNofM(edgeCount, verticesInRadius);
				for (V vertex : randomVertices)
					addEdge(source, vertex);
			}
		}

		public int generateRandomGraph(Playground pg)
		{
			/*Playground pg = new Playground();
			pg.height = new IntRange(0, 10000);
			pg.width = new IntRange(0, 10000);
			pg.edgeCount = new IntRange(1, 4);
			pg.vertexCount = new IntRange(5, 5);
			pg.edgeDistance = new DoubleRange(15d, 100d);*/

			addVertex(0, 0);
			int vertexCount = 1;

			while (getVertices().size() < pg.vertexCount.max)
			{
				V randomVertex = getVertex(RandomNumbers.getRandom(0, getVertices().size()));
				Coordinate coordinate = generateRandomCoordinate(randomVertex, pg, 100);

				List<V> verticesInRadius = getVerticesInRadius(coordinate, pg.edgeDistance.max);
				int edgeCount = RandomNumbers.getRandom(pg.edgeCount.min, pg.edgeCount.max);

				if (verticesInRadius.size() < edgeCount)
				{
					V newVertex = addVertex(coordinate.x(), coordinate.y());
					vertexCount++;

					for (V vertex : verticesInRadius)
					{
						addEdge(newVertex, vertex);
					}
				}
			}

			return vertexCount;
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

		private List<V> getRandomNofM(int n, List<V> m)
		{

			List<V> vertices = new ArrayList<>();

			for (int i = 0; i < n; i++)
			{
				Random random = new Random();
				vertices.add(m.get(random.nextInt(m.size())));
			}

			return vertices;
		}

	}

	public void createManetGraph(Topology type, double edgeDistance)
	{
		switch (type)
		{
		case GRID:
			generateGridGraph(new Playground());
			break;
		case SIMPLE:
			generateSimpleGraph();
			break;
		case RANDOM:
			generateRandomGraph(new Playground());
			break;
		case DEADEND:
			generateAlmostDeadEndGraph();
			break;
		case TRAPEZIUM:
			generateTrapeziumGraph();
			break;

		default:
			break;
		}

	}
}
