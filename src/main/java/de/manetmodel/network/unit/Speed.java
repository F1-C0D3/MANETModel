package de.manetmodel.network.unit;

import java.net.http.HttpClient.Version;

import org.apache.commons.lang3.Validate;

public class Speed {
    public double value;

    public Speed(double value, Unit.Distance distance, Unit.TimeSteps time) {
	this.value = toBase(value, distance, time);
    }

    public Speed() {
    }

    private double toBase(double value, Unit.Distance distanceUnit, Unit.TimeSteps timeUnit) {
	value = ((value) / (double) Unit.getTimeFactor(timeUnit)) * ((double) Unit.getDistanceFactor(distanceUnit));

	return value;
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
