package de.manetmodel.graph;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import de.manetmodel.graph.Playground.DoubleRange;
import de.manetmodel.graph.Playground.IntRange;
import de.manetmodel.util.RandomNumbers;
import de.manetmodel.util.Tuple;
import de.manetmodel.visualization.VisualEdge;
import de.manetmodel.visualization.VisualGraph;
import de.manetmodel.visualization.VisualVertex;

public class ManetGraph<V extends ManetVertex, E extends ManetEdge>
{

	private final Supplier<V> vertexSupplier;
	private final Supplier<E> edgeSupplier;

	private int vertexCount;
	private int edgeCount;

	private ArrayList<V> vertices;
	private ArrayList<E> edges;

	private ArrayList<ArrayList<Tuple<Integer, Integer>>> vertexAdjacencies;
	private ArrayList<Tuple<Integer, Integer>> edgeAdjacencies;

	public ManetGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier)
	{
		this.vertexSupplier = vertexSupplier;
		this.edgeSupplier = edgeSupplier;
		this.vertexCount = 0;
		this.edgeCount = 0;
		this.vertices = new ArrayList<V>();
		this.edges = new ArrayList<E>();
		this.vertexAdjacencies = new ArrayList<ArrayList<Tuple<Integer, Integer>>>();
		this.edgeAdjacencies = new ArrayList<Tuple<Integer, Integer>>();
	}

	public V addVertex(double x, double y)
	{
		V vertex = this.vertexSupplier.get();
		vertex.setID(vertexCount++);
		vertex.setPosition(x, y);
		this.vertices.add(vertex);
		this.vertexAdjacencies.add(new ArrayList<Tuple<Integer, Integer>>());
		return vertex;
	}

	public boolean addVertex(V vertex)
	{
		if (vertex.getPostion() == null)
			return false;
		vertex.setID(vertexCount++);
		this.vertices.add(vertex);
		this.vertexAdjacencies.add(new ArrayList<Tuple<Integer, Integer>>());
		return true;
	}

	public V getVertex(int ID)
	{
		return this.vertices.get(ID);
	}

	public Tuple<V, V> getVerticesOf(E edge)
	{
		Tuple<Integer, Integer> vertexIDs = this.edgeAdjacencies.get(edge.getID());
		return new Tuple<V, V>(this.vertices.get(vertexIDs.getFirst()), this.vertices.get(vertexIDs.getSecond()));
	}

	public E getEdge(V source, V target)
	{
		for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(source.getID()))
			if (adjacency.getSecond() == target.getID())
				return this.edges.get(adjacency.getFirst());
		return null;
	}

	public boolean containsEdge(V source, V target)
	{
		for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(source.getID()))
			if (adjacency.getSecond() == target.getID())
				return true;
		return false;
	}

	public E addEdge(V source, V target)
	{
		if (containsEdge(source, target))
			return null;
		E edge = this.edgeSupplier.get();
		edge.setID(edgeCount++);
		edge.setDistance(this.getDistance(source, target));
		edge.setVertexIDs(source.getID(), target.getID());
		this.edges.add(edge);
		this.vertexAdjacencies.get(source.getID()).add(new Tuple<Integer, Integer>(edge.getID(), target.getID()));
		this.vertexAdjacencies.get(target.getID()).add(new Tuple<Integer, Integer>(edge.getID(), source.getID()));
		this.edgeAdjacencies.add(new Tuple<Integer, Integer>(source.getID(), target.getID()));
		return edge;
	}

	public List<E> getEdgesOf(V vertex)
	{
		List<E> edges = new ArrayList<E>();
		for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(vertex.getID()))
			edges.add(this.edges.get(adjacency.getFirst()));
		return edges;
	}

	public V getTargetOf(V vertex, E edge)
	{
		Tuple<Integer, Integer> vertexIDs = this.edgeAdjacencies.get(edge.getID());
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
		List<V> vertices = new ArrayList<>();
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
		for (Tuple<Integer, Integer> adjacency : vertexAdjacencies.get(vertex.getID()))
			nextHops.add(this.vertices.get(adjacency.getSecond()));
		return nextHops;
	}

	@Override
	public String toString()
	{
		String str = "Not implemented yet";
		return str;
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

	public void generateDeadEndGraph()
	{
		Generator generator = new Generator();
		generator.generateDeadEndGraph();
	}

	public int generateRandomGraph()
	{
		Generator generator = new Generator();
		return generator.generateRandomGraph();
	}

	public int generateGridGraph()
	{
		Generator generator = new Generator();
		return generator.generateGridGraph();
	}

	public Tuple<IManetVertex, IManetVertex> getVerticesOf(IManetEdge edge)
	{
		Tuple<Integer, Integer> vertexIDs = this.edgeAdjacencies.get(edge.getID());
		return new Tuple<IManetVertex, IManetVertex>(this.vertices.get(vertexIDs.getFirst()),
				this.vertices.get(vertexIDs.getSecond()));
	}

	private ArrayList<ArrayList<Tuple<Integer, Integer>>> getVertexAdjacencies()
	{
		return vertexAdjacencies;
	}

	public Iterator<V> vertexBaseIterator()
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

	public Iterator<E> edgeBaseIterator()
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

	public VisualGraph toVisualGraph()
	{

		VisualGraph graph = new VisualGraph(Color.WHITE, Color.LIGHT_GRAY, Color.BLACK);

		for (V vertex : vertices)
			graph.addVertex(new VisualVertex(vertex.getID(), vertex.getPostion()));

		for (E edge : edges)
		{
			Tuple<V, V> vertices = getVerticesOf(edge);
			graph.addEdge(
					new VisualEdge(edge.getID(), vertices.getFirst().getPostion(), vertices.getSecond().getPostion(),
							String.format("%d / %.2f", edge.getOccupation().size(), edge.getDistance())));
		}

		return graph;
	}

	public class IO
	{

		public void importGraph()
		{

		}

		public ArrayList<ArrayList<Tuple<Integer, Integer>>> exportPrimitiveVertexAdjacencies()
		{
			return getVertexAdjacencies();
		}
	}

	public class Generator
	{

		public void generateSimpleGraph()
		{

			V source = addVertex(0, 0);
			V a = addVertex(7, 4);
			V b = addVertex(2, 7);
			V c = addVertex(3, 12);
			V target = addVertex(10, 10);

			addEdge(source, a);
			addEdge(source, b);
			addEdge(a, b);
			addEdge(b, c);
			addEdge(a, target);
			addEdge(b, target);

			generateOccupation();
		}

		public void generateDeadEndGraph()
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

			generateOccupation();

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

			generateOccupation();
		}

		public int generateGridGraph()
		{

			Playground pg = new Playground();
			pg.height = new IntRange(0, 1000);
			pg.width = new IntRange(0, 1000);
			pg.edgeCount = new IntRange(4, 4);
			pg.vertexCount = new IntRange(50, 100);
			pg.vertexDistance = new DoubleRange(75d, 75d);
			pg.edgeDistance = new DoubleRange(100d, 100d);

			V currentVertex = addVertex(0, 0);
			int vertexCount = 1;

			while ((currentVertex.x() <= pg.width.max - pg.vertexDistance.max) && vertexCount < pg.vertexCount.max)
			{

				if (vertexCount > 1)
				{
					double xOffset = RandomNumbers.getRandom(pg.vertexDistance.min, pg.vertexDistance.max);
					V newVertex = addVertex(currentVertex.x() + xOffset, 0);
					currentVertex = newVertex;
					vertexCount++;
					generateEdges(newVertex, pg);
				}

				while (currentVertex.y() <= (pg.height.max - pg.vertexDistance.max))
				{
					double yOffset = RandomNumbers.getRandom(pg.vertexDistance.min, pg.vertexDistance.max);
					V newVertex = addVertex(currentVertex.x(), currentVertex.y() + yOffset);
					currentVertex = newVertex;
					vertexCount++;
					generateEdges(newVertex, pg);
				}
			}

			generateOccupation();
			return vertexCount;
		}

		private void generateEdges(V source, Playground pg)
		{

			int edgeCount = RandomNumbers.getRandom(pg.edgeCount.min, pg.edgeCount.max);
			double edgeDistance = RandomNumbers.getRandom(pg.edgeDistance.min, pg.edgeDistance.max);

			List<V> verticesInRadius = getVerticesInRadius(source, edgeDistance);

			if (verticesInRadius.size() < edgeCount)
			{
				for (V vertex : verticesInRadius)
					addEdge(source, vertex);
			} else
			{
				List<V> randomVertices = getRandomNofM(edgeCount, verticesInRadius);
				for (V vertex : randomVertices)
					addEdge(source, vertex);
			}
		}

		private void generateOccupation()
		{

			for (E edge : edges)
			{

				Tuple<V, V> vertices = getVerticesOf(edge);
				List<V> nextHops = getNextHopsOf(vertices.getFirst());

				for (V v : nextHops)
					if (!v.equals(vertices.getSecond()))
						for (E e : getEdgesOf(v))
							edge.addOccupation(e.getID());

				nextHops = getNextHopsOf(vertices.getSecond());

				for (V v : nextHops)
					if (!v.equals(vertices.getSecond()))
						for (E e : getEdgesOf(v))
							edge.addOccupation(e.getID());
			}
		}

		public int generateRandomGraph()
		{

			Playground pg = new Playground();
			pg.height = new IntRange(0, 10000);
			pg.width = new IntRange(0, 10000);
			pg.edgeCount = new IntRange(2, 4);
			pg.vertexCount = new IntRange(50, 1000);
			pg.vertexDistance = new DoubleRange(25d, 50d);
			pg.edgeDistance = new DoubleRange(25d, 35d);

			V currentVertex = addVertex(0, 0);
			int vertexCount = 1;

			while (getVertices().size() < pg.vertexCount.max)
			{
				Coordinate coordinate = generateRandomCoordinate(currentVertex, pg, 100);

				if (coordinate != null)
				{
					V newVertex = addVertex(coordinate.x(), coordinate.y());
					addEdge(currentVertex, newVertex);
					currentVertex = newVertex;
					vertexCount++;
					int edgeCount = RandomNumbers.getRandom(pg.edgeCount.min, pg.edgeCount.max);

					List<V> verticesInRadius = getVerticesInRadius(currentVertex, pg.edgeDistance.max);

					if (verticesInRadius.size() < edgeCount)
					{
						for (V vertex : verticesInRadius)
							addEdge(currentVertex, vertex);
					} else
					{
						List<V> randomVertices = getRandomNofM(edgeCount, verticesInRadius);
						for (V vertex : randomVertices)
							addEdge(currentVertex, vertex);
					}
				} else
					break;
			}

			generateOccupation();
			return vertexCount;
		}

		private Coordinate generateRandomCoordinate(V source, Playground pg, int attemps)
		{

			Coordinate coordinate = null;
			int i = 1;

			while (i <= attemps)
			{

				double distance = RandomNumbers.getRandom(pg.vertexDistance.min, pg.vertexDistance.max);
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

				if (pg.isInside(coordinate.x(), coordinate.y()))
					if (getVerticesInRadius(coordinate, pg.vertexDistance.min).isEmpty())
						return coordinate;
				i++;
			}

			return null;
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
}
