package de.manetmodel.network;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import de.manetmodel.graph.ManetGraph;
import de.manetmodel.graph.Playground;
import de.manetmodel.graph.Playground.DoubleRange;
import de.manetmodel.graph.Playground.IntRange;
import de.manetmodel.network.radio.RadioOccupationModel;
import de.manetmodel.util.Topology;
import de.manetmodel.util.Tuple;

public class Manet<N extends Node, L extends Link> {

    private int flowCount;
    private ManetGraph<N, L> graph;

    private RadioOccupationModel<N, L> occupationModel;

    public Manet(Supplier<N> vertexSupplier, Supplier<L> edgeSupplier) {
	graph = new ManetGraph<N, L>(vertexSupplier, edgeSupplier);
	this.flowCount = 0;
    }

    private void networkConnectionSetup() {
	for (L l : graph.getEdges()) {

	    Tuple<N, N> nt = graph.getVerticesOf(l);
	    N s = nt.getFirst();
	    N t = nt.getSecond();
	    l.setTransmissionRate(occupationModel.computeTransmissionBitrate());
	    l.setReceptionPower(occupationModel.computeReception(graph.getDistance(s, t)));

	    l.setInReceptionRange(new HashSet<L>(graph.getEdgesOf(s)));
	    l.setInReceptionRange(new HashSet<L>(graph.getEdgesOf(t)));

	}

	for (N v : graph.getVertices()) {
	    Iterator<L> iterator = graph.getEdges().iterator();

	    while (iterator.hasNext()) {
		L e = iterator.next();

		if (occupationModel.interferencePresent(graph.getDistance(v, graph.getVerticesOf(e).getFirst()))
			&& occupationModel
				.interferencePresent(graph.getDistance(v, graph.getVerticesOf(e).getSecond()))) {
		    v.setInterferedLink(e);
		}

	    }

	}
    }

    public ManetGraph<N, L> getGraph() {
	return this.graph;
    }

    public void createManet(Topology type, RadioOccupationModel<N, L> occupationModel) {
	this.occupationModel = occupationModel;
	createManet(type);
	networkConnectionSetup();
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

    public void createManet(Topology type) {
	switch (type) {
	case GRID:
	    Playground pg = new Playground();
	    pg.height = new IntRange(0, 1000);
	    pg.width = new IntRange(0, 1000);
	    pg.edgeCount = new IntRange(4, 4);
	    pg.vertexCount = new IntRange(55, 55);
	    pg.edgeDistance = new DoubleRange(100d, 100d);

	    graph.generateGridGraph(pg);
	    break;
	case SIMPLE:
	    graph.generateSimpleGraph();
	    break;
	case RANDOM:
	    graph.generateRandomGraph(new Playground());
	    break;
	case DEADEND:
	    graph.generateAlmostDeadEndGraph();
	    break;
	case TRAPEZIUM:
	    graph.generateTrapeziumGraph();
	    break;

	default:
	    break;
	}

    }

}