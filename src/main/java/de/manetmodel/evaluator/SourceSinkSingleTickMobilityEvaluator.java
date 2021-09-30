package de.manetmodel.evaluator;

import de.manetmodel.mobilitymodel.MobilityModel;
import de.manetmodel.mobilitymodel.MovementPattern;
import de.manetmodel.network.Node;

public class SourceSinkSingleTickMobilityEvaluator<N extends Node> extends LinearStandardization {
    
    MobilityModel mobilityModel;
        
    public SourceSinkSingleTickMobilityEvaluator(DoubleScope scoreScope, double weight, MobilityModel mobilityModel) {
	super(scoreScope, weight);

	this.mobilityModel = mobilityModel;
	
	setPropertyScope(new DoubleScope(Math.pow(mobilityModel.getSpeedRange().max().value, 2), Math.pow(mobilityModel.getSpeedRange().min().value, 2)));
	
    }

    public double compute(N source, N sink) {
	MovementPattern sourceTick = source.getPreviousMobilityPattern();
	MovementPattern sinkTick = sink.getPreviousMobilityPattern();

	
	return getScore(sourceTick.getSpeed().value * sinkTick.getSpeed().value);
    }
}
