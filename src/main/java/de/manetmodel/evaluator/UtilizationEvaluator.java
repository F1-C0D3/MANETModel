package de.manetmodel.evaluator;

import de.manetmodel.network.Flow;
import de.manetmodel.network.MANET;
import de.manetmodel.units.DataRate;

public class UtilizationEvaluator extends LinearStandardization {

    private DataRate maxDemand;
    private DataRate minDemand;
    
    public UtilizationEvaluator(DoubleScope scoreScope, double weight, MANET<?,?,?,?> manet) {
	
	super(scoreScope, weight);
	
	System.out.println(String.format("scoreScope: min %.2f max %.2f", scoreScope.min, scoreScope.max));
		
	long min = 0, max = 0;
	
	for(Flow<?,?,?> flow : manet.getFlows()) {
	    max += flow.getDataRate().get();
	    
	    if(flow.getDataRate().get() < min)
		min = flow.getDataRate().get();
	}
	
	maxDemand = new DataRate(max);
	minDemand = new DataRate(min);
	
	if(minDemand.equals(maxDemand))
	    setPropertyScope(new DoubleScope(maxDemand.get(), 0));	
	else
	    setPropertyScope(new DoubleScope(maxDemand.get(), minDemand.get()));	
    
	
	System.out.println(String.format("propertyScope: min %.2f max %.2f", getPropertyScope().min, getPropertyScope().max));
    }
    
    public DataRate getMaxDemand(){
	return maxDemand;
    }
    
    public DataRate getMinDemand(){
	return minDemand;
    }
}
