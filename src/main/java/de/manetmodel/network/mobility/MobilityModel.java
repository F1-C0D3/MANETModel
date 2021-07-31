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

    public final int segments;
    public final Time recordDuration;
    public final Time timeStamp;
    public RandomNumbers random;
    public SpeedRange speedRange;

    public MobilityModel(RandomNumbers random, SpeedRange speedRange, Time recordDuration, int segments) {
	this.random = random;
	this.speedRange = speedRange;
	if (segments == 0) {
	    this.segments = 3;
	} else {
	    this.segments = segments;
	}

	if (recordDuration == null || recordDuration.value == 0) {
	    this.recordDuration = new Time(Unit.Time.second, 30);
	} else {
	    this.recordDuration = recordDuration;
	}
	this.timeStamp = new Time(Unit.Time.second, recordDuration.getMillis() / (long) segments);

    }

    public MobilityModel(RandomNumbers random, SpeedRange speedRange, Time recordDuration) {
	this.segments = 3;
	this.random = random;
	this.speedRange = speedRange;

	if (recordDuration == null || recordDuration.value == 0) {
	    this.recordDuration = new Time(Unit.Time.second, 30);
	} else {
	    this.recordDuration = recordDuration;
	}

	this.timeStamp = new Time(Unit.Time.second, recordDuration.value / (long) segments);
    }

    public MobilityModel(RandomNumbers random, SpeedRange speedRange) {
	this.segments = 3;
	this.recordDuration = new Time(Unit.Time.second, 30);
	this.random = random;
	this.speedRange = speedRange;
	this.timeStamp = new Time(Unit.Time.second, recordDuration.value / (long) segments);
    }

    public int getSegments() {
	return segments;
    }

    public RandomNumbers getRandom() {
	return random;
    }

    public void setRandom(RandomNumbers random) {
	this.random = random;
    }

    public abstract MovementPattern computeNextMovementPattern(MovementPattern prevPattern);
}
