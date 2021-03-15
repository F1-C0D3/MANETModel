package de.manetmodel.mobility;

import java.util.ArrayList;
import java.util.List;

import de.jgraphlib.graph.Position2D;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Tuple;

public class PedestrianMobilityModel extends MobilityModel {

    DoubleRange speed;
    double deviation;
    DoubleRange minMaxSpeed;

    public PedestrianMobilityModel(RandomNumbers random, DoubleRange speedRange, double deviation) {
	super(random, speedRange);
	this.deviation = deviation;
	minMaxSpeed = new DoubleRange(2d, 40d);
    }

    public PedestrianMobilityModel(RandomNumbers random, DoubleRange speedRange, double deviation, int segments) {
	super(random, speedRange, segments);
	this.deviation = deviation;
	minMaxSpeed = new DoubleRange(2d, 40d);
    }

    @Override
    public MovementPattern computeMovementPattern(Position2D prevPosition, double prevAngle, double prevSpeed) {

	double newSpeed = 0d;

	if (prevSpeed - deviation < minMaxSpeed.min) {
	    newSpeed = deviation + prevSpeed;
	} else if (prevSpeed + deviation > minMaxSpeed.max) {
	    newSpeed = prevSpeed - deviation;
	} else {
	    newSpeed = random.getRandom((prevSpeed - deviation), (prevSpeed + deviation));
	}

	Tuple<Position2D, Double> nextPos = nextPosition(newSpeed, prevAngle, prevPosition);
	MovementPattern pattern = new MovementPattern(newSpeed, nextPos.getFirst(), nextPos.getSecond());

	return pattern;
    }

    private Tuple<Position2D, Double> nextPosition(double distance, double prevAngle, Position2D prevPosition) {
	Position2D position2D = null;

	double area = random.getRandom(0d, 1d);

	double angle = 0d;
	if (area < 0.39d) {
	    if (area < 0.195d) {
		angle = random.getRandom(0d, 15d);
	    } else {
		angle = random.getRandom(345d, 360d);
	    }
	} else if (area >= 0.39d && area < 0.66d) {
	    if (area < 52.5d) {
		angle = random.getRandom(15d, 45d);
	    } else {
		angle = random.getRandom(315d, 345d);
	    }
	} else if (area >= 0.66d && area < 0.88d) {
	    if (area < 79.5d) {
		angle = random.getRandom(45d, 90d);
	    } else {
		angle = random.getRandom(270d, 315d);
	    }
	} else if (area >= 0.83d && area < 0.95d) {
	    if (area < 0.89d) {
		angle = random.getRandom(90d, 160d);
	    } else {
		angle = random.getRandom(200d, 270d);
	    }
	} else {
	    if (area < 0.975d) {
		angle = random.getRandom(160d, 180d);
	    } else {
		angle = random.getRandom(180d, 200d);
	    }
	}
	angle = (angle + prevAngle) % 360;
	double angleRadians = Math.toRadians(angle);
	double xNew = (Math.cos(angleRadians) * distance) + prevPosition.x();
	double yNew = (Math.sin(angleRadians) * distance) + prevPosition.y();
	position2D = new Position2D(xNew, yNew);

	return new Tuple<Position2D, Double>(position2D, angle);
    }

}
