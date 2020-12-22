package de.manetmodel.network.unit;

public class DataRate {
    private long bits;

    public DataRate(double value, Unit.Type type) {
	this.bits = toBits(value, type);
    }

    public DataRate(long bits) {
	this.bits = bits;
    }

    public DataRate() {

    }

    protected long toBits(double value, Unit.Type type) {
	long factor = Unit.getFactor(type);
	return (long) Math.round(value * factor);
    }

    public final long get() {
	return bits;
    }

}
