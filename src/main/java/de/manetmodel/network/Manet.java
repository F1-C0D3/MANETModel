package de.manetmodel.network;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import de.manetmodel.graph.EdgeDistance;
import de.manetmodel.graph.UndirectedWeighted2DGraph;
import de.manetmodel.network.radio.IRadioModel;
import de.manetmodel.network.unit.DataRate;
import de.manetmodel.util.Tuple;

public class Manet<N extends Node<W>, L extends Link<W>, W extends EdgeDistance>
	extends UndirectedWeighted2DGraph<N, L, W> {
    private IRadioModel radioModel;
    private DataRate utilization;
    private DataRate capacity;

    public Manet(Supplier<N> vertexSupplier, Supplier<L> edgeSupplier, IRadioModel radioModel) {
	super(vertexSupplier, edgeSupplier);
	this.radioModel = radioModel;
	this.capacity = new DataRate(0L);
	this.utilization = new DataRate(0L);
    }

    @Override
    public L addEdge(N source, N target) {
	L link = super.addEdge(source, target);

	for (L l : this.getEdges()) {
	    Tuple<N, N> lt = this.getVerticesOf(l);
	    N s1 = lt.getFirst();
	    N s2 = lt.getSecond();

	    double distance = this.getDistance(s1.getPosition(), s2.getPosition());

	    if (l == link) {
		capacity.set(capacity.get() + radioModel.transmissionBitrate(distance).get());
	    }
	    l.setTransmissionRate(radioModel.transmissionBitrate(distance));
	    l.setReceptionPower(radioModel.receptionPower(distance));

	    List<N> lListOfs1 = this.getNextHopsOf(s1);
	    List<N> lListOfs2 = this.getNextHopsOf(s2);

	    for (N n : lListOfs1) {
		l.setInterferedLinks(new HashSet<Link<W>>(this.getEdgesOf(n)));
	    }

	    for (N n : lListOfs2) {
		l.setInterferedLinks(new HashSet<Link<W>>(this.getEdgesOf(n)));
	    }
	}
	return link;
    }

    public boolean addVertex(N node) {
	boolean result = super.addVertex(node);

	for (N n : this.getVertices()) {
	    setLinksInterferedByL(n);
	}
	return result;
    }

    @Override
    public N addVertex(double x, double y) {

	N node = super.addVertex(x, y);

	for (N n : this.getVertices()) {
	    setLinksInterferedByL(n);
	}

	return node;
    }

    private void setLinksInterferedByL(N n) {
	Iterator<L> iterator = this.getEdges().iterator();

	while (iterator.hasNext()) {
	    L le = iterator.next();
	    N se = this.getVerticesOf(le).getFirst();
	    N sk = this.getVerticesOf(le).getSecond();

	    if (radioModel.interferencePresent(this.getDistance(n.getPosition(), se.getPosition()))
		    && radioModel.interferencePresent(this.getDistance(n.getPosition(), sk.getPosition()))) {
		n.setInterferedLink(le);
	    }
	}
    }

    public void addFlow(Flow<N, L, W> flow) {
	Iterator<Tuple<L, N>> flowIterator = flow.listIterator(1);

	while (flowIterator.hasNext()) {
	    Tuple<L, N> linkAndNode = flowIterator.next();
	    L l = linkAndNode.getFirst();
	    Set<Link<W>> interferedLinks = new HashSet<Link<W>>(l.inReceptionRange());
	    N se = this.getTargetOf(linkAndNode.getSecond(), l);
	    interferedLinks.addAll(se.getInterferedLinks());
	    Iterator<Link<W>> iLinkIterator = interferedLinks.iterator();
	    while (iLinkIterator.hasNext()) {
		this.utilization.set(this.utilization.get() + flow.getDataRate().get());
		Link<W> interferedLink = iLinkIterator.next();
		interferedLink.increaseUtilizationBy(flow.getDataRate());
	    }
	}
    }

    /*
     * Must be implemented
     */
    public void removeFlow(Flow<N, L, W> flow) {
	for (Link<W> l : this.getEdges()) {
	    l.setUtilization(new DataRate(0L));
	}
	utilization = new DataRate(0L);
    }

    public DataRate getUtilization() {
	return this.utilization;
    }

    public DataRate getCapacity() {
	return this.capacity;
    }

}