package de.manetmodel.evaluator;

import de.jgraphlib.maths.Line2D;
import de.jgraphlib.maths.Point2D;

public abstract class PropertyStandardization {

    DoubleScope propertyScope;
    DoubleScope scoreScope;
    double propertyOffset;
    double weight;
    boolean invert;
    Line2D function;

    public PropertyStandardization(DoubleScope scoreScope, double weight) {
	this.scoreScope = scoreScope;
	this.weight = weight;
	this.propertyOffset = 0;
	this.invert = false;
	//System.out.println(String.format("scoreScope: min %.2f max %.2f", scoreScope.min, scoreScope.max));	
    }

    protected void setPropertyScope(DoubleScope propertyScope) {

	//System.out.println(String.format("propertyScope: min %.2f max %.2f", propertyScope.min, propertyScope.max));
	
	if (propertyScope.max < propertyScope.min) {
	    double temp = propertyScope.max;
	    propertyScope.max = propertyScope.min;
	    propertyScope.min = temp;	 
	    invert = true;
	}

	if (propertyScope.min < 0) {
	    propertyOffset = Math.abs(propertyScope.min);
	    propertyScope.min += propertyOffset;
	    propertyScope.max += propertyOffset;
	}
	
	if (propertyScope.min > 0) {
	    propertyOffset = -(propertyScope.min);
	    propertyScope.min += propertyOffset;
	    propertyScope.max += propertyOffset;
	}
	
	this.propertyScope = propertyScope;
	
	if(invert)
	    function = new Line2D(new Point2D(/*x1*/propertyScope.min, /*y1*/scoreScope.max), new Point2D(/*x2*/propertyScope.max, /*y2*/scoreScope.min));    	
	else
	    function = new Line2D(new Point2D(/*x1*/propertyScope.min, /*y1*/scoreScope.min), new Point2D(/*x2*/propertyScope.max, /*y2*/scoreScope.max));

	//System.out.println(String.format("propertyScope: min %.2f max %.2f", propertyScope.min, propertyScope.max));
    }

    public DoubleScope getScoreScope() {
	return this.scoreScope;
    }

    protected double getScore(double propertyValue) {
		
	//System.out.println(String.format("propertyValue: %.2f", propertyValue));
		
	return function.getY(propertyValue + propertyOffset);
    }
}
