package de.manetmodel.evaluator;

import de.jgraphlib.util.Tuple;
import de.manetmodel.mobilitymodel.MobilityModel;
import de.manetmodel.mobilitymodel.MovementPattern;
import de.manetmodel.network.Node;
import de.manetmodel.units.Speed;

public class MobilityEvaluator<N extends Node> extends PropertyStandardization {

    PropertyEvaluator<Tuple<Speed, Speed>> speed;
    
    MobilityModel mobilityModel;
        
    public MobilityEvaluator(DoubleScope scoreScope, double weight, MobilityModel mobilityModel) {
	super(scoreScope, weight);

	this.mobilityModel = mobilityModel;
	
	setPropertyScope(new DoubleScope(Math.pow(mobilityModel.getSpeedRange().max().value, 2), Math.pow(mobilityModel.getSpeedRange().min().value, 2)));
	
	speed = new PropertyEvaluator<Tuple<Speed, Speed>>(
		/* propertyValue */	(Tuple<Speed, Speed> speeds) -> { return speeds.getFirst().value * speeds.getSecond().value; },
		/* propertyScope */ 	new DoubleScope(Math.pow(mobilityModel.getSpeedRange().min().value, 2), Math.pow(mobilityModel.getSpeedRange().max().value, 0)),
		/* scoreScope */ 	new DoubleScope(0d, 1d), 
		/* weight */		1);
	
	//System.out.println(String.format("SpeedRange: min %.2f max %.2f", mobilityModel.getSpeedRange().min().value, mobilityModel.getSpeedRange().max().value));
    }

    public double compute(N source, N sink) {

	MovementPattern sourceTick = source.getMobility().get(source.getMobility().size() - 1);
	MovementPattern sinkTick = sink.getMobility().get(source.getMobility().size() - 1);

	// Check if motion is in same direction
	//double angleDregrees = Math.abs(sourceTick.getAngle() - sinkTick.getAngle());
	//double vectorProduct = sourceTick.getSpeed().value * sinkTick.getSpeed().value;
	//double scalarProduct = Math.cos(Math.toRadians(angleDregrees)) * vectorProduct;
	//setPropertyScope(new DoubleScope(-vectorProduct, vectorProduct));
	//return getScore(scalarProduct);
	
	// Compute a "speed-score" based on node's speed
	//double speedScore = speed.getScore(new Tuple<Speed,Speed>(sourceTick.getSpeed(), sinkTick.getSpeed()));
	
	return getScore(sourceTick.getSpeed().value * sinkTick.getSpeed().value);
    }
}
