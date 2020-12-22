package de.manetmodel.network;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import de.manetmodel.graph.WeightedUndirectedGraph;
import de.manetmodel.network.radio.IRadioModel;
import de.manetmodel.network.unit.DataRate;
import de.manetmodel.util.Tuple;

public class Manet<N extends Node, L extends Link> extends WeightedUndirectedGraph<N, L> {
    private IRadioModel radioModel;

    public Manet(Supplier<N> vertexSupplier, Supplier<L> edgeSupplier, IRadioModel radioModel) {
	super(vertexSupplier, edgeSupplier);
	this.radioModel = radioModel;
    }

    @Override
    public L addEdge(N source, N target) {
	L e = super.addEdge(source, target);

	for (L l : this.getEdges()) {
	    Tuple<N, N> lt = this.getVerticesOf(l);
	    N s1 = lt.getFirst();
	    N s2 = lt.getSecond();
	    double distance = this.getDistance(s1, s2);
	    l.setTransmissionRate(radioModel.transmissionBitrate(distance));
	    l.setReceptionPower(radioModel.receptionPower(distance));

	    List<N> lListOfs1 = this.getNextHopsOf(s1);
	    List<N> lListOfs2 = this.getNextHopsOf(s2);

	    for (N n : lListOfs1) {
		l.setInReceptionRange(new HashSet<L>(this.getEdgesOf(n)));
	    }

	    for (N n : lListOfs2) {
		l.setInReceptionRange(new HashSet<L>(this.getEdgesOf(n)));
	    }

	}
	return e;
    }

    @Override
    public boolean addVertex(N vertex) {
	boolean result = super.addVertex(vertex);

	for (N v : this.getVertices()) {
	    setLinksInterferedByL(v);
	}
	return result;

    }

    @Override
    public N addVertex(double x, double y) {
	N n = super.addVertex(x, y);

	for (N v : this.getVertices()) {
	    setLinksInterferedByL(v);
	}
	return n;
    }

    private void setLinksInterferedByL(N n) {
	Iterator<L> iterator = this.getEdges().iterator();

	while (iterator.hasNext()) {
	    L le = iterator.next();
	    N se = this.getVerticesOf(le).getFirst();
	    N sk = this.getVerticesOf(le).getSecond();

	    if (radioModel.interferencePresent(this.getDistance(n, se))
		    && radioModel.interferencePresent(this.getDistance(n, sk))) {
		n.setInterferedLink(le);
	    }
	}
    }

    /*
     * return negative value if deployment exceeds link capacities
     * 
     * @ToDo: Double to mbit unit
     * 
     * @Todo: graph copy
     */
    public DataRate utilization(List<Flow<N, L>> flows) {
	/* set link utilization to initial value (0) */
//	relaxL();
	Iterator<Flow<N, L>> flowsIter = flows.iterator();

	long networkUtilization = 0L;

	while (flowsIter.hasNext()) {
	    Flow<N, L> f = flowsIter.next();
	    Iterator<Tuple<L, N>> fIter = f.listIterator(1);

	    while (fIter.hasNext()) {
		Tuple<L, N> ln = fIter.next();
		L l = ln.getFirst();
		Set<L> uLinks = new HashSet<L>(l.inReceptionRange());
		N se = this.getTargetOf(ln.getSecond(), l);
		uLinks.addAll(se.getInterferedLinks());
		Iterator<L> pIter = uLinks.iterator();

		while (pIter.hasNext()) {
//		    L ul = pIter.next();
//		    ul.setTransmissionRate(new DataRate(f.getDataRate().get() + ul.getTransmissionRate().get()));

		    networkUtilization += f.getDataRate().get();
		}
	    }
	}
	return new DataRate(networkUtilization);
    }

//    private void relaxL() {
//
//	for (L l : this.getEdges()) {
//	    l.setUtilization(0L);
//	}
//    }

}