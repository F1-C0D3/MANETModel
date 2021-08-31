package de.manetmodel.results.examples;

import com.opencsv.bean.ColumnPositionMappingStrategy;

import de.manemodel.elements.examples.IdealLinkQuality;
import de.manetmodel.elements.Link;
import de.manetmodel.elements.Node;
import de.manetmodel.network.mobility.MobilityModel;
import de.manetmodel.network.mobility.MovementPattern;
import de.manetmodel.network.radio.examples.ScalarRadioModel;
import de.manetmodel.results.RunResultMapper;
import de.manetmodel.results.RunResultParameter;
import de.manetmodel.scenarios.Scenario;

public class IdealRadioRunResultMapper extends RunResultMapper<IdealLinkQuality, RunResultParameter> {

    public IdealRadioRunResultMapper(ColumnPositionMappingStrategy<RunResultParameter> mappingStrategy,
	    Scenario scenario, MobilityModel mobilityModel) {
	super(mappingStrategy, scenario, mobilityModel);
    }


    @Override
    public RunResultParameter individualRunResultMapper(Node source, Node sink, Link<IdealLinkQuality> link) {

	RunResultParameter resultParameter = new RunResultParameter();
	resultParameter.setlId(link.getID());
	resultParameter.setN1Id(source.getID());
	resultParameter.setN2Id(sink.getID());

	long transmissionrate = link.getLinkParameters().getTransmissionRate().get();
	long utilization = link.getUtilization().get();

	if (link.isActive()) {
	    resultParameter.setPathParticipant(true);
	    if (transmissionrate < utilization)
		resultParameter.setOverUtilization(utilization - transmissionrate);

	    MovementPattern nodeOneMobilityPattern = source.getNodeParameters().getMovementBehavior().get(0);
	    MovementPattern nodeTwoMobilityPattern = sink.getNodeParameters().getMovementBehavior().get(0);
	    double linkQuality = link.getWeight().getDistance();
	    resultParameter.setConnectionStability(linkQuality);
	}
	resultParameter.setUtilization(link.getUtilization().get());
	return resultParameter;
    }

    @Override
    public ColumnPositionMappingStrategy<RunResultParameter> getMappingStrategy() {
	return this.mappingStrategy;
    }

    @Override
    public void setMappingStrategy(ColumnPositionMappingStrategy<RunResultParameter> mappingStrategy) {
	this.mappingStrategy = mappingStrategy;

    }

}
