package de.manetmodel.results.examples;

import com.opencsv.bean.ColumnPositionMappingStrategy;

import de.jgraphlib.graph.elements.Position2D;
import de.manemodel.elements.examples.ScalarLinkQuality;
import de.manetmodel.elements.Link;
import de.manetmodel.elements.Node;
import de.manetmodel.network.mobility.MobilityModel;
import de.manetmodel.network.mobility.MovementPattern;
import de.manetmodel.network.radio.examples.ScalarConnectionProperties;
import de.manetmodel.network.radio.examples.ScalarLinkParameters;
import de.manetmodel.network.radio.examples.ScalarNodeParameters;
import de.manetmodel.network.radio.examples.ScalarRadioModel;
import de.manetmodel.results.RunResultMapper;
import de.manetmodel.results.RunResultParameter;
import de.manetmodel.scenarios.Scenario;

public class ScalarRadioRunResultMapper extends RunResultMapper<ScalarLinkQuality, RunResultParameter> {

    public ScalarRadioRunResultMapper(ColumnPositionMappingStrategy<RunResultParameter> mappingStrategy,
	    Scenario scenario, MobilityModel mobilityModel) {
	super(mappingStrategy, scenario, mobilityModel);

    }

    private int getLinkStability(ScalarNodeParameters sourceNodeProperties, ScalarNodeParameters sinkNodeProperties,
	    ScalarLinkParameters linkParameters) {

	MovementPattern nodeOneMobilityPattern = sourceNodeProperties.getMovementBehavior().get(0);
	MovementPattern nodeTwoMobilityPattern = sinkNodeProperties.getMovementBehavior().get(0);
	double currentDistance = nodeDistance(nodeOneMobilityPattern.getPostion(), nodeTwoMobilityPattern.getPostion());
	double requiredReceptionPower = sinkNodeProperties.getReceptionThreshold();

	double currentReceptionPower = linkParameters.getReceptionPower();

	int stabilityIterator = 0;

	while (currentReceptionPower >= requiredReceptionPower) {

	    MovementPattern newNodeOneMobilityPattern = mobilityModel
		    .computeNextMovementPattern(nodeOneMobilityPattern);

	    MovementPattern newNodeTwoMobilityPattern = mobilityModel
		    .computeNextMovementPattern(nodeTwoMobilityPattern);

	    currentDistance = nodeDistance(newNodeOneMobilityPattern.getPostion(),
		    newNodeTwoMobilityPattern.getPostion());

	    currentReceptionPower = ScalarRadioModel.Propagation.receptionPower(currentDistance,
		    sourceNodeProperties.getCarrierFrequency(), sourceNodeProperties.getTransmissionPower());

	    nodeOneMobilityPattern = newNodeOneMobilityPattern;
	    nodeTwoMobilityPattern = newNodeTwoMobilityPattern;
	    stabilityIterator++;
	}
	return (int) (stabilityIterator * mobilityModel.timeStamp.value);
    }

    private double nodeDistance(Position2D nodeOne, Position2D nodeTwo) {
	return Math.sqrt(Math.pow(nodeOne.x() - nodeTwo.x(), 2) + Math.pow(nodeOne.y() - nodeTwo.y(), 2));
    }

    @Override
    public RunResultParameter individualRunResultMapper(Node source, Node sink, Link<ScalarLinkQuality> link) {
	RunResultParameter runResultParameter = new RunResultParameter();
	runResultParameter.setlId(link.getID());
	runResultParameter.setN1Id(source.getID());
	runResultParameter.setN2Id(sink.getID());

	long transmissionrate = link.getLinkParameters().getTransmissionRate().get();
	long utilization = link.getUtilization().get();

	if (link.isActive()) {
	    
	    runResultParameter.setPathParticipant(true);
	    
	    if (transmissionrate < utilization)
		runResultParameter.setOverUtilization(utilization - transmissionrate);
	    
	    runResultParameter.setConnectionStability(getLinkStability(
		    (ScalarNodeParameters) source.getNodeParameters(), (ScalarNodeParameters) sink.getNodeParameters(),
		    (ScalarLinkParameters) link.getLinkParameters()));
	}
	
	runResultParameter.setUtilization(link.getUtilization().get());
	
	return runResultParameter;
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
