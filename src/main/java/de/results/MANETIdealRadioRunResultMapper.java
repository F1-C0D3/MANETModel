package de.results;

import java.util.List;
import java.util.function.Supplier;

import com.opencsv.bean.ColumnPositionMappingStrategy;

import de.jgraphlib.graph.Position2D;
import de.jgraphlib.graph.generator.GraphProperties;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Tuple;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.mobility.MobilityModel;
import de.manetmodel.network.mobility.MovementPattern;
import de.manetmodel.network.radio.IRadioModel;

public class MANETIdealRadioRunResultMapper<R extends RunResultParameter> extends RunResultMapper<R> {
    private GraphProperties networkProperties;
    private IRadioModel radioModel;
    private MobilityModel mobilityModel;

    public MANETIdealRadioRunResultMapper(Supplier<R> resultParameterSupplier,
	    ColumnPositionMappingStrategy<R> mappingStrategy, Scenario scenario, GraphProperties networkProperties,
	    IRadioModel radioModel, MobilityModel mobilityModel) {
	super(resultParameterSupplier, mappingStrategy, scenario);

	this.networkProperties = networkProperties;
	this.radioModel = radioModel;
	this.mobilityModel = mobilityModel;
    }

    public MANETIdealRadioRunResultMapper(Supplier<R> resultParameterSupplier,
	    ColumnPositionMappingStrategy<R> mappingStrategy, Scenario scenario) {
	super(resultParameterSupplier, mappingStrategy, scenario);
    }

    @Override
    public <W extends LinkQuality> R individualRunResultMapper(int n1Id, int n2Id, int lId, W w) {
	R result = resultParameterSupplier.get();
	result.setlId(lId);
	result.setN1Id(n1Id);
	result.setN2Id(n2Id);

	long transmissionrate = w.getTransmissionRate().get();
	long utilization = w.getUtilization().get();

	if (w.getIsActive()) {
	    result.setPathParticipant(true);
	    if (transmissionrate < utilization)
		result.setOverUtilization(utilization - transmissionrate);
	    MovementPattern nodeOneMobilityPattern = w.getSinkAndSourceMobility().getFirst().get(0);
	    MovementPattern nodeTwoMobilityPattern = w.getSinkAndSourceMobility().getSecond().get(0);
	    double linkQuality = radioModel.receptionPower(
		    nodeDistance(nodeOneMobilityPattern.getPostion(), nodeTwoMobilityPattern.getPostion()));
	    result.setConnectionStability(linkQuality);
	}
	result.setUtilization(w.getUtilization().get());
	return result;
    }

    private double nodeDistance(Position2D nodeOne, Position2D nodeTwo) {
	return Math.sqrt(Math.pow(nodeOne.x() - nodeTwo.x(), 2) + Math.pow(nodeOne.y() - nodeTwo.y(), 2));
    }

}
