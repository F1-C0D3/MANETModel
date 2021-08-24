package de.manetmodel.generator;

import de.jgraphlib.graph.generator.PathProblemProperties;
import de.manetmodel.network.unit.DataRate;

public class FlowProblemProperties extends PathProblemProperties {
    public DataRate minDemand;
    public DataRate maxDemand;  
}
