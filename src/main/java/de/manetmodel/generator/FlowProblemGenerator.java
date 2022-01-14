package de.manetmodel.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import de.jgraphlib.generator.PathProblemGenerator;
import de.jgraphlib.graph.WeightedGraph;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.network.Flow;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.units.DataRate;

public class FlowProblemGenerator<N extends Vertex<Position2D>, L extends WeightedEdge<W>, W extends LinkQuality, F extends Flow<N, L, W>>
	extends PathProblemGenerator<N, L, W, F> {

    public FlowProblemGenerator(RandomNumbers randomNumbers, Supplier<F> flowSupplier) {
	super(randomNumbers, flowSupplier);
    }

    public List<F> generate(WeightedGraph<N, ?, L, W, ?> graph, FlowProblemProperties problemProperties) {

	List<F> flowProblems = new ArrayList<F>();

	if (problemProperties.isUniqueSourceDestination() ) {

	    List<Integer> nodeIdExclusionList = new ArrayList<Integer>();
	    
	    for (int i = 0; i < problemProperties.getPathCount(); i++) {
		
		int randomSourceNodeID = randomNumbers.getRandomNotInE(0, graph.getVertices().size() - 1, nodeIdExclusionList);
		nodeIdExclusionList.add(randomSourceNodeID);

		int randomTargetNodeID = randomNumbers.getRandomNotInE(0, graph.getVertices().size() - 1, nodeIdExclusionList);
		nodeIdExclusionList.add(randomTargetNodeID);

		F flow = pathSupplier.get();
		flow.setSource(graph.getVertex(randomSourceNodeID));
		flow.setTarget(graph.getVertex(randomTargetNodeID));
		flowProblems.add(flow);
	    }
	    
	} else
	    flowProblems = super.generate(graph, problemProperties);

	return adjustDataRates(flowProblems, problemProperties);

    }
    
    
    public List<F> adjustDataRates(List<F> flowProblems, FlowProblemProperties problemProperties){
	for (F flowProblem : flowProblems)
	    flowProblem.setDataRate(new DataRate((long) randomNumbers.getRandom(problemProperties.getMinDemand().get(), problemProperties.getMaxDemand().get())));
	return flowProblems;
    }

    public void printFlowProblems(List<F> flowProblems) {

	System.out.println("FlowProblems:");

	for (int i = 0; i < flowProblems.size(); i++) {
	    System.out.println(
		    String.format("source %d, target %d, dataRate: %d", flowProblems.get(i).getSource().getID(),
			    flowProblems.get(i).getTarget().getID(), flowProblems.get(i).getDataRate().get()));
	}
    }
}
