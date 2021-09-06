package de.manetmodel.units;

public class Watt {
    private double value;

    public Watt(double value) {
	this.value = value;
    }

    public void set(double value) {
	this.value = value;
    }

    public double get() {
	return this.value;
    }

    private double convert(double value) {
	return 10d * (Math.log10(1000d * this.value * 1d));
    }

    public dBm todBm() {
	return new dBm(convert(this.value));
    }
    
    @Override
    public String toString() {
        return String.format("%f Watt", this.value);
    }
}
