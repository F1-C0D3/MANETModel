package de.manetmodel.generator;

import java.util.List;
import java.util.function.Function;

import de.jgraphlib.graph.algorithms.DijkstraShortestPath;
import de.jgraphlib.util.Log;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Log.HeaderLevel;
import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANET;
import de.manetmodel.network.Node;
import de.manetmodel.units.DataRate;

public class OverUtilzedProblemGenerator<N extends Node, L extends Link<W>, W extends LinkQuality, F extends Flow<N, L, W>> {

    MANET<N, L, W, F> manet;
    DataRate maxDataRate;
    DataRate minDataRate;

    Function<W, Double> metric;
    FlowProblemGenerator<N, L, W, F> flowProblemGenerator;
    Log log;

    public OverUtilzedProblemGenerator(MANET<N, L, W, F> manet, Function<W, Double> metric) {
	this.manet = manet;
	this.metric = metric;
	this.log = new Log();
    }
    
    public OverUtilzedProblemGenerator(MANET<N, L, W, F> manet, Function<W, Double> metric, RandomNumbers randomNumbers) {
	this.manet = manet;
	this.metric = metric;
	this.log = new Log();
    }

    public void initialize() {

	maxDataRate = new DataRate(0);

	// Find maximum dataRate
	for (L link : manet.getEdges())
	    if (link.getTransmissionRate().get() > maxDataRate.get())
		maxDataRate = link.getTransmissionRate();
	
	// Find minimum dataRate
		for (L link : manet.getEdges())
		    if (link.getTransmissionRate().get() < maxDataRate.get())
			minDataRate = link.getTransmissionRate();

	log.info(String.format(
		"Maximum DataRate of %s is %s", manet.getClass().getSimpleName(),maxDataRate.toString()));
	
	log.info(String.format(
		"Minimum DataRate of %s is %s", manet.getClass().getSimpleName(),minDataRate.toString()));
    }

    public List<F> compute(OverUtilizedProblemProperties properties,RandomNumbers randomNumbers) {

	log.infoHeader(HeaderLevel.h1, getClass().getSimpleName());
	initialize();

	flowProblemGenerator = new FlowProblemGenerator<N, L, W, F>(randomNumbers, manet.getFlowSupplier());
	DijkstraShortestPath<N, L, W> dijkstraShortestPath = new DijkstraShortestPath<N, L, W>(manet);

	int trys = 5000;
	double overUtilizationPercentage = 0d;
	OverUtilizedProblemProperties variableProperties = properties.copy();
	List<F> flowProblems=null;
	
	    // Generate flowProblem
	    flowProblems = flowProblemGenerator.generate(manet, variableProperties);

	    while (overUtilizationPercentage < properties.getOverUtilizationPercentage() && trys > 0) {

		// increase only data rates of flows
		flowProblems = flowProblemGenerator.adjustDataRates(flowProblems, variableProperties);

		for (F flowProblem : flowProblems) {

		    // Generate a shortest path from source to target
		    flowProblem.update(
			    dijkstraShortestPath.compute(flowProblem.getSource(), flowProblem.getTarget(), metric));

		    // Add & deploy flow to network
		    manet.addFlow(flowProblem);
		    manet.deployFlow(flowProblem);
		}


		// If not, increase maxDemand of flowProblemProperties and repeat
		    DataRate newMaxDemand = new DataRate(
			    variableProperties.getMaxDemand().get() + (variableProperties.getIncreaseFactor().get() / flowProblems.size()));

		    DataRate newMinDemand = new DataRate(
			    variableProperties.getMinDemand().get() + (variableProperties.getIncreaseFactor().get() / flowProblems.size()));
		    // MaxDemand of a flowProblem is not allowed to grow above the link with maximum
		    // capacity
		    variableProperties.setMaxDemand(newMaxDemand);
		    variableProperties.setMinDemand(newMinDemand);
		    trys--;
		    
			// Check if generated flowProblems over-utilize network
		    if(manet.isOverutilized())
		    overUtilizationPercentage = manet
			    .getOverUtilizationPercentageOf(manet.getOverUtilizedActiveLinks());

		manet.undeployFlows();
		manet.clearFlows();

		if (isOverutilizedWithSingleFlow(flowProblems)) {
		    overUtilizationPercentage = 0d;
		    variableProperties = properties.copy();
		    flowProblems = flowProblemGenerator.generate(manet, variableProperties);
		}
	    }
	    
	if (overUtilizationPercentage > properties.getOverUtilizationPercentage()) {
	    log.infoHeader(HeaderLevel.h1, "FlowProblems");
	    for (int i = 0; i < flowProblems.size(); i++)
		log.info(String.format("Flow %d: %d ~> %d, DataRate: %s", i, flowProblems.get(i).getSource().getID(),
			flowProblems.get(i).getTarget().getID(), flowProblems.get(i).getDataRate().toString()));
	    log.info(String.format("%s is overutilized by %.2f%% (to shortest-path's & given metric)",
		    manet.getClass().getSimpleName(), overUtilizationPercentage));
	}

	// Remove path's from the flows
	for (F flowProblem : flowProblems)
	    flowProblem.clear();

	// Return flowProblems
	return flowProblems;
    }

    boolean isOverutilizedWithSingleFlow(List<F> flowProblems) {

	boolean isOverUtilized = false;
	for (F flow : flowProblems) {
	    manet.addFlow(flow);
	    manet.deployFlow(flow);

	    if (manet.isOverutilized())
		isOverUtilized = true;

	    manet.undeployFlow(flow);
	    manet.clearFlows();

	    if (isOverUtilized)
		return true;
	}
	return false;
    }
}
