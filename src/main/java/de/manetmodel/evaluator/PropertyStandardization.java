package de.manetmodel.evaluator;

public abstract class PropertyStandardization {

    DoubleScope propertyScope;
    DoubleScope scoreScope;
    double propertyOffset;
    double weight;

    public PropertyStandardization(DoubleScope scoreScope, double weight) {
	this.scoreScope = scoreScope;
	this.weight = weight;
	this.propertyOffset = 0;
    }

    protected void setPropertyScope(DoubleScope propertyScope) {

	//System.out.println(String.format("propertyScope.min = %.2f; .max= %.2f", propertyScope.min, propertyScope.max));
	
	// Swap
	if (propertyScope.max < propertyScope.min) {
	    double temp = propertyScope.max;
	    propertyScope.max = propertyScope.min;
	    propertyScope.min = temp;
	}

	// Add offset to normalize in range [0,_]
	if (propertyScope.min < 0) {
	    propertyOffset = Math.abs(propertyScope.min);
	    propertyScope.min += propertyOffset;
	    propertyScope.max += propertyOffset;
	}

	this.propertyScope = propertyScope;
	
	//System.out.println(String.format("propertyScope.min = %.2f; .max= %.2f", propertyScope.min, propertyScope.max));
    }

    public DoubleScope getScoreScope() {
	return this.scoreScope;
    }

    protected double getScore(double propertyValue) {
	return (((propertyValue + propertyOffset) / propertyScope.max()) * scoreScope.max()) * weight;
    }
}
