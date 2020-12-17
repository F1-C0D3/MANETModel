package de.manetmodel.network;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.junit.Test;

import de.manetmodel.algo.DijkstraShortestPath;
import de.manetmodel.graph.Path;
import de.manetmodel.util.Tuple;

public class OccupationTest {

    public Manet<Node, Link> manet;
    DijkstraShortestPath<Node, Link> sp;
    Function<Tuple<Link, Node>, Double> metric;

    public OccupationTest() {
	MyVizualManetGrid manetGrid = new MyVizualManetGrid(30);
	manet = manetGrid.manet;

	sp = new DijkstraShortestPath<Node, Link>(manet);

	metric = (Tuple<Link, Node> p) -> {

//	    Node n = manet.getGraph().getTargetOf(p.getSecond(), p.getFirst());
//	    n.getInterferedLinks();
//	    Set<Link> iLinks = new HashSet<Link>(n.getInterferedLinks());
//	    iLinks.addAll(p.getFirst().inReceptionRange());
//	    return (double) iLinks.size();
	    return 1d;
	};
    }

    @Test
    public void gridNetworkTestOneFlow() {
	List<Flow<Node, Link>> flows = new ArrayList<Flow<Node, Link>>();
	Flow f = new Flow<Node, Link>(manet.getVertex(0), manet.getVertex(4), 1l);

	Path<Node, Link> p = sp.compute(manet.getVertex(0), manet.getVertex(4), metric);

	Iterator<Tuple<Link, Node>> iter = p.listIterator(1);
	while (iter.hasNext()) {
	    Tuple<Link, Node> next = iter.next();
	    f.add(next);
	}
	flows.add(f);
	long res = manet.utilization(flows);
	assertEquals((int) res, 46);

    }

    @Test
    public void gridNetworkTestTwoFlow() {

	List<Flow<Node, Link>> flows = new ArrayList<Flow<Node, Link>>();
	Flow f1 = new Flow<Node, Link>(manet.getVertex(0), manet.getVertex(4), 1l);
	Flow f2 = new Flow<Node, Link>(manet.getVertex(5), manet.getVertex(9), 1l);

	Path<Node, Link> p1 = sp.compute(manet.getVertex(0), manet.getVertex(4), metric);

	Path<Node, Link> p2 = sp.compute(manet.getVertex(5), manet.getVertex(9), metric);

	Iterator<Tuple<Link, Node>> iter1 = p1.listIterator(1);
	while (iter1.hasNext()) {
	    Tuple<Link, Node> next = iter1.next();
	    f1.add(next);
	}

	Iterator<Tuple<Link, Node>> iter2 = p2.listIterator(1);
	while (iter2.hasNext()) {
	    Tuple<Link, Node> next = iter2.next();
	    f2.add(next);
	}
	flows.add(f1);
	flows.add(f2);
	long res = manet.utilization(flows);
	assertEquals((int) res, 116);

    }

}
