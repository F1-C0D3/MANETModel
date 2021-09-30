package de.manetmodel.evaluator;

import de.jgraphlib.util.Tuple;
import de.manetmodel.mobilitymodel.MobilityModel;
import de.manetmodel.mobilitymodel.MovementPattern;
import de.manetmodel.network.Node;
import de.manetmodel.units.Speed;

public class SimpleSinkMobilityEvaluator<N extends Node> extends LinearStandardization {

    MobilityModel mobilityModel;

    public SimpleSinkMobilityEvaluator(DoubleScope scoreScope, double weight, MobilityModel mobilityModel) {
	super(scoreScope, weight);

	this.mobilityModel = mobilityModel;

	setPropertyScope(new DoubleScope(Math.pow(mobilityModel.getSpeedRange().max().value, 2),
		Math.pow(mobilityModel.getSpeedRange().min().value, 2)));

    }

    public double compute(N source, N sink) {

	MovementPattern sinkTick = sink.getPreviousMobilityPattern();


	return sinkTick.getSpeed().value / mobilityModel.getSpeedRange().max().value;
    }
}
