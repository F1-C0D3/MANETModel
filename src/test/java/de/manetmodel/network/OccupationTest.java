package de.manetmodel.network;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.manetmodel.graph.WeightedUndirectedGraph;

public class OccupationTest {

    @Test
    public void gridNetworkTest() {
	MyVizualManetGrid manetGrid = new MyVizualManetGrid(30);
	WeightedUndirectedGraph<Node, Link> graph = manetGrid.manet.getGraph();

	Flow f = new Flow<Node, Link>(graph.getVertex(0), graph.getVertex(4), 02l);

	long res = manetGrid.manet.utilization(f);
	assertEquals(res, 2l);

    }

}
