package de.manetmodel.mobilitymodel;

import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.units.Speed;
import de.manetmodel.units.Time;
import de.manetmodel.units.Unit;
import de.manetmodel.units.Speed.SpeedRange;

public abstract class MobilityModel {

    private final int ticks;
    private final Time tickDuration;
   
    protected SpeedRange speedRange;
    protected RandomNumbers random;

    public MobilityModel(RandomNumbers random, SpeedRange speedRange, Time tickDuration, int ticks) {
	this.random = random;
	this.speedRange = speedRange;
	if (ticks == 0) {
	    this.ticks = 5;
	} else {
	    this.ticks = ticks;
	}

	if (tickDuration == null || tickDuration.value == 0) {
	    this.tickDuration = new Time(Unit.TimeSteps.second, 30);
	} else {
	    this.tickDuration = tickDuration;
	}

    }

    public MobilityModel(RandomNumbers random, SpeedRange speedRange, Time tickDuration) {
	this.ticks = 3;
	this.random = random;
	this.speedRange = speedRange;

	if (tickDuration == null || tickDuration.value == 0) {
	    this.tickDuration = new Time(Unit.TimeSteps.second, 1l);
	} else {
	    this.tickDuration = tickDuration;
	}

    }

    public MobilityModel(RandomNumbers random, SpeedRange speedRange) {
	this.ticks = 3;
	this.tickDuration = new Time(Unit.TimeSteps.second, 1l);
	this.random = random;
	this.speedRange = speedRange;
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

    
    public Time getTickDuration() {
        return tickDuration;
    }

    public abstract Speed initializeSpeed();

    public abstract MovementPattern computeNextMovementPattern(MovementPattern prevPattern);
    
    
}
