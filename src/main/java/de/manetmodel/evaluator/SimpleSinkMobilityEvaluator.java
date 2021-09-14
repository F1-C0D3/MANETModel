package de.manetmodel.evaluator;

import de.jgraphlib.util.Tuple;
import de.manetmodel.mobilitymodel.MobilityModel;
import de.manetmodel.mobilitymodel.MovementPattern;
import de.manetmodel.network.Node;
import de.manetmodel.units.Speed;

public class SimpleSinkMobilityEvaluator<N extends Node> extends PropertyStandardization {

    PropertyEvaluator<Tuple<Speed, Speed>> speed;

    MobilityModel mobilityModel;

    public SimpleSinkMobilityEvaluator(DoubleScope scoreScope, double weight, MobilityModel mobilityModel) {
	super(scoreScope, weight);

	this.mobilityModel = mobilityModel;

	setPropertyScope(new DoubleScope(Math.pow(mobilityModel.getSpeedRange().max().value, 2),
		Math.pow(mobilityModel.getSpeedRange().min().value, 2)));

	speed = new PropertyEvaluator<Tuple<Speed, Speed>>(/* propertyValue */ (Tuple<Speed, Speed> speeds) -> {
	    return speeds.getFirst().value * speeds.getSecond().value;
	}, /* propertyScope */ new DoubleScope(Math.pow(mobilityModel.getSpeedRange().min().value, 2),
		Math.pow(mobilityModel.getSpeedRange().max().value, 0)), /* scoreScope */ new DoubleScope(0d, 1d),
		/* weight */ 1);

    }

    public double compute(N source, N sink) {

	MovementPattern sinkTick = sink.getMobility().get(source.getMobility().size() - 1);


	return sinkTick.getSpeed().value / mobilityModel.getSpeedRange().max().value;
    }
}
