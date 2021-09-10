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
    Function<W, Double> metric;
    FlowProblemGenerator<N, L, W, F> flowProblemGenerator;

    public OverUtilzedProblemGenerator(MANET<N, L, W, F> manet, Function<W, Double> metric) {
	this.manet = manet;
	this.metric = metric;
    }

    public List<F> compute(OverUtilizedProblemProperties properties) {
	return compute(properties, new RandomNumbers());
    }

    public List<F> compute(OverUtilizedProblemProperties properties, RandomNumbers randomNumbers) {

	int trys = 0;
	boolean invalidSoltuion = false;

	flowProblemGenerator = new FlowProblemGenerator<N, L, W, F>(randomNumbers, manet.getFlowSupplier());
	DijkstraShortestPath<N, L, W> dijkstraShortestPath = new DijkstraShortestPath<N, L, W>(manet);

	double overUtilizationPercentage = 0;

	List<F> flowProblems = null;

	while (overUtilizationPercentage < properties.overUtilizationPercentage && trys < 100) {

	    flowProblems = flowProblemGenerator.generate(manet, properties);

	    for (F flowProblem : flowProblems) {
		flowProblem
			.update(dijkstraShortestPath.compute(flowProblem.getSource(), flowProblem.getTarget(), metric));

		manet.addFlow(flowProblem);
		manet.deployFlow(flowProblem);
		// No single flow can be over-utilize the network
		if (manet.isOverutilized()) {
		    manet.undeployFlow(flowProblem);
		    invalidSoltuion = true;
		}
		manet.undeployFlow(flowProblem);
	    }

	    if (!invalidSoltuion) {

		manet.deployFlows(flowProblems);

		if (manet.getOverUtilizedActiveLinks().size() > 0) {
		    overUtilizationPercentage = manet
			    .getOverUtilizationPercentageOf(manet.getOverUtilizedActiveLinks());
		    System.out.println(overUtilizationPercentage);

		} else {
		    manet.undeployFlows();
		    manet.clearFlows();
		    properties.maxDemand = new DataRate(properties.maxDemand.get() + properties.maxDemand.get() / 10);
		    trys++;
		}
	    }
	    
	   invalidSoltuion=false;
	}

	for (F flowProblem : flowProblems) {
	    flowProblem.clear();
	}
	manet.undeployFlows();
	manet.clearFlows();
	return flowProblems;
    }
}
