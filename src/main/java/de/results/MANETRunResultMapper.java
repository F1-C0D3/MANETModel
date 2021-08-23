package de.results;

import java.util.List;
import java.util.function.Supplier;

import com.opencsv.bean.ColumnPositionMappingStrategy;

import de.jgraphlib.graph.generator.GraphProperties;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Tuple;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.Node;
import de.manetmodel.network.mobility.MobilityModel;
import de.manetmodel.network.mobility.MovementPattern;
import de.manetmodel.network.radio.IRadioModel;
import de.manetmodel.scenarios.Scenario;

public class MANETRunResultMapper<R extends RunResultParameter> extends RunResultMapper<R> {
    private GraphProperties networkProperties;
    private IRadioModel radioModel;
    private MobilityModel mobilityModel;

    public MANETRunResultMapper(Supplier<R> resultParameterSupplier, ColumnPositionMappingStrategy<R> mappingStrategy,
	    Scenario scenario, GraphProperties networkProperties, IRadioModel radioModel, MobilityModel mobilityModel) {
	super(resultParameterSupplier, mappingStrategy, scenario);

	this.networkProperties = networkProperties;
	this.radioModel = radioModel;
	this.mobilityModel = mobilityModel;
    }

    public MANETRunResultMapper(Supplier<R> resultParameterSupplier, ColumnPositionMappingStrategy<R> mappingStrategy,
	    Scenario scenario) {
	super(resultParameterSupplier, mappingStrategy, scenario);
    }

    @Override
    public <W extends LinkQuality, N extends Node> R individualRunResultMapper(N source, N sink, int lId, W w) {
	R result = resultParameterSupplier.get();
	result.setlId(lId);
	result.setN1Id(source.getID());
	result.setN2Id(sink.getID());

	long transmissionrate = w.getTransmissionRate().get();
	long utilization = w.getUtilization().get();

	if (w.isActive()) {
	    result.setPathParticipant(true);
	    if (transmissionrate < utilization)
		result.setOverUtilization(utilization - transmissionrate);
	    result.setConnectionStability(getLinkStability(source.getPrevMobility(), sink.getPrevMobility()));
	}
	result.setUtilization(w.getUtilization().get());
	return result;
    }

    private int getLinkStability(List<MovementPattern> mobilitySource, List<MovementPattern> mobilitySink) {
	double max = networkProperties.getEdgeDistance().max;
	MovementPattern nodeOneMobilityPattern = mobilitySource.get(0);
	MovementPattern nodeTwoMobilityPattern = mobilitySink.get(0);
	double currentDistance = nodeDistance(nodeOneMobilityPattern.getPostion(), nodeTwoMobilityPattern.getPostion());
	double requiredReceptionPower = radioModel
		.receptionPower(RandomNumbers.getInstance(1).getRandom(currentDistance, max));

	double currentReceptionPower = radioModel.receptionPower(currentDistance);
	int stabilityIterator = 0;
	while (currentReceptionPower >= requiredReceptionPower) {
	    MovementPattern newNodeOneMobilityPattern = mobilityModel
		    .computeNextMovementPattern(nodeOneMobilityPattern);
	    MovementPattern newNodeTwoMobilityPattern = mobilityModel
		    .computeNextMovementPattern(nodeTwoMobilityPattern);
	    currentDistance = nodeDistance(newNodeOneMobilityPattern.getPostion(),
		    newNodeTwoMobilityPattern.getPostion());
	    currentReceptionPower = radioModel.receptionPower(currentDistance);

	    nodeOneMobilityPattern = newNodeOneMobilityPattern;
	    nodeTwoMobilityPattern = newNodeTwoMobilityPattern;
	    stabilityIterator++;
	}
	return (int) (stabilityIterator * mobilityModel.timeStamp.value);
    }

    private double nodeDistance(Position2D nodeOne, Position2D nodeTwo) {
	return Math.sqrt(Math.pow(nodeOne.x() - nodeTwo.x(), 2) + Math.pow(nodeOne.y() - nodeTwo.y(), 2));
    }

}
