package de.manetmodel.evaluator;

public class DoubleScope {

    public double min;
    public double max;
    
    public DoubleScope(double min, double max) {
	this.min = min;
	this.max = max;
    }
    
    public double min() {
	return this.min;
    }
    
    public double max() {
	return this.max;
    }  
}
