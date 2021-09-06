package de.manetmodel.generator;

import java.util.List;
import java.util.function.Function;

import de.jgraphlib.graph.algorithms.DijkstraShortestPath;
import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANET;
import de.manetmodel.network.Node;
import de.manetmodel.units.DataRate;

public class OverUtilzedProblemGenerator<N extends Node, L extends Link<W>, W extends LinkQuality, F extends Flow<N, L, W>> {

    MANET<N, L, W, F> manet;
    Function<L, Double> metric;
    FlowProblemGenerator<N, L, W, F> flowProblemGenerator;
    RandomNumbers randomNumbers = new RandomNumbers();

    public OverUtilzedProblemGenerator(MANET<N, L, W, F> manet, Function<L, Double> metric) {
	this.manet = manet;
	this.metric = metric;
    }

    public List<F> compute(OverUtilizedProblemProperties properties) {

	int trys = 0;
	
	flowProblemGenerator = new FlowProblemGenerator<N, L, W, F>(randomNumbers, manet.getFlowSupplier());
	DijkstraShortestPath<N, L, W> dijkstraShortestPath = new DijkstraShortestPath<N, L, W>(manet);

	double overUtilizationPercentage = 0;

	List<F> flowProblems = null;
	
	while (overUtilizationPercentage < properties.overUtilizationPercentage && trys < 100) {

	    flowProblems = flowProblemGenerator.generate(manet, properties);

	    for (F flowProblem : flowProblems) {
		flowProblem.update(dijkstraShortestPath.compute(flowProblem.getSource(), flowProblem.getTarget(), metric));
		manet.addFlow(flowProblem);
		manet.deployFlow(flowProblem);
	    }

	    if (manet.getOverUtilizedActiveLinks().size() > 0) {
		overUtilizationPercentage = manet.getOverUtilizationPercentageOf(manet.getOverUtilizedActiveLinks());
		System.out.println(overUtilizationPercentage);	
	    }
	    else {
		manet.undeployFlows();
		manet.clearFlows();
		properties.maxDemand = new DataRate(properties.maxDemand.get() + properties.maxDemand.get() / 10);
		trys++;
	    }
	}
	
	return flowProblems;
    }
}
