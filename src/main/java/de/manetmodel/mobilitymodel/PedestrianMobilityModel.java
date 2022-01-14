package de.manetmodel.mobilitymodel;

import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Tuple;
import de.manetmodel.units.Speed;
import de.manetmodel.units.Speed.SpeedRange;
import de.manetmodel.units.Time;
import de.manetmodel.units.Unit.Distance;
import de.manetmodel.units.Unit.TimeSteps;

public class PedestrianMobilityModel extends MobilityModel {

    private Speed deviation;

    public PedestrianMobilityModel(RandomNumbers random, SpeedRange speedRange, Speed deviation) {
	super(random, speedRange);
	this.deviation = deviation;
    }

    public PedestrianMobilityModel(RandomNumbers random, SpeedRange speedRange, Time tickDuration, Speed deviation) {
	super(random, speedRange, tickDuration);
	this.deviation = deviation;
    }

    public PedestrianMobilityModel(RandomNumbers random, SpeedRange speedRange, Time tickDuration, Speed deviation,
	    int segments) {
	super(random, speedRange, tickDuration, segments);
	this.deviation = deviation;
    }

    @Override
    public MovementPattern computeNextMovementPattern(MovementPattern prevPattern) {
	Speed prevSpeed = prevPattern.getSpeed();
	Position2D prevPosition = prevPattern.getPostion();
	double prevAngle = prevPattern.getAngle();
	if ((prevSpeed.value - deviation.value) < speedRange.min().value) {
	    prevSpeed.value = deviation.value + prevSpeed.value;
	} else if ((prevSpeed.value + deviation.value) > speedRange.max().value) {
	    prevSpeed.value = prevSpeed.value - deviation.value;
	} else {
	    prevSpeed.value = random.getRandom((prevSpeed.value - deviation.value),
		    (prevSpeed.value + deviation.value));
	}
	Speed newSpeed = new Speed(prevSpeed.value);
	Tuple<Position2D, Double> nextPos = nextPosition(newSpeed, prevAngle, prevPosition);
	MovementPattern pattern = new MovementPattern(newSpeed, nextPos.getFirst(), nextPos.getSecond());

	return pattern;
    }

    private Tuple<Position2D, Double> nextPosition(Speed speed, double prevAngle, Position2D prevPosition) {
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
	double xNew = (Math.cos(angleRadians) * speed.convertTo(Distance.meter, TimeSteps.milliseconds)*this.getTickDuration().getMillis()) + prevPosition.x();
	double yNew = (Math.sin(angleRadians) * speed.convertTo(Distance.meter, TimeSteps.milliseconds)*this.getTickDuration().getMillis()) + prevPosition.y();
	position2D = new Position2D(xNew, yNew);

	return new Tuple<Position2D, Double>(position2D, angle);
    }

    @Override
    public Speed initializeSpeed() {

	double initialSpeed = random.getRandom((this.getSpeedRange().min().value) + this.deviation.value,
		(this.getSpeedRange().max().value) - this.deviation.value);
	return new Speed(initialSpeed);
    }

}
