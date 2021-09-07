package de.manetmodel.evaluator;

import java.util.function.Function;

public class PropertyEvaluator<T> {
    
    Function<T, Double> propertyValue;
    Function<T, Double> propertyMinValue;
    Function<T, Double> propertyMaxValue;
    DoubleScope propertyScope;
    DoubleScope scoreScope;
    int weight;    
     
    public PropertyEvaluator(
	    Function<T, Double> propertyValue, 
	    DoubleScope propertyScope, 		
	    DoubleScope scoreScope, 
	    int weight) {
	
	this.propertyValue = propertyValue;
	this.propertyScope = propertyScope;
	this.scoreScope = scoreScope;
	this.weight = weight;
    }
    
    public PropertyEvaluator(
	    Function<T, Double> propertyValue, 
	    Function<T, Double> propertyMinValue,
	    Function<T, Double> propertyMaxValue, 
	    DoubleScope scoreScope,
	    int weight) {
	
 	this.propertyValue = propertyValue;
 	this.propertyMinValue = propertyMinValue;
 	this.propertyMaxValue = propertyMaxValue;
 	this.weight = weight;
 	this.scoreScope = scoreScope;
     }
    
    public double getScore(T linkQuality) {	
	return ((propertyValue.apply(linkQuality) / propertyScope.max() ) * scoreScope.max()) * weight;
    }
}
