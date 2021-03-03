package de.manetmodel.network;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.util.Tuple;
import de.manetmodel.network.radio.IRadioModel;
import de.manetmodel.network.unit.DataRate;

public class MANET<N extends Node, L extends Link<W>, W extends LinkQuality, F extends Flow<Node, Link<W>, W>>
	extends UndirectedWeighted2DGraph<N, L, W> {
    private int flowCount;
    private ArrayList<F> flows;
    private IRadioModel radioModel;
    private Supplier<F> flowSupplier;
    private DataRate capacity;
    protected DataRate utilization;

    public MANET(Supplier<N> vertexSupplier, Supplier<L> edgeSupplier, Supplier<F> flowSupplier,
	    IRadioModel radioModel) {
	super(vertexSupplier, edgeSupplier);
	this.flowCount = 0;
	this.flowSupplier = flowSupplier;
	this.radioModel = radioModel;
	this.flows = new ArrayList<F>();
	this.capacity = new DataRate(0L);
	this.utilization = new DataRate(0L);
    }

    public F addFlow(N source, N target, DataRate r) {
	F f = flowSupplier.get();
	f.setId(flowCount++);
	f.setProperties(source, target, r);
	flows.add(f);
	return f;
    }

    public F getFlow(int id) {
	return flows.get(id);
    }

    public List<Integer> getFlowIds() {
	List<Integer> flowIds = new ArrayList<Integer>();
	for (F f : flows)
	    flowIds.add(f.getId());
	return flowIds;
    }

    public void deployFlow(Flow<N, L, W> flow) {
	Iterator<Tuple<L, N>> flowIterator = flow.listIterator(1);

	while (flowIterator.hasNext()) {
	    Tuple<L, N> linkAndNode = flowIterator.next();
	    L l = linkAndNode.getFirst();
	    l.setIsActive(true);
	    increaseUtilizationBy(l, flow.getDataRate());
	}
    }

    /*
     * Must be implemented
     */
    public void undeployFlow(Flow<N, L, W> flow) {
	for (L l : this.getEdges()) {
	    l.getWeight().setUtilization(new DataRate(0L));
	    l.setIsActive(false);
	}
	utilization = new DataRate(0L);
    }

    public void increaseUtilizationBy(L l, DataRate r) {
	Set<Integer> interferedLinks = new HashSet<Integer>(l.getUtilizedLinkIds());
	Iterator<Integer> lIdIterator = interferedLinks.iterator();
	while (lIdIterator.hasNext()) {
	    this.utilization.set(this.utilization.get() + r.get());
	    L interferedLink = this.getEdge(lIdIterator.next());
	    DataRate linkUtilization = interferedLink.getWeight().getUtilization();
	    interferedLink.getWeight().setUtilization(new DataRate(linkUtilization.get() + r.get()));
	}
    }

    @Override
    public L addEdge(N source, N target, W weight) {
	L link = super.addEdge(source, target, weight);

	for (L l : this.getEdges()) {
	    Tuple<N, N> lt = this.getVerticesOf(l);
	    N s1 = lt.getFirst();
	    N s2 = lt.getSecond();

	    double distance = this.getDistance(s1.getPosition(), s2.getPosition());

	    if (l == link) {
		capacity.set(capacity.get() + radioModel.transmissionBitrate(distance).get());
	    }
	    l.getWeight().setTransmissionRate(radioModel.transmissionBitrate(distance));
	    l.getWeight().setReceptionPower(radioModel.receptionPower(distance));

	    List<N> lListOfs1 = this.getNextHopsOf(s1);
	    List<N> lListOfs2 = this.getNextHopsOf(s2);

	    for (N n : lListOfs1) {
		l.setUtilizedLinks(new HashSet<Integer>(this.getEdgeIdsOf(n.getID())));
	    }

	    for (N n : lListOfs2) {
		l.setUtilizedLinks(new HashSet<Integer>(this.getEdgeIdsOf(n.getID())));
	    }

	    l.getWeight().setUtilizedLinks(l.getUtilizedLinkIds().size());
	}
	return link;
    }

    public DataRate getOverUtilizedLinks() {
	DataRate overUtilization = new DataRate(0L);

	for (L l : this.getEdges()) {
	    if (l.getIsActive()) {
		DataRate tRate = l.getWeight().getTransmissionRate();
		DataRate utilization = l.getWeight().getUtilization();
		double oU = tRate.get() - utilization.get();
		overUtilization.set(oU < 0 ? overUtilization.get() + (long) Math.abs(oU) : overUtilization.get());
	    }
	}

	return overUtilization;
    }

    /*
     * Method detects links which are interfered due to eventually overlapping
     * transmissions. Is not considered to be used during evaluations
     * 
     * private void setLinksInterferedByL(N n) { Iterator<L> iterator =
     * this.getEdges().iterator();
     * 
     * while (iterator.hasNext()) { L le = iterator.next(); N se =
     * this.getVerticesOf(le).getFirst(); N sk = this.getVerticesOf(le).getSecond();
     * 
     * if (radioModel.interferencePresent(this.getDistance(n.getPosition(),
     * se.getPosition())) &&
     * radioModel.interferencePresent(this.getDistance(n.getPosition(),
     * sk.getPosition()))) { n.setInterferedLink(le); } } }
     */

    public DataRate getUtilization() {
	return this.utilization;
    }

    public DataRate getCapacity() {
	return this.capacity;
    }

}