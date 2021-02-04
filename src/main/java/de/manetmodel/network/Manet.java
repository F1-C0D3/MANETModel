package de.manetmodel.network;

import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

import de.manetmodel.graph.EdgeDistance;
import de.manetmodel.graph.UndirectedWeighted2DGraph;
import de.manetmodel.network.radio.IRadioModel;
import de.manetmodel.network.unit.DataRate;
import de.manetmodel.util.Tuple;

public class Manet<N extends Node<W>, L extends Link<W>, W extends EdgeDistance>
	extends UndirectedWeighted2DGraph<N, L, W> {
    private IRadioModel radioModel;
    private DataRate capacity;
    protected DataRate utilization;

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