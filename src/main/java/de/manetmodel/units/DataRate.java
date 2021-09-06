package de.manetmodel.units;

import java.util.Comparator;

import de.manetmodel.units.DataUnit.Type;

public class DataRate implements Comparator<DataRate> {
    private long bits;

    public DataRate(double value, DataUnit.Type type) {
	this.bits = toBits(value, type);
    }

    public DataRate(long bits) {
	this.bits = bits;
    }

    public DataRate() {
    }

    protected long toBits(double value, DataUnit.Type type) {
	long factor = DataUnit.getFactor(type);
	return Math.round(value * factor);
    }

    public void set(long bits) {
	this.bits = bits;
    }

    public void set(double value, DataUnit.Type type) {
	this.bits = toBits(value, type);
    }

    public final long get() {
	return bits;
    }

    @Override
    public String toString() {
	Type type = DataUnit.getNextLowerType(bits);
	return new StringBuffer().append(bits / (double) DataUnit.getFactor(type)).append(" ").append(type).toString();
    }

    public static class DataRateRange {

	private DataRate min;
	private DataRate max;

	public DataRateRange(long min, long max, DataUnit.Type type) {
	    this.min = new DataRate(min, type);
	    this.max = new DataRate(max, type);

	}

	public DataRate min() {
	    return min;
	}

	public DataRate max() {
	    return max;
	}
    }

    @Override
    public int compare(DataRate o1, DataRate o2) {
	return (int) (o1.get() - o2.get());
    }
}
