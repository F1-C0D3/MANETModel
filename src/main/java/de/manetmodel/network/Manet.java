package de.manetmodel.network;

import java.util.ArrayList;
import java.util.function.Supplier;

import de.manetmodel.graph.ManetGraph;
import de.manetmodel.util.Topology;

public class Manet<N extends Node, L extends Link>
{

	ManetGraph<N, L> graph;
	ArrayList<Flow<N, L>> flows;

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

	public void createManet(Topology type, double receptionRange)
	{
		graph.createManetGraph(type, receptionRange);

	}
}
