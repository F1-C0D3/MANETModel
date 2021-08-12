package de.manetmodel.network;

import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.manetmodel.network.unit.DataRate;

public class FlowObjective <N extends Vertex<Position2D>> {

    private N source;
    private N sink;
    private DataRate demand;
    
    public FlowObjective(N source, N sink, DataRate demand) {
	this.source = source;
	this.sink = sink;
	this.demand = demand;
    }
    
    public N getSource() {
	return source;
    }
    
    public N getSink() {
	return sink;
    }
    
    public DataRate getDemand() {
	return demand;
    }
    
}
