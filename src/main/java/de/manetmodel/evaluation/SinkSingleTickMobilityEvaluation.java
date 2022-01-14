package de.manetmodel.evaluation;

import de.manetmodel.evaluator.DoubleScope;
import de.manetmodel.evaluator.LinearStandardization;
import de.manetmodel.mobilitymodel.MobilityModel;
import de.manetmodel.mobilitymodel.MovementPattern;
import de.manetmodel.network.Node;
import de.manetmodel.units.Unit.Distance;
import de.manetmodel.units.Unit.TimeSteps;

public class SinkSingleTickMobilityEvaluation<N extends Node> extends LinearStandardization {

    MobilityModel mobilityModel;

    public SinkSingleTickMobilityEvaluation(DoubleScope scoreScope, double weight, MobilityModel mobilityModel) {
	super(scoreScope, weight);

	this.mobilityModel = mobilityModel;

	setPropertyScope(new DoubleScope(mobilityModel.getSpeedRange().min().convertTo(Distance.kilometer, TimeSteps.hour), mobilityModel.getSpeedRange().max().convertTo(Distance.kilometer, TimeSteps.hour)));

    }

    public double compute(N source, N sink) {

	MovementPattern sinkTick = sink.getPreviousMobilityPattern();
	 double speedValue = sinkTick.getSpeed().value / mobilityModel.getSpeedRange().max().value;

	return speedValue;
    }
}
