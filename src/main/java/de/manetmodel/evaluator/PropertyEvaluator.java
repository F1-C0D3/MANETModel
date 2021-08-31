package de.manetmodel.evaluator;

import java.util.function.Function;

public class PropertyEvaluator<W> {
    
    Function<W, Double> propertyValue;
    Function<W, Double> propertyMinValue;
    Function<W, Double> propertyMaxValue;
    DoubleScope propertyScope;
    DoubleScope scoreScope;
    int weight;    
     
    public PropertyEvaluator(
	    Function<W, Double> propertyValue, 
	    DoubleScope propertyScope, 		/* property scope is static and depends on global environment */
	    DoubleScope scoreScope, 
	    int weight) {
	
	this.propertyValue = propertyValue;
	this.propertyScope = propertyScope;
	this.scoreScope = scoreScope;
	this.weight = weight;
    }
    
    public PropertyEvaluator(
	    Function<W, Double> propertyValue, 
	    Function<W, Double> propertyMinValue, /*property scope depends on (a dynamic state) of the object itself*/
	    Function<W, Double> propertyMaxValue, 
	    DoubleScope scoreScope,
	    int weight) {
	
 	this.propertyValue = propertyValue;
 	this.propertyMinValue = propertyMinValue;
 	this.propertyMaxValue = propertyMaxValue;
 	this.weight = weight;
 	this.scoreScope = scoreScope;
     }
    
    public double getScore(W linkQuality) {	
	return ((propertyValue.apply(linkQuality) / propertyScope.max() ) * scoreScope.max()) * weight;
    }
}
