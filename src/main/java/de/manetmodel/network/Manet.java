package de.manetmodel.network;

import java.util.ArrayList;
import java.util.function.Supplier;

import de.manetmodel.graph.ManetGraph;
import de.manetmodel.util.Topology;
import de.manetmodel.util.Tuple;

public class Manet<N extends Node, L extends Link, W extends RadioWavePropagation<?>>
{

	private ManetGraph<N, L> graph;
	private ArrayList<Flow<N, L>> flows;

	private W radioWavePropagation;

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

	public void createManet(Topology type, W wavepropagation)
	{
		radioWavePropagation = wavepropagation;
		graph.createManetGraph(type, wavepropagation.transmissionRange());
		networkConnectionSetup();
	}

	private void networkConnectionSetup()
	{
		for (L l : graph.getEdges())
		{
			Tuple<N, N> nt = graph.getVerticesOf(l);
			N source = nt.getFirst();
			N target = nt.getSecond();

			l.setLinkQuality(radioWavePropagation.getTransmissionQuality(l, graph.getDistance(source, target)));

			l.setInterferredNodes(graph.getVerticesInRadius(source, radioWavePropagation.interferenceRange()));
			l.setInterferredNodes(graph.getVerticesInRadius(target, radioWavePropagation.interferenceRange()));

		}
	}
}
