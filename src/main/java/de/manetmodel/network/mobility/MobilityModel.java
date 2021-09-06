package de.manetmodel.network.mobility;

import java.util.List;

import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.network.unit.DataUnit;
import de.manetmodel.network.unit.Speed;
import de.manetmodel.network.unit.Speed.SpeedRange;
import de.manetmodel.network.unit.Time;
import de.manetmodel.network.unit.Unit;

public abstract class MobilityModel {

    public final int ticks;
    public final Time recordDuration;
    public final Time timeStamp;
    public RandomNumbers random;
    public SpeedRange speedRange;

    public MobilityModel(RandomNumbers random, SpeedRange speedRange, Time recordDuration, int ticks) {
	this.random = random;
	this.speedRange = speedRange;
	if (ticks == 0) {
	    this.ticks = 3;
	} else {
	    this.ticks = ticks;
	}

	if (recordDuration == null || recordDuration.value == 0) {
	    this.recordDuration = new Time(Unit.TimeSteps.second, 30);
	} else {
	    this.recordDuration = recordDuration;
	}
	this.timeStamp = new Time(Unit.TimeSteps.second, recordDuration.getMillis() / (long) ticks);

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

    public abstract MovementPattern computeNextMovementPattern(MovementPattern prevPattern);
}
