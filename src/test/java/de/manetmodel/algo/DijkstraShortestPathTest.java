package de.manetmodel.algo;

import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.function.Function;
import org.junit.Test;
import de.manetmodel.app.ManetModelApp;
import de.manetmodel.graph.Path;
import de.manetmodel.graph.Playground;
import de.manetmodel.graph.Playground.DoubleRange;
import de.manetmodel.graph.Playground.IntRange;
import de.manetmodel.graph.generator.GraphGenerator;
import de.manetmodel.network.Link;
import de.manetmodel.network.Manet;
import de.manetmodel.network.ManetSupplier;
import de.manetmodel.network.Node;
import de.manetmodel.network.radio.IdealRadioOccupation;

public class DijkstraShortestPathTest
{
	@Test
	public void DijkstraShortestPathTest()
	{
		Manet<Node<Link>, Link> manet = new Manet<Node<Link>, Link>(
			new ManetSupplier.ManetNodeSupplier(),
			new ManetSupplier.ManetLinkSupplier());
		
		/*Node<Link> source = manet.getGraph().addVertex(0, 0);
		Node<Link> a = manet.getGraph().addVertex(7, 4);
		Node<Link> b = manet.getGraph().addVertex(2, 7);
		Node<Link> target = manet.getGraph().addVertex(10, 10);

		manet.getGraph().addEdge(source, a);
		manet.getGraph().addEdge(source, b);
		manet.getGraph().addEdge(a, b);
		manet.getGraph().addEdge(a, target);
		manet.getGraph().addEdge(b, target);*/
		
		GraphGenerator<Node<Link>,Link> generator = new GraphGenerator<Node<Link>,Link>(manet.getGraph());
		Playground pg = new Playground();
		pg.height = new IntRange(0, 10000);
		pg.width = new IntRange(0, 10000);
		pg.edgeCount = new IntRange(2, 4);
		pg.vertexCount = new IntRange(100, 100);
		pg.edgeDistance = new DoubleRange(50d, 100d);	
		generator.generateRandomGraph(pg);
		
		manet.setRadioOccupationModel(new IdealRadioOccupation(100d, 125d, 2d));
		manet.initialize();
		
		Function<Node<Link>, Double> metric = (Node<Link> n) -> {
			return (double) n.getInterferedLinks().size();
		};

		DijkstraShortestPath<Node<Link>, Link> dijkstra = new DijkstraShortestPath<Node<Link>, Link>(manet.getGraph());
		Path<Node<Link>, Link> shortestPath = dijkstra.compute(manet.getGraph().getFirstVertex(), manet.getGraph().getLastVertex(), metric);		
		ManetModelApp<Node<Link>, Link> app = new ManetModelApp<Node<Link>, Link>(manet.getGraph());	
		app.getPanel().getVisualGraph().addPath(shortestPath, Color.RED);
		app.run();
		
		/*List<Integer> spCompare = new ArrayList<Integer>();
		spCompare.add(0);
		spCompare.add(1);
		spCompare.add(3);

		Iterator<Tuple<Link, Node<Link>>> it = sp.iterator();
		List<Integer> spComputed = new ArrayList<Integer>();

		while (it.hasNext())
		{
			spComputed.add(it.next().getSecond().getID());
		}

		assertTrue(spCompare.equals(spComputed));	
		*/
	}
}
