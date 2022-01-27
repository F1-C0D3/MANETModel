package de.manetmodel.evaluator;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.Supplier;

import javax.swing.SwingUtilities;

import org.junit.Test;

import de.jgraphlib.generator.GraphProperties.EdgeStyle;
import de.jgraphlib.generator.GridGraphGenerator;
import de.jgraphlib.generator.GridGraphProperties;
import de.jgraphlib.graph.algorithms.DijkstraShortestPath;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.util.RandomNumbers;
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
import de.manetmodel.units.DataUnit.Type;
import de.manetmodel.units.Speed;
import de.manetmodel.units.Speed.SpeedRange;
import de.manetmodel.units.Unit;
import de.manetmodel.units.Watt;

public class FlowDistributionEvaluatorTest {

    @Test
    public void flowDistributionEvaluatorTest() throws InvocationTargetException, InterruptedException, IOException {
	
	/**************************************************************************************************************************************/
	ScalarRadioModel radioModel = new ScalarRadioModel(new Watt(0.001d), new Watt(1e-11), 2000000d, 2412000000d, 35d,100);
	PedestrianMobilityModel mobilityModel = new PedestrianMobilityModel(
		new RandomNumbers(), 
		new SpeedRange(0, 100, Unit.TimeSteps.second, Unit.Distance.meter), 
		new Speed(50, Unit.Distance.meter, Unit.TimeSteps.second));
			
	ScalarLinkQualityEvaluator linkQualityEvaluator = 
		new ScalarLinkQualityEvaluator(new DoubleScope(0d, 1d), radioModel, mobilityModel);
	

	Supplier<ScalarLinkQuality> linkPropertySupplier = new ScalarRadioMANETSupplier().getLinkPropertySupplier();
	ScalarRadioMANET manet = new ScalarRadioMANET(new ScalarRadioMANETSupplier().getNodeSupplier(),
		new ScalarRadioMANETSupplier().getLinkSupplier(),
		linkPropertySupplier,
		new ScalarRadioMANETSupplier().getFlowSupplier(),
		radioModel, 
		mobilityModel,
		linkQualityEvaluator);
	
	/**************************************************************************************************************************************/
	
	GridGraphProperties graphProperties = new GridGraphProperties(
		/* playground width */ 			500,
		/* playground height */ 		500, 
		/* distance between vertices */ 	100, 
		/* length of edges */ 			100,
		/* Style of edges*/			EdgeStyle.BIDIRECTIONAL);	


	RandomNumbers random = new RandomNumbers();

	GridGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> generator = 
		new GridGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(
			manet, linkPropertySupplier, random);

	generator.generate(graphProperties);

	manet.initialize();
	
	/**************************************************************************************************************************************/	
		
	DijkstraShortestPath<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> dijkstraShortestPath = 
		new DijkstraShortestPath<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(manet);
	
	ScalarRadioFlow flow1 = new ScalarRadioFlow(manet.getVertex(1), manet.getVertex(31), new DataRate(0.5, Type.kilobit));
	flow1.update(dijkstraShortestPath.compute(flow1.getSource(), flow1.getTarget(), (ScalarLinkQuality w) -> { return w.getScore();}));
	manet.addFlow(flow1);
	manet.deployFlow(flow1);
	
	ScalarRadioFlow flow2 = new ScalarRadioFlow(manet.getVertex(1), manet.getVertex(31), new DataRate(0.5, Type.kilobit));
	flow2.update(dijkstraShortestPath.compute(flow2.getSource(), flow2.getTarget(), (ScalarLinkQuality w) -> { return w.getScore();}));
	manet.addFlow(flow2);
	manet.deployFlow(flow2);
	
	/*ScalarRadioFlow flow3 = new ScalarRadioFlow(manet.getVertex(1), manet.getVertex(31), new DataRate(1, Type.kilobit));
	flow3.update(dijkstraShortestPath.compute(flow3.getSource(), flow3.getTarget(), (ScalarLinkQuality w) -> { return w.getScore();}));
	manet.addFlow(flow3);
	manet.deployFlow(flow3);*/

	FlowDistributionEvaluator<ScalarRadioLink, ScalarRadioFlow> flowDistributionEvaluator = 
		new FlowDistributionEvaluator<ScalarRadioLink, ScalarRadioFlow>(new DoubleScope(0,1), linkQualityEvaluator, 1, 2);
			
	for(Map.Entry<ScalarRadioFlow, Double> entry : flowDistributionEvaluator.evaluate(manet).entrySet()) {  
	    
	    double score = 0;
	    
	    for(ScalarRadioLink link : entry.getKey().getEdges()) 
		score += link.getWeight().getScore();
	    
	    System.out.println(String.format("flow %d mergedScore %.2f score %.2f", entry.getKey().getID(), entry.getValue(), score));
	}
	
	SwingUtilities.invokeAndWait(new VisualGraphApp<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(manet, new LinkUtilizationPrinter<ScalarRadioLink, ScalarLinkQuality>()));
	
	System.in.read();
    }   
}
