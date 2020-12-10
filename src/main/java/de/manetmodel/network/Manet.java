package de.manetmodel.network;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Supplier;

import de.manetmodel.graph.WeightedUndirectedGraph;
import de.manetmodel.network.radio.RadioOccupationModel;

public class Manet<N extends Node<L>, L extends Link>
{
	private WeightedUndirectedGraph<N,L> graph;
	private ArrayList<Flow<N, L>> flows;
	private RadioOccupationModel radioOccupationModel;

	public Manet(Supplier<N> vertexSupplier, Supplier<L> edgeSupplier)
	{
		graph = new WeightedUndirectedGraph<N,L>(vertexSupplier, edgeSupplier);
	}
	
	public void initialize() {	
		this.networkConnectionSetup();
	}
	
	public void setGraph(WeightedUndirectedGraph<N,L> graph) {
		this.graph = graph;
	}
	
	public WeightedUndirectedGraph<N,L> getGraph() {
		return this.graph;
	}
	
	public void setRadioOccupationModel(RadioOccupationModel radioOccupationModel) {
		this.radioOccupationModel = radioOccupationModel;
	}
	
	public RadioOccupationModel getRadioOccupationModel(){
		return this.radioOccupationModel;
	}
	
	public void addFlow(Flow<N, L> flow)
	{
		this.flows.add(flow);
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

				if (radioOccupationModel.interferencePresent(graph.getDistance(v, graph.getVerticesOf(e).getFirst())) && 
						radioOccupationModel.interferencePresent(graph.getDistance(v, graph.getVerticesOf(e).getSecond())))
				{
					v.setInterferedLink(e);
				}
			}
		}
	}
}
