package de.manetmodel.generator;

import de.jgraphlib.generator.PathProblemProperties;
import de.manetmodel.units.DataRate;

public class FlowProblemProperties extends PathProblemProperties {
    
    private  DataRate minDemand;
    private  DataRate maxDemand;
    private  boolean uniqueSourceDestination;
    
    public FlowProblemProperties(int pathCount, int minLength, int maxLength,
	    DataRate minDemand, DataRate maxDemand, boolean uniqueSourceDestination) {
	
	super(pathCount,minLength,maxLength);
	
	this.minDemand = minDemand;
	this.maxDemand = maxDemand;
	this.uniqueSourceDestination = uniqueSourceDestination;	
    }

    public DataRate getMinDemand() {
        return minDemand;
    }

    public void setMinDemand(DataRate minDemand) {
        this.minDemand = minDemand;
    }

    public DataRate getMaxDemand() {
        return maxDemand;
    }

    public void setMaxDemand(DataRate maxDemand) {
        this.maxDemand = maxDemand;
    }

    public boolean isUniqueSourceDestination() {
        return uniqueSourceDestination;
    }

    public void setUniqueSourceDestination(boolean uniqueSourceDestination) {
        this.uniqueSourceDestination = uniqueSourceDestination;
    }
}
