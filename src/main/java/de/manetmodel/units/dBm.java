package de.manetmodel.units;

public class dBm {
    private double value;

    public dBm(double value) {
	this.value = value;

    }

    public double get() {
	return value;
    }

    public void set(double value) {
	this.value = value;
    }

    public Watt toWatt() {
	return new Watt(convert(this.value));
    }

    private double convert(double value) {
	return (1d * (Math.pow(10, (this.value / 10d))) / 1000d);
    }
    
    @Override
    public String toString() {
	return String.format("%f dBm", this.value);
    }

}
