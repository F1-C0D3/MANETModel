package de.manetmodel.generator;

import de.jgraphlib.graph.generator.PathProblemProperties;
import de.manetmodel.units.DataRate;

public class FlowProblemProperties extends PathProblemProperties {
    public DataRate minDemand;
    public DataRate maxDemand;
    public boolean uniqueSourceDestination;
}
