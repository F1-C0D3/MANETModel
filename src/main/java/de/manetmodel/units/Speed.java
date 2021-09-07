package de.manetmodel.units;

public class Speed {
    public double value;

    public Speed(double value, Unit.Distance distance, Unit.TimeSteps time) {
	this.value = toBase(value, distance, time);
    }

    public Speed(double value) {
	this.value = value;
    }

    private double toBase(double value, Unit.Distance distanceUnit, Unit.TimeSteps timeUnit) {
	value = ((value) / (double) Unit.getTimeFactor(timeUnit)) * ((double) Unit.getDistanceFactor(distanceUnit));

	return value;
    }

    public double convertTo(Unit.Distance distance, Unit.TimeSteps timeUnit) {
	double newValue = value;

	int distanceFactor = Unit.getDistanceFactor(distance);
	int timeFactor = Unit.getTimeFactor(timeUnit);

	return (newValue / distanceFactor * timeFactor);

    }

    @Override
    public String toString() {
	return String.format("%f kilometer/sec", convertTo(Unit.Distance.kilometer, Unit.TimeSteps.hour));
    }

    public static class SpeedRange {

	private Speed min;
	private Speed max;

	public SpeedRange(double min, double max, Unit.TimeSteps time, Unit.Distance distance) {
	    this.min = new Speed(min, distance, time);
	    this.max = new Speed(max, distance, time);

	}

	public Speed min() {
	    return min;
	}

	public Speed max() {
	    return max;
	}
    }

}
