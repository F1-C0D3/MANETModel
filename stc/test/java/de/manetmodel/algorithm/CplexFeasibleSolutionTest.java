package de.manetmodel.algorithm;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Function;

import javax.swing.SwingUtilities;

import org.junit.Test;

import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.graph.generator.GridGraphGenerator;
import de.jgraphlib.graph.generator.GridGraphProperties;
import de.jgraphlib.graph.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;
import de.jgraphlib.graph.suppliers.EdgeDistanceSupplier;
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
import de.manetmodel.units.Unit;
import de.manetmodel.units.Watt;
import ilog.concert.IloException;
import de.manetmodel.units.Speed.SpeedRange;

public class CplexFeasibleSolutionTest {

    @Test
    public void simpleTest() throws InvocationTargetException, InterruptedException, IloException, IOException {

	ScalarRadioModel radioModel = new ScalarRadioModel(new Watt(0.001d), new Watt(1e-11), 2000000d, 2412000000d,
		35d,100);
	PedestrianMobilityModel mobilityModel = new PedestrianMobilityModel(new RandomNumbers(),
		new SpeedRange(0, 100, Unit.TimeSteps.second, Unit.Distance.meter),
		new Speed(50, Unit.Distance.meter, Unit.TimeSteps.second));
	ScalarLinkQualityEvaluator evaluator = new ScalarLinkQualityEvaluator(new DoubleScope(0d, 1d), radioModel,
		mobilityModel);

	ScalarRadioMANET manet = new ScalarRadioMANET(new ScalarRadioMANETSupplier().getNodeSupplier(),
		new ScalarRadioMANETSupplier().getLinkSupplier(),
		new ScalarRadioMANETSupplier().getLinkPropertySupplier(),
		new ScalarRadioMANETSupplier().getFlowSupplier(), radioModel, mobilityModel, evaluator);
	RandomNumbers randomNumbers = new RandomNumbers(0);
	GridGraphProperties properties = new GridGraphProperties(/* playground width */ 1000,
		/* playground height */ 600, /* distance between vertices */
		100, /* length of edges */
		100);

	GridGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> generator = new GridGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(
		manet, new RandomNumbers(0));
	generator.generate(properties);

	manet.initialize();
	
	Function<ScalarLinkQuality, Double> metric = (ScalarLinkQuality w) -> {
		return w.getDistance();
	};

	OverUtilzedProblemGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow> overUtilizedProblemGenerator = new OverUtilzedProblemGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow>(
			manet, metric);

	OverUtilizedProblemProperties problemProperties = new OverUtilizedProblemProperties();
	problemProperties.pathCount = 30;
	problemProperties.minLength = 10;
	problemProperties.maxLength = 20;
	problemProperties.minDemand = new DataRate(200, Type.kilobit);
	problemProperties.maxDemand = new DataRate(400, Type.kilobit);
	problemProperties.increaseFactor = new DataRate(200, Type.kilobit);
	problemProperties.overUtilizationPercentage = 1;
	problemProperties.uniqueSourceDestination = true;
	List<ScalarRadioFlow> flowProblems = overUtilizedProblemGenerator.compute(problemProperties, randomNumbers);
	manet.addFlows(flowProblems);
	CplexFlowDistribution<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow> naiveOptimalFlowDistribution = new CplexFlowDistribution<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow>();

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
		35d,100);
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
