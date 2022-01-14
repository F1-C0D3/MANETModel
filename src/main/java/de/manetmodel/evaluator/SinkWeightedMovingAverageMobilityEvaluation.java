package de.manetmodel.evaluator;

import java.util.List;

import de.manetmodel.mobilitymodel.MobilityModel;
import de.manetmodel.mobilitymodel.MovementPattern;
import de.manetmodel.network.Node;
import de.manetmodel.units.Unit.Distance;
import de.manetmodel.units.Unit.TimeSteps;

public class SinkWeightedMovingAverageMobilityEvaluator<N extends Node> extends LinearStandardization {

    MobilityModel mobilityModel;

    public SinkWeightedMovingAverageMobilityEvaluator(DoubleScope scoreScope, double weight,
	    MobilityModel mobilityModel) {
	super(scoreScope, weight);

	this.mobilityModel = mobilityModel;

	setPropertyScope(
		new DoubleScope(mobilityModel.getSpeedRange().min().convertTo(Distance.kilometer, TimeSteps.hour),
			mobilityModel.getSpeedRange().max().convertTo(Distance.kilometer, TimeSteps.hour)));

    }

    public double compute(N source, N sink) {

	double[] n = new double[] {5d,10d,15d, 20d,50d};
	List<MovementPattern> sinkMovementPatterns = sink.getNPreviousMobilityPatterns(n.length);

	double wma = 0d;
	double sumN=0d;
	
	double speedRange = mobilityModel.getSpeedRange().max().value-mobilityModel.getSpeedRange().min().value;
	for (int i = 0; i < n.length; i++) {
	    
	    wma +=(((sinkMovementPatterns.get(i).getSpeed().value-mobilityModel.getSpeedRange().min().value)/speedRange)*n[i]);
	    sumN +=n[i];
	}
	
	wma= wma/sumN;
	return wma;
    }
}
