package de.manetmodel.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.swing.SwingUtilities;

import org.junit.Test;

import de.jgraphlib.graph.algorithms.DijkstraShortestPath;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;
import de.jgraphlib.graph.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Tuple;
import de.manetmodel.evaluator.DoubleScope;
import de.manetmodel.evaluator.ScalarLinkQualityEvaluator;
import de.manetmodel.gui.LinkUtilizationPrinter;
import de.manetmodel.mobilitymodel.PedestrianMobilityModel;
import de.manetmodel.network.scalar.ScalarLinkQuality;
import de.manetmodel.network.scalar.ScalarRadioFlow;
import de.manetmodel.network.scalar.ScalarRadioLink;
import de.manetmodel.network.scalar.ScalarRadioMANET;
import de.manetmodel.network.scalar.ScalarRadioMANETSupplier;
import de.manetmodel.network.scalar.ScalarRadioModel;
import de.manetmodel.network.scalar.ScalarRadioNode;
import de.manetmodel.units.DataRate;
import de.manetmodel.units.Speed;
import de.manetmodel.units.Speed.SpeedRange;
import de.manetmodel.units.Unit;
import de.manetmodel.units.Watt;

public class OverUtilzedProblemGeneratorTest {

    @Test
    public void overUtilzedProblemGeneratorTest() throws IOException, InvocationTargetException, InterruptedException {

	ScalarRadioModel radioModel = new ScalarRadioModel(new Watt(0.002d), new Watt(1e-11), 1000d, 2412000000d, /**
														   * maxCommunicationRange
														   **/
		100d);
	PedestrianMobilityModel mobilityModel = new PedestrianMobilityModel(new RandomNumbers(),
		new SpeedRange(0, 100, Unit.TimeSteps.second, Unit.Distance.meter),
		new Speed(50, Unit.Distance.meter, Unit.TimeSteps.second));
	ScalarLinkQualityEvaluator evaluator = new ScalarLinkQualityEvaluator(new DoubleScope(0d, 1d), radioModel,
		mobilityModel);

	ScalarRadioMANET manet = new ScalarRadioMANET(new ScalarRadioMANETSupplier().getNodeSupplier(),
		new ScalarRadioMANETSupplier().getLinkSupplier(),
		new ScalarRadioMANETSupplier().getLinkPropertySupplier(),
		new ScalarRadioMANETSupplier().getFlowSupplier(), radioModel, mobilityModel, evaluator);

	NetworkGraphProperties graphProperties = new NetworkGraphProperties(/* playground width */ 1024,
		/* playground height */ 768, /* number of vertices */ new IntRange(100, 100),
		/* distance between vertices */ new DoubleRange(50d, 100d),
		/* edge distance */ new DoubleRange(100d, 100d));

	NetworkGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> generator = new NetworkGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(
		manet, new ScalarRadioMANETSupplier().getLinkPropertySupplier(), new RandomNumbers());

	generator.generate(graphProperties);
	manet.initialize();

	Function<ScalarLinkQuality, Double> metric = (ScalarLinkQuality w) -> {
	    return w.getScore();
	};

	OverUtilzedProblemGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow> overUtilizedProblemGenerator = new OverUtilzedProblemGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow>(
		manet, metric);

	OverUtilizedProblemProperties problemProperties = new OverUtilizedProblemProperties();
	problemProperties.pathCount = 5;
	problemProperties.minLength = 10;
	problemProperties.maxLength = 20;
	problemProperties.minDemand = new DataRate(100);
	problemProperties.maxDemand = new DataRate(200);
	problemProperties.overUtilizationPercentage = 5;

	List<ScalarRadioFlow> flowProblems = overUtilizedProblemGenerator.compute(problemProperties);
    }

    @Test
    public void generateUniqueSourceDestinationNodes() throws InvocationTargetException, InterruptedException {

	ScalarRadioModel radioModel = new ScalarRadioModel(new Watt(0.002d), new Watt(1e-11), 1000d, 2412000000d, 
		100d);
	PedestrianMobilityModel mobilityModel = new PedestrianMobilityModel(new RandomNumbers(),
		new SpeedRange(0, 100, Unit.TimeSteps.second, Unit.Distance.meter),
		new Speed(50, Unit.Distance.meter, Unit.TimeSteps.second));
	ScalarLinkQualityEvaluator evaluator = new ScalarLinkQualityEvaluator(new DoubleScope(0d, 1d), radioModel,
		mobilityModel);

	ScalarRadioMANET manet = new ScalarRadioMANET(new ScalarRadioMANETSupplier().getNodeSupplier(),
		new ScalarRadioMANETSupplier().getLinkSupplier(),
		new ScalarRadioMANETSupplier().getLinkPropertySupplier(),
		new ScalarRadioMANETSupplier().getFlowSupplier(), radioModel, mobilityModel, evaluator);

	NetworkGraphProperties graphProperties = new NetworkGraphProperties(1024,
		768,new IntRange(100, 100),
		new DoubleRange(50d, 100d),
		new DoubleRange(100d, 100d));

	NetworkGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> generator = new NetworkGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(
		manet, new ScalarRadioMANETSupplier().getLinkPropertySupplier(), new RandomNumbers());

	generator.generate(graphProperties);
	manet.initialize();

	Function<ScalarLinkQuality, Double> metric = (ScalarLinkQuality w) -> {
	    return w.getScore();
	};

	OverUtilzedProblemGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow> overUtilizedProblemGenerator = new OverUtilzedProblemGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow>(
		manet, metric);

	OverUtilizedProblemProperties problemProperties = new OverUtilizedProblemProperties();
	problemProperties.pathCount = 50;
	problemProperties.minLength = 10;
	problemProperties.maxLength = 20;
	problemProperties.minDemand = new DataRate(100);
	problemProperties.maxDemand = new DataRate(200);
	problemProperties.overUtilizationPercentage = 5;
	problemProperties.uniqueSourceDestination = true;

	List<ScalarRadioFlow> flowProblems = overUtilizedProblemGenerator.compute(problemProperties);

	List<Integer> uniqueNodeIDs = new ArrayList<Integer>();

	for (ScalarRadioFlow flow : flowProblems) {

	    if (!uniqueNodeIDs.contains(flow.getSource().getID())
		    && !uniqueNodeIDs.contains(flow.getTarget().getID())) {
		uniqueNodeIDs.add(flow.getSource().getID());
		uniqueNodeIDs.add(flow.getTarget().getID());
	    } else {
		fail("One or more IDs are not unique");
	    }

	}
	System.out.println(uniqueNodeIDs.toString());
    }

    @Test
    public void verifyOverUtilization() throws InvocationTargetException, InterruptedException {

	ScalarRadioModel radioModel = new ScalarRadioModel(new Watt(0.002d), new Watt(1e-11), 1000d, 2412000000d,
		100d);
	PedestrianMobilityModel mobilityModel = new PedestrianMobilityModel(new RandomNumbers(),
		new SpeedRange(0, 100, Unit.TimeSteps.second, Unit.Distance.meter),
		new Speed(50, Unit.Distance.meter, Unit.TimeSteps.second));
	ScalarLinkQualityEvaluator evaluator = new ScalarLinkQualityEvaluator(new DoubleScope(0d, 1d), radioModel,
		mobilityModel);

	ScalarRadioMANET manet = new ScalarRadioMANET(new ScalarRadioMANETSupplier().getNodeSupplier(),
		new ScalarRadioMANETSupplier().getLinkSupplier(),
		new ScalarRadioMANETSupplier().getLinkPropertySupplier(),
		new ScalarRadioMANETSupplier().getFlowSupplier(), radioModel, mobilityModel, evaluator);

	NetworkGraphProperties graphProperties = new NetworkGraphProperties(
		1024,
		768, 
		new IntRange(100, 100),	
		new DoubleRange(50d, 100d),
		new DoubleRange(100d, 100d));

	NetworkGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> generator = new NetworkGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(
		manet, new ScalarRadioMANETSupplier().getLinkPropertySupplier(), new RandomNumbers());

	generator.generate(graphProperties);
	manet.initialize();

	Function<ScalarLinkQuality, Double> metric = (ScalarLinkQuality w) -> {
	    return w.getDistance();
	};

	OverUtilzedProblemGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow> overUtilizedProblemGenerator = new OverUtilzedProblemGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow>(
		manet, metric);

	OverUtilizedProblemProperties problemProperties = new OverUtilizedProblemProperties();
	problemProperties.pathCount = 5;
	problemProperties.minLength = 10;
	problemProperties.maxLength = 20;
	problemProperties.minDemand = new DataRate(100);
	problemProperties.maxDemand = new DataRate(200);
	problemProperties.overUtilizationPercentage = 5;
	problemProperties.uniqueSourceDestination = true;

	List<ScalarRadioFlow> flowProblems = overUtilizedProblemGenerator.compute(problemProperties);

	DijkstraShortestPath<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> sp = new DijkstraShortestPath<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(
		manet);

	for (ScalarRadioFlow scalarRadioFlow : flowProblems) {
	    scalarRadioFlow.update(sp.compute(scalarRadioFlow.getSource(), scalarRadioFlow.getTarget(), metric));
	}

	manet.addFlows(flowProblems);
	manet.deployFlows(flowProblems);
	assertNotEquals(0d, manet.getOverUtilization().get());

    }

    @Test
    public void verifySingleFlowNoSolution() throws InvocationTargetException, InterruptedException {

	ScalarRadioModel radioModel = new ScalarRadioModel(new Watt(0.002d), new Watt(1e-11), 1000d, 2412000000d, 
		100d);
	PedestrianMobilityModel mobilityModel = new PedestrianMobilityModel(new RandomNumbers(),
		new SpeedRange(0, 100, Unit.TimeSteps.second, Unit.Distance.meter),
		new Speed(50, Unit.Distance.meter, Unit.TimeSteps.second));
	ScalarLinkQualityEvaluator evaluator = new ScalarLinkQualityEvaluator(new DoubleScope(0d, 1d), radioModel,
		mobilityModel);

	ScalarRadioMANET manet = new ScalarRadioMANET(new ScalarRadioMANETSupplier().getNodeSupplier(),
		new ScalarRadioMANETSupplier().getLinkSupplier(),
		new ScalarRadioMANETSupplier().getLinkPropertySupplier(),
		new ScalarRadioMANETSupplier().getFlowSupplier(), radioModel, mobilityModel, evaluator);

	NetworkGraphProperties graphProperties = new NetworkGraphProperties(
		1024,
		768,
		new IntRange(100, 100),
		new DoubleRange(50d, 100d),
		new DoubleRange(100d, 100d));

	NetworkGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> generator = new NetworkGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(
		manet, new ScalarRadioMANETSupplier().getLinkPropertySupplier(), new RandomNumbers());

	generator.generate(graphProperties);
	manet.initialize();

	Function<ScalarLinkQuality, Double> metric = (ScalarLinkQuality w) -> {
	    return w.getDistance();
	};

	OverUtilzedProblemGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow> overUtilizedProblemGenerator = new OverUtilzedProblemGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow>(
		manet, metric);

	OverUtilizedProblemProperties problemProperties = new OverUtilizedProblemProperties();
	problemProperties.pathCount = 1;
	problemProperties.minLength = 10;
	problemProperties.maxLength = 20;
	problemProperties.minDemand = new DataRate(100);
	problemProperties.maxDemand = new DataRate(200);
	problemProperties.overUtilizationPercentage = 5;
	problemProperties.uniqueSourceDestination = true;

	ScalarRadioFlow flowProblem = overUtilizedProblemGenerator.compute(problemProperties).get(0);

	assertEquals(0, flowProblem.getEdges().size());
    }
}
