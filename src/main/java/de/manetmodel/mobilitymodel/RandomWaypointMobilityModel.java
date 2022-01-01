package de.manetmodel.mobilitymodel;

import de.jgraphlib.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Tuple;
import de.manetmodel.units.Speed;
import de.manetmodel.units.Time;
import de.manetmodel.units.Unit;
import de.manetmodel.units.Speed.SpeedRange;
import de.manetmodel.units.Unit.Distance;
import de.manetmodel.units.Unit.TimeSteps;

public class RandomWaypointMobilityModel extends MobilityModel {


    public RandomWaypointMobilityModel(RandomNumbers random, SpeedRange speedRange, Time tickDuration, int ticks) {
	super(random, speedRange, tickDuration, ticks);
    }

    @Override
    public MovementPattern computeNextMovementPattern(MovementPattern prevPattern) {

	Speed newSpeed = this.initializeSpeed();
	Tuple<Position2D, Double> nextPos = nextPosition(newSpeed, prevPattern.getAngle(), prevPattern.getPostion());
	MovementPattern pattern = new MovementPattern(newSpeed, nextPos.getFirst(), nextPos.getSecond());

	return pattern;
    }

    private Tuple<Position2D, Double> nextPosition(Speed speed, double prevAngle, Position2D prevPosition) {
	double angle = random.getRandom(0d, 360d);

	angle = (angle + prevAngle) % 360;
	double angleRadians = Math.toRadians(angle);
	double xNew = (Math.cos(angleRadians) * speed.convertTo(Distance.meter, TimeSteps.milliseconds)
		* this.getTickDuration().getMillis()) + prevPosition.x();
	double yNew = (Math.sin(angleRadians) * speed.convertTo(Distance.meter, TimeSteps.milliseconds)
		* this.getTickDuration().getMillis()) + prevPosition.y();
	Position2D position = new Position2D(xNew, yNew);

	return new Tuple<Position2D, Double>(position, angle);
    }

    @Override
    public Speed initializeSpeed() {

	double initialSpeed = random.getRandom((this.getSpeedRange().min().value),
		(this.getSpeedRange().max().value));
	return new Speed(initialSpeed);
    }

}
