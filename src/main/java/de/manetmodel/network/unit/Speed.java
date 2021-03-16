package de.manetmodel.network.unit;

import java.net.http.HttpClient.Version;

import org.apache.commons.lang3.Validate;

public class Speed {
    public double value;

    public Speed(double value, VelocityUnits.DistanceUnit distance, VelocityUnits.TimeUnit time) {
	this.value = toBase(value, distance, time);
    }

    public Speed() {
    }


    private double toBase(double value, VelocityUnits.DistanceUnit distanceUnit, VelocityUnits.TimeUnit timeUnit) {
	value = ((value) / (double) VelocityUnits.getTimeFactor(timeUnit))
		* ((double) VelocityUnits.getDistanceFactor(distanceUnit));

	return value;
    }

    public static class Time {
	public double value;

	public Time(VelocityUnits.TimeUnit unit, double value) {
	    this.value = toBase(unit, value);
	}

	private double toBase(VelocityUnits.TimeUnit unit, double value) {
	    return value * VelocityUnits.getTimeFactor(unit);
	}
    }

    public static class Distance {

	public double value;

	public Distance(VelocityUnits.DistanceUnit unit, double value) {
	    this.value = toBase(unit, value);
	}

	private double toBase(VelocityUnits.DistanceUnit unit, double value) {
	    return value * VelocityUnits.getDistanceFactor(unit);
	}
    }

    public static class SpeedRange {

	private Speed min;
	private Speed max;

	public SpeedRange(double min, double max, VelocityUnits.TimeUnit time, VelocityUnits.DistanceUnit distance) {
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
