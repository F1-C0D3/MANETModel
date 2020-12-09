package de.manetmodel.network;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Supplier;

import de.manetmodel.graph.ManetGraph;
import de.manetmodel.graph.Playground;
import de.manetmodel.network.radio.RadioOccupationModel;
import de.manetmodel.util.Topology;

public class Manet<N extends Node, L extends Link>
{

	private ManetGraph<N, L> graph;
	private ArrayList<Flow<N, L>> flows;

	private RadioOccupationModel<N, L> occupationModel;

	public Manet(Supplier<N> vertexSupplier, Supplier<L> edgeSupplier)
	{
		graph = new ManetGraph<N, L>(vertexSupplier, edgeSupplier);
	}

	public ManetGraph<N, L> getGraph()
	{
		return this.graph;
	}

	public void addFlow(Flow<N, L> flow)
	{
		this.flows.add(flow);
	}

	public void createManet(Topology type, RadioOccupationModel<N, L> occupationModel)
	{
		this.occupationModel = occupationModel;
		createGraph(type);
		networkConnectionSetup();
	}

	public void createGraph(Topology type)
	{
		switch (type)
		{
		case GRID:
			graph.generateGridGraph(new Playground());
			break;
		case SIMPLE:
			graph.generateSimpleGraph();
			break;
		case RANDOM:
			graph.generateRandomGraph(new Playground());
			break;
		case DEADEND:
			graph.generateAlmostDeadEndGraph();
			break;
		case TRAPEZIUM:
			graph.generateTrapeziumGraph();
			break;

		default:
			break;
		}

	}

	private void networkConnectionSetup()
	{
		Set<L> iLinks = new HashSet<L>();
		for (N v : graph.getVertices())
		{
			Iterator<L> iterator = graph.getEdges().iterator();

			while (iterator.hasNext())
			{
				L e = iterator.next();

				if (occupationModel.interferencePresent(graph.getDistance(v, graph.getVerticesOf(e).getFirst()))
						&& occupationModel
								.interferencePresent(graph.getDistance(v, graph.getVerticesOf(e).getSecond())))
				{
					v.setInterferedLink(e);
				}

			}

		}
	}
}
