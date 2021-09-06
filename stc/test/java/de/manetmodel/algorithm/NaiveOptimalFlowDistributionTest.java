package de.manetmodel.algorithm;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.junit.Test;

import de.jgraphlib.graph.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.gui.printer.WeightedEdgeIDPrinter;
import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.example.elements.ScalarLinkQuality;
import de.manetmodel.example.elements.ScalarRadioFlow;
import de.manetmodel.example.elements.ScalarRadioLink;
import de.manetmodel.example.elements.ScalarRadioNode;
import de.manetmodel.example.network.ScalarRadioMANET;
import de.manetmodel.example.network.ScalarRadioMANETSupplier;
import de.manetmodel.example.radio.ScalarRadioModel;
import de.manetmodel.network.unit.DataRate;
import de.manetmodel.network.unit.Watt;


public class NaiveOptimalFlowDistributionTest {

    @Test
    public void naiveOptimalFlowDistributionTest() throws InvocationTargetException, InterruptedException {
	
	ScalarRadioMANET manet = new ScalarRadioMANET(
			new ScalarRadioMANETSupplier().getNodeSupplier(),
			new ScalarRadioMANETSupplier().getLinkSupplier(),
			new ScalarRadioMANETSupplier().getLinkPropertySupplier(), 
			new ScalarRadioMANETSupplier().getFlowSupplier(),
			new ScalarRadioModel(new Watt(0.002d), new Watt(1e-11), 1000d, 2412000000d), null, null);
	
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
