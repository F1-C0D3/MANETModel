package de.manetmodel.manetgraph;

import java.util.function.Supplier;

import de.manetmodel.graph.EdgeDistance;
import de.manetmodel.graph.Position2D;
import de.manetmodel.graph.UndirectedWeighted2DGraph;
import de.manetmodel.graph.Vertex;
import de.manetmodel.graph.WeightedEdge;
import de.manetmodel.graph.generator.GraphGenerator;
import de.manetmodel.graph.generator.NetworkGraphGenerator;

public class AcoGraph extends ManetGraph<AcoEdgeWeight> {

    public AcoGraph() {
    }

    public void add(AcoNode v) {
	super.addVertex(v);
    }

    public static void main() {

	ManetGraph<EdgeDistance> manetGraph = new ManetGraph<EdgeDistance>();

	NetworkGraphGenerator<EdgeDistance> gen1 = new NetworkGraphGenerator<EdgeDistance>(manetGraph);

	AcoGraph acoGraph = new AcoGraph();

	NetworkGraphGenerator<AcoEdgeWeight> gen2 = new NetworkGraphGenerator<AcoEdgeWeight>(acoGraph);
    }
}
