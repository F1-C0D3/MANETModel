package de.manetmodel.mobility;

import java.util.List;

import de.jgraphlib.graph.Position2D;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.util.RandomNumbers;

public abstract class MobilityModel {

    public int segments;
    public RandomNumbers random;
    public DoubleRange speedRange;

    public MobilityModel(RandomNumbers random, DoubleRange speedRange, int segments) {
	this.random = random;
	this.speedRange = speedRange;
	this.segments = segments;
	if (segments == 0)
	    this.segments = 3;
    }

    public MobilityModel(RandomNumbers random, DoubleRange speedRange) {
	this.random = random;
	this.segments = 3;
	this.speedRange = speedRange;
    }

    public int getSegments() {
	return segments;
    }

    public void setSegments(int segments) {
	this.segments = segments;
    }

    public RandomNumbers getRandom() {
	return random;
    }

    public void setRandom(RandomNumbers random) {
	this.random = random;
    }

    public abstract MovementPattern computeMovementPattern(Position2D prevPosition, double revAngle, double prevSpeed);
}
