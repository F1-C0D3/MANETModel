package de.manetmodel.network.unit;

public class Timer {
    boolean started = false;
    private Time startTime;
    private Time endTime;
    private Time duration;

    public Timer() {
    }

    public void start() {
	if (!started)
	    this.startTime = new Time(System.currentTimeMillis());
	started = true;
    }

    public void stop() {
	this.endTime = new Time(System.currentTimeMillis());
	this.duration = new Time(this.endTime.getMillis() - startTime.getMillis());
    }

    public Time getTime() {
	return new Time(System.currentTimeMillis() - startTime.getMillis());
    }

    public Time getTime(long milliSeconds) {
	return new Time(milliSeconds - this.startTime.getMillis());
    }

    public Time getDuration() {
	return this.duration;
    }
}
