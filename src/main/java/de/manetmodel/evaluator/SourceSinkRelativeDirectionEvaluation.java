package de.manetmodel.evaluator;

import java.util.List;

import de.manetmodel.mobilitymodel.MobilityModel;
import de.manetmodel.mobilitymodel.MovementPattern;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.Node;
import de.manetmodel.units.Speed;
import de.manetmodel.units.Speed.SpeedRange;
import de.manetmodel.units.Time;
import de.manetmodel.units.Unit.Distance;
import de.manetmodel.units.Unit.TimeSteps;

public class SourceSinkRelativeDirectionEvaluator<N extends Node, L extends Link<W>, W extends LinkQuality> extends LinearStandardization {

    MobilityModel mobilityModel;

    public SourceSinkRelativeDirectionEvaluator(DoubleScope scoreScope, double weight, MobilityModel mobilityModel) {
	super(scoreScope, weight);

	this.mobilityModel = mobilityModel;

	setPropertyScope(new DoubleScope(mobilityModel.getSpeedRange().min().convertTo(Distance.kilometer, TimeSteps.hour), mobilityModel.getSpeedRange().max().convertTo(Distance.kilometer, TimeSteps.hour)));

    }

    public double compute(N source, N sink, L link) {

	List<MovementPattern> sourceTicks = source.getMobilityCharacteristic();
	List<MovementPattern> sinkTicks = sink.getMobilityCharacteristic();
	
	SpeedRange speedRange = mobilityModel.getSpeedRange();
	Time timeStamp = mobilityModel.getTickDuration();
	
	MovementPattern currentSourceTick = sourceTicks.get(sourceTicks.size()-1);
	MovementPattern currentSinkTick = sinkTicks.get(sinkTicks.size()-1);
	double currentDistance = Math.sqrt(Math.pow(currentSourceTick.getPostion().x() - currentSinkTick.getPostion().x(), 2) + Math.pow(currentSourceTick.getPostion().y() - currentSinkTick.getPostion().y(), 2));
	
	MovementPattern previousSourceTick = sourceTicks.get(sourceTicks.size()-2);
	MovementPattern previousSinkTick = sinkTicks.get(sinkTicks.size()-2);
	double previousPrevDistance = Math.sqrt(Math.pow(previousSourceTick.getPostion().x() - previousSinkTick.getPostion().x(), 2) + Math.pow(previousSourceTick.getPostion().y() - previousSinkTick.getPostion().y(), 2));

	double actualdistance = Math.abs(currentDistance-previousPrevDistance);
	double worstCasDistance =speedRange.max().convertTo(Distance.meter, TimeSteps.milliseconds) * timeStamp.getMillis()*2d;

	return actualdistance /worstCasDistance;
    }
}
