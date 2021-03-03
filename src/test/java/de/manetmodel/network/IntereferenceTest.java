package de.manetmodel.network;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.junit.Test;

import de.manetmodel.algo.DijkstraShortestPath;
import de.manetmodel.graph.EdgeDistance;
import de.manetmodel.graph.Path;
import de.manetmodel.network.unit.DataRate;
import de.manetmodel.network.unit.Unit;
import de.manetmodel.util.Tuple;

public class IntereferenceTest {

    public MANET<Node<EdgeDistance>, Link<EdgeDistance>, EdgeDistance> manet;
    DijkstraShortestPath<Node<EdgeDistance>, Link<EdgeDistance>> sp;
    Function<Tuple<Link<EdgeDistance>, Node<EdgeDistance>>, Double> metric;

    public IntereferenceTest() {
	MyVizualManetGrid manetGrid = new MyVizualManetGrid(30);
	manet = manetGrid.manet;

	sp = new DijkstraShortestPath<Node<EdgeDistance>, Link<EdgeDistance>>(manet);

	metric = (Tuple<Link<EdgeDistance>, Node<EdgeDistance>> p) -> {

	    Node<EdgeDistance> n = manet.getTargetOf(p.getSecond(), p.getFirst());
	    n.getInterferedLinks();
	    Set<Link<EdgeDistance>> iLinks = new HashSet<Link<EdgeDistance>>(n.getInterferedLinks());
	    iLinks.addAll(p.getFirst().inReceptionRange());
	    return (double) iLinks.size();

	};
    }

    @Test
    public void gridNetworkTestOneFlow() {
	List<Flow<Node<EdgeDistance>, Link<EdgeDistance>, EdgeDistance>> flows = new ArrayList<Flow<Node<EdgeDistance>, Link<EdgeDistance>, EdgeDistance>>();
	Flow<Node<EdgeDistance>, Link<EdgeDistance>, EdgeDistance> f = new Flow<Node<EdgeDistance>, Link<EdgeDistance>, EdgeDistance>(
		manet.getVertex(0), manet.getVertex(4), new DataRate(1d, Unit.Type.bit));

	Path<Node<EdgeDistance>, Link<EdgeDistance>> p = sp.compute(manet.getVertex(0), manet.getVertex(4), metric);

	Iterator<Tuple<Link<EdgeDistance>, Node<EdgeDistance>>> iter = p.listIterator(1);
	while (iter.hasNext()) {
	    Tuple<Link<EdgeDistance>, Node<EdgeDistance>> next = iter.next();
	    f.add(next);
	}
	flows.add(f);
	manet.undeployFlow(null);
	for (Flow<Node<EdgeDistance>, Link<EdgeDistance>, EdgeDistance> nf : flows) {
	    manet.deployFlow(nf);
	}
	assertEquals((int) manet.getUtilization().get(), 46);
    }

    @Test
    public void gridNetworkTestTwoFlow() {

	List<Flow<Node<EdgeDistance>, Link<EdgeDistance>, EdgeDistance>> flows = new ArrayList<Flow<Node<EdgeDistance>, Link<EdgeDistance>, EdgeDistance>>();
	Flow<Node<EdgeDistance>, Link<EdgeDistance>, EdgeDistance> f1 = new Flow<Node<EdgeDistance>, Link<EdgeDistance>, EdgeDistance>(
		manet.getVertex(0), manet.getVertex(4), new DataRate(1, Unit.Type.bit));
	Flow<Node<EdgeDistance>, Link<EdgeDistance>, EdgeDistance> f2 = new Flow<Node<EdgeDistance>, Link<EdgeDistance>, EdgeDistance>(
		manet.getVertex(5), manet.getVertex(9), new DataRate(1, Unit.Type.bit));

	Path<Node<EdgeDistance>, Link<EdgeDistance>> p1 = sp.compute(manet.getVertex(0), manet.getVertex(4), metric);

	Path<Node<EdgeDistance>, Link<EdgeDistance>> p2 = sp.compute(manet.getVertex(5), manet.getVertex(9), metric);

	Iterator<Tuple<Link<EdgeDistance>, Node<EdgeDistance>>> iter1 = p1.listIterator(1);
	while (iter1.hasNext()) {
	    Tuple<Link<EdgeDistance>, Node<EdgeDistance>> next = iter1.next();
	    f1.add(next);
	}

	Iterator<Tuple<Link<EdgeDistance>, Node<EdgeDistance>>> iter2 = p2.listIterator(1);
	while (iter2.hasNext()) {
	    Tuple<Link<EdgeDistance>, Node<EdgeDistance>> next = iter2.next();
	    f2.add(next);
	}
	flows.add(f1);
	flows.add(f2);
	manet.undeployFlow(null);
	for (Flow<Node<EdgeDistance>, Link<EdgeDistance>, EdgeDistance> nf : flows) {
	    manet.deployFlow(nf);
	}
	assertEquals((int) manet.getUtilization().get(), 112);

    }

}
