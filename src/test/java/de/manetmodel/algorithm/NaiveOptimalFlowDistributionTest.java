package de.manetmodel.algorithm;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.junit.Test;

import de.jgraphlib.generator.GraphProperties.DoubleRange;
import de.jgraphlib.generator.GraphProperties.IntRange;
import de.jgraphlib.generator.NetworkGraphGenerator;
import de.jgraphlib.generator.NetworkGraphProperties;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.gui.printer.WeightedEdgeIDPrinter;
import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.evaluator.DoubleScope;
import de.manetmodel.evaluator.ScalarLinkQualityEvaluator;
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


public class NaiveOptimalFlowDistributionTest {

    @Test
    public void naiveOptimalFlowDistributionTest() throws InvocationTargetException, InterruptedException {
	ScalarRadioModel radioModel = new ScalarRadioModel(new Watt(0.001d), new Watt(1e-11), 2000000d, 2412000000d,
		35d,100);
	PedestrianMobilityModel mobilityModel = new PedestrianMobilityModel(new RandomNumbers(), new SpeedRange(0, 100, Unit.TimeSteps.second, Unit.Distance.meter), new Speed(50, Unit.Distance.meter, Unit.TimeSteps.second));
	ScalarLinkQualityEvaluator evaluator = new ScalarLinkQualityEvaluator(new DoubleScope(0d, 1d), radioModel,
		mobilityModel);
	
	
	ScalarRadioMANET manet = new ScalarRadioMANET(new ScalarRadioMANETSupplier().getNodeSupplier(),
		new ScalarRadioMANETSupplier().getLinkSupplier(),
		new ScalarRadioMANETSupplier().getLinkPropertySupplier(),
		new ScalarRadioMANETSupplier().getFlowSupplier(),
		radioModel, 
		mobilityModel,
		evaluator);
	
	NetworkGraphProperties graphProperties = new NetworkGraphProperties(
		/* playground width */ 		1024,
		/* playground height */ 	768, 
		/* number of vertices */ 	new IntRange(20,20),
		/* distance between vertices */ new DoubleRange(50d, 100d),
		/* edge distance */ 		new DoubleRange(100d, 100d));

	RandomNumbers random = new RandomNumbers();
	
	NetworkGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> generator = 
		new NetworkGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(
			manet, new ScalarRadioMANETSupplier().getLinkPropertySupplier(), random);
	
	generator.generate(graphProperties);
	
	manet.addFlow(manet.getFirstVertex(), manet.getVertices().get(random.getRandom(1, manet.getVertices().size()-1)), new DataRate(100));
	manet.addFlow(manet.getFirstVertex(), manet.getVertices().get(random.getRandom(1, manet.getVertices().size()-1)), new DataRate(100));
	manet.addFlow(manet.getFirstVertex(), manet.getVertices().get(random.getRandom(1, manet.getVertices().size()-1)), new DataRate(100));
	manet.addFlow(manet.getFirstVertex(), manet.getVertices().get(random.getRandom(1, manet.getVertices().size()-1)), new DataRate(100));
	manet.addFlow(manet.getFirstVertex(), manet.getVertices().get(random.getRandom(1, manet.getVertices().size()-1)), new DataRate(100));

	manet.initialize();
		
	SwingUtilities.invokeAndWait(new VisualGraphApp<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(
		manet, new WeightedEdgeIDPrinter<ScalarRadioLink, ScalarLinkQuality>()));
	
	NaiveOptimalFlowDistribution<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow> naiveOptimalFlowDistribution = 
		new NaiveOptimalFlowDistribution<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality, ScalarRadioFlow>();

	naiveOptimalFlowDistribution.generateFeasibleDistributions(manet);
    }  
}
