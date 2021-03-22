package de.results;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import de.jgraphlib.graph.Position2D;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.util.Tuple;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.mobility.MobilityModel;
import de.manetmodel.network.mobility.MovementPattern;
import de.manetmodel.network.radio.IRadioModel;
import de.manetmodel.network.unit.Speed.Time;
import de.manetmodel.network.unit.VelocityUnits;
import de.manetmodel.network.unit.VelocityUnits.TimeUnit;

public class MANETRunResultMapper<W extends LinkQuality, R extends MANETResultParameter> extends RunResultMapper<W, R> {

    private NetworkGraphProperties networkProperties;
    private IRadioModel radioModel;
    private MobilityModel mobilityModel;

    public MANETRunResultMapper(Supplier<R> supplier, Scenario scenario, NetworkGraphProperties networkProperties,
	    IRadioModel radioModel, MobilityModel mobilityModel) {
	super(supplier, scenario);

	this.networkProperties = networkProperties;
	this.radioModel = radioModel;
	this.mobilityModel = mobilityModel;
    }

    public MANETRunResultMapper(Supplier<R> supplier) {
	super(supplier);
    }

    @Override
    public R singleRunResultMapper(int n1Id, int n2Id, int lId, W w) {

	R r = super.singleRunResultMapper(n1Id, n2Id, lId, w);

	double transmissionrate = w.getTransmissionRate().get();
	double utilization = w.getUtilization().get();

	if (w.getIsActive()) {
	    r.setPathParticipant(true);
	    if (transmissionrate < utilization)
		r.setOverUtilization(utilization - transmissionrate);
	    r.setConnectionStability(getLinkStability(w.getSinkAndSourceMobility()));
	}
	r.setUtilization(w.getUtilization().get());
	return r;
    }

    private double getLinkStability(Tuple<List<MovementPattern>, List<MovementPattern>> linkMovementPattern) {
	double max = networkProperties.getEdgeDistance().max;
	double requiredReceptionPower = radioModel.receptionPower(max);
	MovementPattern nodeOneMobilityPattern = linkMovementPattern.getFirst().get(0);
	MovementPattern nodeTwoMobilityPattern = linkMovementPattern.getSecond().get(0);
	double currentDistance = nodeDistance(nodeOneMobilityPattern.getPostion(), nodeTwoMobilityPattern.getPostion());

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
	return stabilityIterator * mobilityModel.timeStamp.value;
    }

    private double nodeDistance(Position2D nodeOne, Position2D nodeTwo) {
	return Math.sqrt(Math.pow(nodeOne.x() - nodeTwo.x(), 2) + Math.pow(nodeOne.y() - nodeTwo.y(), 2));
    }

    @Override
    public R toMeanMapper(List<List<R>> runs) {

	R result = supplier.get();

	for (List<R> run : runs) {
	    for (R r : run) {
		result.setOverUtilization(result.getOverUtilization() + r.getOverUtilization());
		result.setUtilization(result.getUtilization() + r.getUtilization());
		result.setConnectionStability(result.getConnectionStability() + r.getConnectionStability());
	    }
	}

	result.setOverUtilization(result.getOverUtilization() / runs.size());
	result.setUtilization(result.getUtilization() / runs.size());
	result.setConnectionStability(result.getConnectionStability() / runs.size());

	return result;
    }
}