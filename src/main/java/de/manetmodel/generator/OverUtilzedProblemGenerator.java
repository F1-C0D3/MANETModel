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
    Function<W, Double> metric;
    FlowProblemGenerator<N, L, W, F> flowProblemGenerator;
    Log log;

    public OverUtilzedProblemGenerator(MANET<N, L, W, F> manet, Function<W, Double> metric) {
	this.manet = manet;
	this.metric = metric;
	this.log = new Log();
    }

    public List<F> compute(OverUtilizedProblemProperties properties) {
	return compute(properties, new RandomNumbers());
    }
    
    public void initialize() {
	
	maxDataRate = new DataRate(0);
	
	// Find maximum dataRate
	for(L link : manet.getEdges()) 
	    if(link.getTransmissionRate().get() > maxDataRate.get())
		maxDataRate = link.getTransmissionRate();
	
	log.info(String.format("Maximum DataRate of %s is %s", manet.getClass().getSimpleName(), maxDataRate.toString()));
    }

    public List<F> compute(OverUtilizedProblemProperties properties, RandomNumbers randomNumbers) {

	log.infoHeader(HeaderLevel.h1, getClass().getSimpleName());
	initialize();
	
	flowProblemGenerator = new FlowProblemGenerator<N, L, W, F>(randomNumbers, manet.getFlowSupplier());
	DijkstraShortestPath<N, L, W> dijkstraShortestPath = new DijkstraShortestPath<N, L, W>(manet);

	int trys = 1000;
	List<F> flowProblems = null;
	double overUtilizationPercentage = 0;

	while (overUtilizationPercentage < properties.overUtilizationPercentage && trys > 0) {

	    // Generate flowProblems
	    flowProblems = flowProblemGenerator.generate(manet, properties);

	    for (F flowProblem : flowProblems) {
		
		// Generate a shortest path from source to target
		flowProblem.update(dijkstraShortestPath.compute(flowProblem.getSource(), flowProblem.getTarget(), metric));
		
		// Add & deploy flow to network
		manet.addFlow(flowProblem);
		manet.deployFlow(flowProblem);
	    }

	    // Check if generated flowProblems over-utilize network
	    if (manet.getOverUtilizedActiveLinks().size() > 0) {
		overUtilizationPercentage = manet.getOverUtilizationPercentageOf(manet.getOverUtilizedActiveLinks());  
	    }
	    // If not, increase maxDemand of flowProblemProperties and repeat
	    else {
		DataRate newMaxDemand = new DataRate(properties.maxDemand.get() + (properties.maxDemand.get() * 1/20));		
		// MaxDemand of a flowProblem is not allowed to grow above the link with maximum capacity
		if(newMaxDemand.get() > maxDataRate.get()) return null;	
		properties.maxDemand = newMaxDemand;
		trys++;
	    }    
	    
	    manet.undeployFlows();
	    manet.clearFlows();
	}
		
	if(overUtilizationPercentage > properties.overUtilizationPercentage) {	
	    log.infoHeader(HeaderLevel.h1, "FlowProblems");
	    for (int i = 0; i < flowProblems.size(); i++) 
		log.info(String.format("Flow %d: %d ~> %d, DataRate: %s", 
			i,
			flowProblems.get(i).getSource().getID(),
			flowProblems.get(i).getTarget().getID(), 
			flowProblems.get(i).getDataRate().toString()));	 
	    log.info(String.format("%s is overutilized by %.2f%% (to shortest-path's & given metric)", manet.getClass().getSimpleName(), overUtilizationPercentage));
	}
	
	// Remove path's from the flows
	for (F flowProblem : flowProblems) flowProblem.clear();
	
	// Return flowProblems
	return flowProblems;
    }
}
