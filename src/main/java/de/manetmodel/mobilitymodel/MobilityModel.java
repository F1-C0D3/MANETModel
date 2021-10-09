package de.manetmodel.mobilitymodel;

import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.units.Speed;
import de.manetmodel.units.Time;
import de.manetmodel.units.Unit;
import de.manetmodel.units.Speed.SpeedRange;

public abstract class MobilityModel {

    private final int ticks;
    private final Time recordDuration;
    private final Time timeStamp;
   
    protected SpeedRange speedRange;
    protected RandomNumbers random;

    public MobilityModel(RandomNumbers random, SpeedRange speedRange, Time recordDuration, int ticks) {
	this.random = random;
	this.speedRange = speedRange;
	if (ticks == 0) {
	    this.ticks = 5;
	} else {
	    this.ticks = ticks;
	}

	if (recordDuration == null || recordDuration.value == 0) {
	    this.recordDuration = new Time(Unit.TimeSteps.second, 30);
	} else {
	    this.recordDuration = recordDuration;
	}
	this.timeStamp = new Time(Unit.TimeSteps.milliseconds, recordDuration.getMillis() / (long) ticks);

    }

    public MobilityModel(RandomNumbers random, SpeedRange speedRange, Time recordDuration) {
	this.ticks = 3;
	this.random = random;
	this.speedRange = speedRange;

	if (recordDuration == null || recordDuration.value == 0) {
	    this.recordDuration = new Time(Unit.TimeSteps.second, 30);
	} else {
	    this.recordDuration = recordDuration;
	}

	this.timeStamp = new Time(Unit.TimeSteps.second, recordDuration.value / (long) ticks);
    }

    public MobilityModel(RandomNumbers random, SpeedRange speedRange) {
	this.ticks = 3;
	this.recordDuration = new Time(Unit.TimeSteps.second, 30);
	this.random = random;
	this.speedRange = speedRange;
	this.timeStamp = new Time(Unit.TimeSteps.second, recordDuration.value / (long) ticks);
    }

    public SpeedRange getSpeedRange() {
	return this.speedRange;
    }

    public int getTicks() {
	return ticks;
    }

    public RandomNumbers getRandom() {
	return random;
    }

    public void setRandom(RandomNumbers random) {
	this.random = random;
    }

    
    public Time getTimeStamp() {
        return timeStamp;
    }

    public abstract Speed initializeSpeed();

    public abstract MovementPattern computeNextMovementPattern(MovementPattern prevPattern);
    
    
}
