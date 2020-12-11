package de.manetmodel.network;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import de.manetmodel.graph.WeightedUndirectedGraph;
import de.manetmodel.network.radio.RadioOccupationModel;
import de.manetmodel.util.Tuple;

public class Manet<N extends Node, L extends Link> {
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
	for (L l : graph.getEdges()) {
	    Tuple<N, N> lt = graph.getVerticesOf(l);
	    N se = lt.getFirst();
	    N sk = lt.getSecond();

	    l.setTransmissionRate(radioOccupationModel.computeTransmissionBitrate());
	    l.setReceptionPower(radioOccupationModel.computeReception(graph.getDistance(se, sk)));

	    List<N> lListOfse = graph.getNextHopsOf(se);
	    List<N> lListOfsk = graph.getNextHopsOf(sk);

	    for (N n : lListOfse) {
		l.setInReceptionRange(new HashSet<L>(graph.getEdgesOf(n)));
	    }

	    for (N n : lListOfsk) {
		l.setInReceptionRange(new HashSet<L>(graph.getEdgesOf(n)));
	    }

	}

	for (N v : graph.getVertices()) {
	    Iterator<L> iterator = graph.getEdges().iterator();

	    while (iterator.hasNext()) {
		L le = iterator.next();
		N se = graph.getVerticesOf(le).getFirst();
		N sk = graph.getVerticesOf(le).getSecond();

		if (radioOccupationModel.interferencePresent(graph.getDistance(v, se))
			&& radioOccupationModel.interferencePresent(graph.getDistance(v, sk))) {
		    v.setInterferedLink(le);
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
    public double utilization(List<Flow<N, L>> flows) {
	Iterator<Flow<N, L>> flowsIter = flows.iterator();

	/* utilization */
	double u = 0d;

	while (flowsIter.hasNext()) {
	    Flow<N, L> f = flowsIter.next();
	    Iterator<Tuple<L, N>> fIter = f.listIterator(1);

	    while (fIter.hasNext()) {
		Tuple<L, N> ln = fIter.next();
		L l = ln.getFirst();
		Set<L> uLinks = new HashSet<L>(l.inReceptionRange());
		N se = graph.getTargetOf(ln.getSecond(), l);
		uLinks.addAll(se.getInterferedLinks());
		Iterator<L> pIter = uLinks.iterator();

		while (pIter.hasNext()) {
		    pIter.next();
		    u += f.getBitrate();
		}

	    }
	}
	return u;
    }

}