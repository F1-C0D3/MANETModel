package de.manetmodel.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
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
import de.jgraphlib.graph.generator.GridGraphGenerator;
import de.jgraphlib.graph.generator.GridGraphProperties;
import de.jgraphlib.graph.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Tuple;
import de.manetmodel.evaluator.DoubleScope;
import de.manetmodel.evaluator.ScalarLinkQualityEvaluator;
import de.manetmodel.gui.printer.LinkUtilizationPrinter;
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
import de.manetmodel.units.DataUnit.Type;

public class OverUtilzedProblemGeneratorTest {

    @Test
    public void overUtilzedProblemGeneratorPenetrationTest()
	    throws IOException, InvocationTargetException, InterruptedException {

	int runs = 200;
	RandomNumbers random = new RandomNumbers(0);

	while (runs != 0) {
	    ScalarRadioModel radioModel = new ScalarRadioModel(new Watt(0.001d), new Watt(1e-11), 2000000d, 2412000000d,
		    35d, 100);
	    PedestrianMobilityModel mobilityModel = new PedestrianMobilityModel(random,
		    new SpeedRange(0, 100, Unit.TimeSteps.second, Unit.Distance.meter),
		    new Speed(50, Unit.Distance.meter, Unit.TimeSteps.second));
	    ScalarLinkQualityEvaluator evaluator = new ScalarLinkQualityEvaluator(new DoubleScope(0d, 1d), radioModel,
		    mobilityModel);

	    ScalarRadioMANET manet = new ScalarRadioMANET(new ScalarRadioMANETSupplier().getNodeSupplier(),
			new ScalarRadioMANETSupplier().getLinkSupplier(),
			new ScalarRadioMANETSupplier().getLinkPropertySupplier(),
			new ScalarRadioMANETSupplier().getFlowSupplier(), radioModel, mobilityModel, evaluator);

		NetworkGraphProperties graphProperties = new NetworkGraphProperties(1024, 768, new IntRange(100, 100),
			new DoubleRange(50d, 100d), new DoubleRange(100d, 100d));

		NetworkGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> generator = new NetworkGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(
			manet, new ScalarRadioMANETSupplier().getLinkPropertySupplier(), random);

		generator.generate(graphProperties);
		manet.initialize();
	    Function<ScalarLinkQuality, Double> metric = (ScalarLinkQuality w) -> {
		return w.getScore();
	    };

	    OverUtilzedProblemGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow> overUtilizedProblemGenerator = new OverUtilzedProblemGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow>(
		    manet, metric);

		OverUtilizedProblemProperties problemProperties = new OverUtilizedProblemProperties(
			/* Number of paths */10, /* Minimum path length */10,
			/* Maximum path length */20, /* Minimum demand of each flow */new DataRate(100, Type.kilobit),
			/* Maximum demand of each flow */new DataRate(200, Type.kilobit),
			/* Unique source destination pairs */true,
			/* Over-utilization percentage */10,
			/* Increase factor of each tick */new DataRate(50, Type.kilobit));
	    List<ScalarRadioFlow> flowProblems = overUtilizedProblemGenerator.compute(problemProperties,random);

	    DijkstraShortestPath<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> sp = new DijkstraShortestPath<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(
		    manet);
	    manetOverUtilizedDueToSeveralFlowsInternalTest(manet, flowProblems, sp, metric);
	    runs--;
	}

    }

    @Test
    public void simpleSingleFlowProblemGeneratorTestGridMANET()
	    throws IOException, InvocationTargetException, InterruptedException {

	ScalarRadioModel radioModel = new ScalarRadioModel(new Watt(0.001d), new Watt(1e-11), 2000000d, 2412000000d,
		35d, 100);
	PedestrianMobilityModel mobilityModel = new PedestrianMobilityModel(new RandomNumbers(),
		new SpeedRange(0, 100, Unit.TimeSteps.second, Unit.Distance.meter),
		new Speed(50, Unit.Distance.meter, Unit.TimeSteps.second));
	ScalarLinkQualityEvaluator evaluator = new ScalarLinkQualityEvaluator(new DoubleScope(0d, 1d), radioModel,
		mobilityModel);

	ScalarRadioMANET manet = new ScalarRadioMANET(new ScalarRadioMANETSupplier().getNodeSupplier(),
		new ScalarRadioMANETSupplier().getLinkSupplier(),
		new ScalarRadioMANETSupplier().getLinkPropertySupplier(),
		new ScalarRadioMANETSupplier().getFlowSupplier(), radioModel, mobilityModel, evaluator);

	GridGraphProperties graphProperties = new GridGraphProperties(/* playground width */ 300,
		/* playground height */ 100, /* distance between vertices */ 100, /* length of edges */ 100);

	RandomNumbers random = new RandomNumbers(0);

	GridGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> generator = new GridGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(
		manet, random);
	generator.generate(graphProperties);
	manet.initialize();
	Function<ScalarLinkQuality, Double> metric = (ScalarLinkQuality w) -> {
	    return w.getScore();
	};

	OverUtilzedProblemGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow> overUtilizedProblemGenerator = new OverUtilzedProblemGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow>(
		manet, metric);

	OverUtilizedProblemProperties problemProperties = new OverUtilizedProblemProperties(
		/* Number of paths */1, /* Minimum path length */10,
		/* Maximum path length */20, /* Minimum demand of each flow */new DataRate(10, Type.kilobit),
		/* Maximum demand of each flow */new DataRate(20, Type.kilobit),
		/* Unique source destination pairs */true,
		/* Over-utilization percentage */2,
		/* Increase factor of each tick */new DataRate(2, Type.kilobit));
	List<ScalarRadioFlow> flowProblems = overUtilizedProblemGenerator.compute(problemProperties);

	assertEquals(flowProblems.get(0).size(), 1);
    }

    @Test
    public void overUtilzedProblemGeneratorTest() throws IOException, InvocationTargetException, InterruptedException {

	ScalarRadioModel radioModel = new ScalarRadioModel(new Watt(0.001d), new Watt(1e-11), 2000000d, 2412000000d,
		35d, 100);
	PedestrianMobilityModel mobilityModel = new PedestrianMobilityModel(new RandomNumbers(),
		new SpeedRange(0, 100, Unit.TimeSteps.second, Unit.Distance.meter),
		new Speed(50, Unit.Distance.meter, Unit.TimeSteps.second));
	ScalarLinkQualityEvaluator evaluator = new ScalarLinkQualityEvaluator(new DoubleScope(0d, 1d), radioModel,
		mobilityModel);

	ScalarRadioMANET manet = new ScalarRadioMANET(new ScalarRadioMANETSupplier().getNodeSupplier(),
		new ScalarRadioMANETSupplier().getLinkSupplier(),
		new ScalarRadioMANETSupplier().getLinkPropertySupplier(),
		new ScalarRadioMANETSupplier().getFlowSupplier(), radioModel, mobilityModel, evaluator);

	GridGraphProperties graphProperties = new GridGraphProperties(/* playground width */ 300,
		/* playground height */ 100, /* distance between vertices */ 100, /* length of edges */ 100);

	RandomNumbers random = new RandomNumbers(0);

	GridGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> generator = new GridGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(
		manet, random);
	generator.generate(graphProperties);
	manet.initialize();
	Function<ScalarLinkQuality, Double> metric = (ScalarLinkQuality w) -> {
	    return w.getScore();
	};

	OverUtilzedProblemGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow> overUtilizedProblemGenerator = new OverUtilzedProblemGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow>(
		manet, metric);

	OverUtilizedProblemProperties problemProperties = new OverUtilizedProblemProperties(
		/* Number of paths */2, /* Minimum path length */10,
		/* Maximum path length */20, /* Minimum demand of each flow */new DataRate(10, Type.kilobit),
		/* Maximum demand of each flow */new DataRate(20, Type.kilobit),
		/* Unique source destination pairs */true,
		/* Over-utilization percentage */2,
		/* Increase factor of each tick */new DataRate(2, Type.kilobit));
	List<ScalarRadioFlow> flowProblems = overUtilizedProblemGenerator.compute(problemProperties);

	DijkstraShortestPath<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> sp = new DijkstraShortestPath<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(
		manet);
	manetOverUtilizedDueToSeveralFlowsInternalTest(manet, flowProblems, sp, metric);

    }

    @Test
    public void generateUniqueSourceDestinationNodes() throws InvocationTargetException, InterruptedException {

	ScalarRadioModel radioModel = new ScalarRadioModel(new Watt(0.001d), new Watt(1e-11), 2000000d, 2412000000d,
		35d, 100);
	PedestrianMobilityModel mobilityModel = new PedestrianMobilityModel(new RandomNumbers(),
		new SpeedRange(0, 100, Unit.TimeSteps.second, Unit.Distance.meter),
		new Speed(50, Unit.Distance.meter, Unit.TimeSteps.second));
	ScalarLinkQualityEvaluator evaluator = new ScalarLinkQualityEvaluator(new DoubleScope(0d, 1d), radioModel,
		mobilityModel);

	ScalarRadioMANET manet = new ScalarRadioMANET(new ScalarRadioMANETSupplier().getNodeSupplier(),
		new ScalarRadioMANETSupplier().getLinkSupplier(),
		new ScalarRadioMANETSupplier().getLinkPropertySupplier(),
		new ScalarRadioMANETSupplier().getFlowSupplier(), radioModel, mobilityModel, evaluator);

	NetworkGraphProperties graphProperties = new NetworkGraphProperties(1024, 768, new IntRange(100, 100),
		new DoubleRange(50d, 100d), new DoubleRange(100d, 100d));

	NetworkGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> generator = new NetworkGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(
		manet, new ScalarRadioMANETSupplier().getLinkPropertySupplier(), new RandomNumbers());

	generator.generate(graphProperties);
	manet.initialize();

	Function<ScalarLinkQuality, Double> metric = (ScalarLinkQuality w) -> {
	    return w.getScore();
	};

	OverUtilzedProblemGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow> overUtilizedProblemGenerator = new OverUtilzedProblemGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow>(
		manet, metric);

	OverUtilizedProblemProperties problemProperties = new OverUtilizedProblemProperties(
		/* Number of paths */20, /* Minimum path length */10,
		/* Maximum path length */20, /* Minimum demand of each flow */new DataRate(10, Type.kilobit),
		/* Maximum demand of each flow */new DataRate(20, Type.kilobit),
		/* Unique source destination pairs */true,
		/* Over-utilization percentage */2,
		/* Increase factor of each tick */new DataRate(2, Type.kilobit));

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
    }

    @Test
    public void verifyAccurateOverUtilizationPercentage() throws InvocationTargetException, InterruptedException {

	ScalarRadioModel radioModel = new ScalarRadioModel(new Watt(0.001d), new Watt(1e-11), 2000000d, 2412000000d,
		35d, 100);
	PedestrianMobilityModel mobilityModel = new PedestrianMobilityModel(new RandomNumbers(),
		new SpeedRange(0, 100, Unit.TimeSteps.second, Unit.Distance.meter),
		new Speed(50, Unit.Distance.meter, Unit.TimeSteps.second));
	ScalarLinkQualityEvaluator evaluator = new ScalarLinkQualityEvaluator(new DoubleScope(0d, 1d), radioModel,
		mobilityModel);

	ScalarRadioMANET manet = new ScalarRadioMANET(new ScalarRadioMANETSupplier().getNodeSupplier(),
		new ScalarRadioMANETSupplier().getLinkSupplier(),
		new ScalarRadioMANETSupplier().getLinkPropertySupplier(),
		new ScalarRadioMANETSupplier().getFlowSupplier(), radioModel, mobilityModel, evaluator);

	NetworkGraphProperties graphProperties = new NetworkGraphProperties(1024, 768, new IntRange(100, 100),
		new DoubleRange(50d, 100d), new DoubleRange(100d, 100d));

	NetworkGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> generator = new NetworkGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(
		manet, new ScalarRadioMANETSupplier().getLinkPropertySupplier(), new RandomNumbers());

	generator.generate(graphProperties);
	manet.initialize();

	Function<ScalarLinkQuality, Double> metric = (ScalarLinkQuality w) -> {
	    return w.getDistance();
	};

	OverUtilzedProblemGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow> overUtilizedProblemGenerator = new OverUtilzedProblemGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow>(
		manet, metric);

	DijkstraShortestPath<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> sp = new DijkstraShortestPath<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(
		manet);

	OverUtilizedProblemProperties problemProperties = new OverUtilizedProblemProperties(
		/* Number of paths */10, /* Minimum path length */10,
		/* Maximum path length */20, /* Minimum demand of each flow */new DataRate(100, Type.kilobit),
		/* Maximum demand of each flow */new DataRate(200, Type.kilobit),
		/* Unique source destination pairs */true,
		/* Over-utilization percentage */1,
		/* Increase factor of each tick */new DataRate(50, Type.kilobit));

	List<ScalarRadioFlow> flowProblems = overUtilizedProblemGenerator.compute(problemProperties);
	double previousOverUtilizedPercentage = 0d;
	manetOverUtilizedDueToSeveralFlowsInternalTest(manet, flowProblems, sp, metric);
	assertTrue(manet
		.getOverUtilizationPercentageOf(manet.getOverUtilizedActiveLinks()) > previousOverUtilizedPercentage);
	previousOverUtilizedPercentage = manet.getOverUtilizationPercentageOf(manet.getOverUtilizedActiveLinks());

	manet.reset();
	for (ScalarRadioFlow scalarRadioFlow : flowProblems) {
	    scalarRadioFlow.setDataRate(new DataRate(0));
	}
	problemProperties.setOverUtilizationPercentage(10);
	flowProblems = overUtilizedProblemGenerator.compute(problemProperties);
	manetOverUtilizedDueToSeveralFlowsInternalTest(manet, flowProblems, sp, metric);
	assertTrue(manet
		.getOverUtilizationPercentageOf(manet.getOverUtilizedActiveLinks()) > previousOverUtilizedPercentage);
	previousOverUtilizedPercentage = manet.getOverUtilizationPercentageOf(manet.getOverUtilizedActiveLinks());

	manet.reset();
	for (ScalarRadioFlow scalarRadioFlow : flowProblems) {
	    scalarRadioFlow.setDataRate(new DataRate(0));
	}
	problemProperties.setOverUtilizationPercentage(20);
	flowProblems = overUtilizedProblemGenerator.compute(problemProperties);
	manetOverUtilizedDueToSeveralFlowsInternalTest(manet, flowProblems, sp, metric);
	assertTrue(manet
		.getOverUtilizationPercentageOf(manet.getOverUtilizedActiveLinks()) > previousOverUtilizedPercentage);
	previousOverUtilizedPercentage = manet.getOverUtilizationPercentageOf(manet.getOverUtilizedActiveLinks());
	manet.reset();

    }

    private void manetOverUtilizedDueToSeveralFlowsInternalTest(ScalarRadioMANET manet,
	    List<ScalarRadioFlow> flowProblems,
	    DijkstraShortestPath<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> sp,
	    Function<ScalarLinkQuality, Double> metric) {

	for (ScalarRadioFlow scalarRadioFlow : flowProblems) {
	    DataRate dataRate = scalarRadioFlow.getDataRate();
	    scalarRadioFlow.update(sp.compute(scalarRadioFlow.getSource(), scalarRadioFlow.getTarget(), metric));

	    scalarRadioFlow.setDataRate(dataRate);
	    manet.addFlow(scalarRadioFlow);
	    manet.deployFlow(scalarRadioFlow);

	    assertFalse(manet.isOverutilized());

	    manet.reset();
	}

	manet.reset();

	for (ScalarRadioFlow scalarRadioFlow : flowProblems) {
	    DataRate dataRate = scalarRadioFlow.getDataRate();
	    scalarRadioFlow.update(sp.compute(scalarRadioFlow.getSource(), scalarRadioFlow.getTarget(), metric));

	    scalarRadioFlow.setDataRate(dataRate);
	    manet.addFlow(scalarRadioFlow);
	    manet.deployFlow(scalarRadioFlow);
	}

	assertTrue(manet.isOverutilized());
    }
}
