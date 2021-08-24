package de.manetmodel.generator;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import de.jgraphlib.graph.algorithms.DijkstraShortestPath;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANET;
import de.manetmodel.network.Node;

public class OverUtilzedProblemGenerator <N extends Node, L extends Link<W>, W extends LinkQuality, F extends Flow<N,L,W>> {

    MANET<N,L,W,F> manet;
    Function<W,Double> metric;
    FlowProblemGenerator<N,L,W,F> flowProblemGenerator;
    RandomNumbers randomNumbers = new RandomNumbers();
    
    public OverUtilzedProblemGenerator(MANET<N,L,W,F> manet, Function<W,Double> metric) {
	this.manet = manet;
	this.metric = metric;
    }
    
    public List<F> compute(OverUtilizedProblemProperties properties, Duration maxDuration) {
			
	flowProblemGenerator = new FlowProblemGenerator<N,L,W,F>(randomNumbers, manet.getFlowSupplier());
		
	long startTime = System.currentTimeMillis();
	long endTime = startTime + maxDuration.toMillis();	
	
	while(System.currentTimeMillis() < endTime) {
	    	    	    
	    List<F> flowProblems = flowProblemGenerator.generate(manet, properties);
	    flowProblemGenerator.printFlowProblems(flowProblems);

	    DijkstraShortestPath<N,L,W> dijkstraShortestPath = new DijkstraShortestPath<N,L,W>(manet);
	    
	    for(F flowProblem : flowProblems) {		
		Path<N,L,W> shortestPath = dijkstraShortestPath.compute(flowProblem.getSource(), flowProblem.getTarget(), metric);		
		flowProblem.update(shortestPath);
		manet.addFlow(flowProblem);
		System.out.println(flowProblem);
	    	manet.deployFlow(flowProblem);	
	    }	 
	   		    
	    if(manet.getOverUtilizedActiveLinks().size() > 0) {
		System.out.println(manet.getOverUtilizedActiveLinks().size());
		System.out.println(manet.getOverUtilizedActiveLinks());
		System.out.println(manet.getOverUtilizationPercentageOf(manet.getOverUtilizedActiveLinks()));
	    }
	    
	    if(manet.getOverUtilizationPercentageOf(manet.getActiveUtilizedLinks()) >= properties.overUtilizationPercentage)
		return flowProblems;	
	        
	    manet.undeployFlows();
	    manet.clearFlows();
	    
	    // make it harder
	    properties.maxLength += 1;
	}
	
	
	return null;		
    }
}
