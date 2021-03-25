package de.manetmodel.network.unit;

public class Distance {

    public double value;

    public Distance(Unit.Distance unit, double value) {
	this.value = toBase(unit, value);
    }

    private double toBase(Unit.Distance unit, double value) {
	return value * Unit.getDistanceFactor(unit);
    }
}