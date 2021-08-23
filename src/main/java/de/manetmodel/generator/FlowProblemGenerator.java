package de.manetmodel.generator;

import java.util.List;
import java.util.function.Supplier;

import de.jgraphlib.graph.WeightedGraph;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.graph.generator.PathProblemGenerator;
import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.network.Flow;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.unit.DataRate;

public class FlowProblemGenerator<N extends Vertex<Position2D>, L extends WeightedEdge<W>, W extends LinkQuality, F extends Flow<N,L,W>> extends PathProblemGenerator<N, L, W ,F> {
    
    public FlowProblemGenerator(RandomNumbers randomNumbers, Supplier<F> flowSupplier) {
	super(randomNumbers, flowSupplier);
    }
    
    public List<F> generate(WeightedGraph<N, ?, L, W, ?> graph, FlowProblemProperties problemProperties) {
	
	List<F> flowProblems = super.generate(graph, problemProperties);
	
	for(F flowProblem : flowProblems) 
	    flowProblem.setDataRate(new DataRate((long) randomNumbers.getRandom(problemProperties.minDemand.get(), problemProperties.maxDemand.get())));
	    
	return flowProblems;
    }  
}
