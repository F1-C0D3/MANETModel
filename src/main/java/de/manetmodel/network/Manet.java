package de.manetmodel.network;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import de.manetmodel.graph.WeightedUndirectedGraph;
import de.manetmodel.network.radio.RadioOccupationModel;
import de.manetmodel.util.Tuple;

public class Manet<N extends Node<L>, L extends Link> {
    private WeightedUndirectedGraph<N, L> graph;
    private RadioOccupationModel radioOccupationModel;

    public Manet(Supplier<N> vertexSupplier, Supplier<L> edgeSupplier) {
	graph = new WeightedUndirectedGraph<N, L>(vertexSupplier, edgeSupplier);
    }

    public void initialize() {
	this.networkConnectionSetup();
    }

    public void setGraph(WeightedUndirectedGraph<N, L> graph) {
	this.graph = graph;
    }

    public WeightedUndirectedGraph<N, L> getGraph() {
	return this.graph;
    }

    public void setRadioOccupationModel(RadioOccupationModel radioOccupationModel) {
	this.radioOccupationModel = radioOccupationModel;
    }

    public RadioOccupationModel getRadioOccupationModel() {
	return this.radioOccupationModel;
    }

    private void networkConnectionSetup() {
	Set<L> iLinks = new HashSet<L>();

	for (N v : graph.getVertices()) {
	    Iterator<L> iterator = graph.getEdges().iterator();

	    while (iterator.hasNext()) {
		L e = iterator.next();

		if (radioOccupationModel.interferencePresent(graph.getDistance(v, graph.getVerticesOf(e).getFirst()))
			&& radioOccupationModel
				.interferencePresent(graph.getDistance(v, graph.getVerticesOf(e).getSecond()))) {
		    v.setInterferedLink(e);
		}
	    }
	}
    }

    /*
     * return negative value if deployment exceeds link capacities
     * 
     * @ToDo: Double to mbit unit €
     * 
     * @Todo: graph copy
     */
    public double manetUtilization(List<Flow<N, L>> flows) {
	Iterator<Flow<N, L>> flowsIter = flows.iterator();
	double u = 0d;

	while (flowsIter.hasNext()) {
	    Flow<N, L> f = flowsIter.next();
	    Iterator<Tuple<L, N>> fIter = f.listIterator(1);
	    while (fIter.hasNext()) {
		Tuple<L, N> t = fIter.next();
		L l = t.getFirst();
		Tuple<N, N> nt = graph.getVerticesOf(l);
		N source = nt.getFirst();
		Set<L> pathCopy = new HashSet<L>(source.getInterferedLinks());

		if (l != null) {
		    pathCopy.addAll(l.inReceptionRange());
		}
		Iterator<L> pIter = pathCopy.iterator();

		while (pIter.hasNext()) {
		    pIter.next();
		    u += f.getBitrate();
		}

	    }
	}
	return u;
    }

}