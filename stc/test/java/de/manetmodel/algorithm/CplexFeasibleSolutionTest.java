package de.manetmodel.algorithm;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.SwingUtilities;

import org.junit.Test;

import de.jgraphlib.generator.GraphProperties.DoubleRange;
import de.jgraphlib.generator.GraphProperties.EdgeStyle;
import de.jgraphlib.generator.GraphProperties.IntRange;
import de.jgraphlib.generator.GridGraphGenerator;
import de.jgraphlib.generator.GridGraphProperties;
import de.jgraphlib.generator.NetworkGraphGenerator;
import de.jgraphlib.generator.NetworkGraphProperties;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.gui.printer.WeightedEdgeIDPrinter;
import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.evaluator.DoubleScope;
import de.manetmodel.evaluator.ScalarLinkQualityEvaluator;
import de.manetmodel.generator.OverUtilizedProblemProperties;
import de.manetmodel.generator.OverUtilzedProblemGenerator;
import de.manetmodel.mobilitymodel.PedestrianMobilityModel;
import de.manetmodel.network.scalar.ScalarLinkQuality;
import de.manetmodel.network.scalar.ScalarRadioFlow;
import de.manetmodel.network.scalar.ScalarRadioLink;
import de.manetmodel.network.scalar.ScalarRadioMANET;
import de.manetmodel.network.scalar.ScalarRadioMANETSupplier;
import de.manetmodel.network.scalar.ScalarRadioModel;
import de.manetmodel.network.scalar.ScalarRadioNode;
import de.manetmodel.units.DataRate;
import de.manetmodel.units.DataUnit.Type;
import de.manetmodel.units.Speed;
import de.manetmodel.units.Speed.SpeedRange;
import de.manetmodel.units.Unit;
import de.manetmodel.units.Watt;
import ilog.concert.IloException;

public class CplexFeasibleSolutionTest {

    @Test
    public void simpleTest() throws InvocationTargetException, InterruptedException, IloException, IOException {

	RandomNumbers randomNumbers = new RandomNumbers(0);
	
	ScalarRadioModel radioModel = new ScalarRadioModel(new Watt(0.001d), new Watt(1e-11), 2000000d, 2412000000d,
		35d, 100);
	PedestrianMobilityModel mobilityModel = new PedestrianMobilityModel(new RandomNumbers(),
		new SpeedRange(0, 100, Unit.TimeSteps.second, Unit.Distance.meter),
		new Speed(50, Unit.Distance.meter, Unit.TimeSteps.second));
	ScalarLinkQualityEvaluator evaluator = new ScalarLinkQualityEvaluator(new DoubleScope(0d, 1d), radioModel,
		mobilityModel);

	Supplier<ScalarLinkQuality> linkPropertySupplier = new ScalarRadioMANETSupplier().getLinkPropertySupplier();
	ScalarRadioMANET manet = new ScalarRadioMANET(new ScalarRadioMANETSupplier().getNodeSupplier(),
		new ScalarRadioMANETSupplier().getLinkSupplier(),
		linkPropertySupplier,
		new ScalarRadioMANETSupplier().getFlowSupplier(), radioModel, mobilityModel, evaluator);
		
	GridGraphProperties properties = new GridGraphProperties(/* playground width */ 1000,
		/* playground height */ 600, /* distance between vertices */
		100, /* length of edges */
		100, EdgeStyle.BIDIRECTIONAL);


	GridGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> generator = new GridGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(
		manet,linkPropertySupplier, new RandomNumbers(0));
	generator.generate(properties);

	manet.initialize();

	Function<ScalarLinkQuality, Double> metric = (ScalarLinkQuality w) -> {
	    return w.getDistance();
	};

	OverUtilzedProblemGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow> overUtilizedProblemGenerator = new OverUtilzedProblemGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow>(
		manet, metric);

	OverUtilizedProblemProperties problemProperties = new OverUtilizedProblemProperties(/* Number of paths */30,
		/* Minimum path length */10, /* Maximum path length */20,
		/* Minimum demand of each flow */new DataRate(10, Type.kilobit),
		/* Maximum demand of each flow */new DataRate(20, Type.kilobit),
		/* Unique source destination pairs */true, /* Over-utilization percentage */2,
		/* Increase factor of each tick */new DataRate(2, Type.kilobit));
	
	List<ScalarRadioFlow> flowProblems = overUtilizedProblemGenerator.compute(problemProperties);
	manet.addFlows(flowProblems);
	
	CplexFlowDistribution<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow> naiveOptimalFlowDistribution = 
		new CplexFlowDistribution<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow>();

	List<ScalarRadioFlow> flows = naiveOptimalFlowDistribution.generateFeasibleSolution(manet);

	for (ScalarRadioFlow scalarRadioFlow : flows) {
	    manet.deployFlow(scalarRadioFlow);
	}

	SwingUtilities.invokeAndWait(new VisualGraphApp<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(manet,
		new WeightedEdgeIDPrinter<ScalarRadioLink, ScalarLinkQuality>()));

	System.in.read();
    }

    @Test
    public void naiveOptimalFlowDistributionTest()
	    throws InvocationTargetException, InterruptedException, IloException, IOException {

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

	NetworkGraphProperties graphProperties = new NetworkGraphProperties(/* playground width */ 1024,
		/* playground height */ 768, /* number of vertices */ new IntRange(20, 20),
		/* distance between vertices */ new DoubleRange(50d, 100d),
		/* edge distance */ new DoubleRange(100d, 100d));

	RandomNumbers random = new RandomNumbers();

	NetworkGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> generator = new NetworkGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(
		manet, new ScalarRadioMANETSupplier().getLinkPropertySupplier(), random);

	generator.generate(graphProperties);

	manet.addFlow(manet.getFirstVertex(),
		manet.getVertices().get(random.getRandom(1, manet.getVertices().size() - 1)), new DataRate(100));
	manet.addFlow(manet.getFirstVertex(),
		manet.getVertices().get(random.getRandom(1, manet.getVertices().size() - 1)), new DataRate(100));
	manet.addFlow(manet.getFirstVertex(),
		manet.getVertices().get(random.getRandom(1, manet.getVertices().size() - 1)), new DataRate(100));
	manet.addFlow(manet.getFirstVertex(),
		manet.getVertices().get(random.getRandom(1, manet.getVertices().size() - 1)), new DataRate(100));
	manet.addFlow(manet.getFirstVertex(),
		manet.getVertices().get(random.getRandom(1, manet.getVertices().size() - 1)), new DataRate(100));

	manet.initialize();

	CplexFlowDistribution<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow> naiveOptimalFlowDistribution = new CplexFlowDistribution<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow>();

	List<ScalarRadioFlow> flows = naiveOptimalFlowDistribution.generateFeasibleSolution(manet);

	for (ScalarRadioFlow scalarRadioFlow : flows) {
	    manet.deployFlow(scalarRadioFlow);
	}

	SwingUtilities.invokeAndWait(new VisualGraphApp<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(manet,
		new WeightedEdgeIDPrinter<ScalarRadioLink, ScalarLinkQuality>()));

    }
}
