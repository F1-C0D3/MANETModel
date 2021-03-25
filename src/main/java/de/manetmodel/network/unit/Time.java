package de.manetmodel.network.unit;

public class Time {
    public long value;

    public Time(Unit.Time unit, long value) {
	this.value = toBase(unit, value);
    }

    public Time(long value) {
	this.value = value;
    }

    public Time() {
	this.value = 0l;
    }

    public void set(long milliseconds) {
	this.value = milliseconds;
    }

    public long getMillis() {
	return this.value;
    }

    private long toBase(Unit.Time unit, long value) {
	return value * Unit.getTimeFactor(unit);
    }

    @Override
    public String toString() {
	long milliSeconds = value % 1000;
	long seconds = (value / 1000) % 60;
	long minutes = (value / (1000 * 60)) % 60;
	long hours = (value / (1000 * 60 * 60)) % 24;
	return String.format("%02d:%02d:%02d.%d", hours, minutes, seconds, milliSeconds);
    }
}