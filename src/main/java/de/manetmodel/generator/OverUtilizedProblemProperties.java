package de.manetmodel.generator;

import de.manetmodel.units.DataRate;

public class OverUtilizedProblemProperties extends FlowProblemProperties {

    private int overUtilizationPercentage;
    private DataRate increaseFactor;

    public OverUtilizedProblemProperties(int pathCount, int minLength, int maxLength,
	    DataRate minDemand, DataRate maxDemand, boolean uniqueSourceDestination, int overUtilizedPercentage, DataRate increaseFactor) {
	
	super(pathCount, minLength, maxLength, minDemand, maxDemand, uniqueSourceDestination);
	
	this.overUtilizationPercentage = overUtilizedPercentage;
	this.increaseFactor = increaseFactor;
    }

    public OverUtilizedProblemProperties copy() {

	return new OverUtilizedProblemProperties(this.getPathCount(),this.getMinLength(),this.getMaxLength(),
		new DataRate(this.getMinDemand().get()),
		 new DataRate(this.getMaxDemand().get()), this.isUniqueSourceDestination(),
		this.overUtilizationPercentage, new DataRate(this.increaseFactor.get()));

    }

    public int getOverUtilizationPercentage() {
        return overUtilizationPercentage;
    }

    public void setOverUtilizationPercentage(int overUtilizationPercentage) {
        this.overUtilizationPercentage = overUtilizationPercentage;
    }

    public DataRate getIncreaseFactor() {
        return increaseFactor;
    }

    public void setIncreaseFactor(DataRate increaseFactor) {
        this.increaseFactor = increaseFactor;
    }
    
    

}
