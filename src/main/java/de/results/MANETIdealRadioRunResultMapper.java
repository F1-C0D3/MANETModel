package de.results;

import java.util.List;
import java.util.function.Supplier;

import com.opencsv.bean.ColumnPositionMappingStrategy;

import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.generator.GraphProperties;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.Node;
import de.manetmodel.network.mobility.MobilityModel;
import de.manetmodel.network.mobility.MovementPattern;
import de.manetmodel.network.radio.IRadioModel;
import de.manetmodel.scenarios.Scenario;

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

	    MovementPattern nodeOneMobilityPattern = source.getPrevMobility().get(0);
	    MovementPattern nodeTwoMobilityPattern = sink.getPrevMobility().get(0);
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
