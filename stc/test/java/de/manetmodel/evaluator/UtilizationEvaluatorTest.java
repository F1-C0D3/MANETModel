package de.manetmodel.evaluator;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import de.jgraphlib.graph.algorithms.DijkstraShortestPath;
import de.jgraphlib.graph.generator.GridGraphGenerator;
import de.jgraphlib.graph.generator.GridGraphProperties;
import de.jgraphlib.util.RandomNumbers;
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
import de.manetmodel.units.Speed.SpeedRange;

public class UtilizationEvaluatorTest {

    @Test
    public void utilizationEvaluatorTest() throws InvocationTargetException, InterruptedException, IOException {
	
	/**************************************************************************************************************************************/
	ScalarRadioModel radioModel = new ScalarRadioModel(
		new Watt(0.002d), 
		new Watt(1e-11), 
		1000d, 
		2412000000d,
		100d);
	
	PedestrianMobilityModel mobilityModel = new PedestrianMobilityModel(
		new RandomNumbers(), 
		new SpeedRange(0, 100, Unit.TimeSteps.second, Unit.Distance.meter), 
		new Speed(50, Unit.Distance.meter, Unit.TimeSteps.second));
			
	ScalarRadioMANET manet = new ScalarRadioMANET(new ScalarRadioMANETSupplier().getNodeSupplier(),
		new ScalarRadioMANETSupplier().getLinkSupplier(),
		new ScalarRadioMANETSupplier().getLinkPropertySupplier(),
		new ScalarRadioMANETSupplier().getFlowSupplier(),
		radioModel, 
		mobilityModel,
		new ScalarLinkQualityEvaluator(new DoubleScope(0d, 1d), radioModel, mobilityModel));
	
	/**************************************************************************************************************************************/
	
	GridGraphProperties graphProperties = new GridGraphProperties(
		/* playground width */ 			500,
		/* playground height */ 		500, 
		/* distance between vertices */ 	100, 
		/* length of edges */ 			100);	

	RandomNumbers random = new RandomNumbers();

	GridGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> generator = 
		new GridGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(
			manet, random);

	generator.generate(graphProperties);

	manet.initialize();
	
	/**************************************************************************************************************************************/	
		
	ScalarRadioFlow flow1 = new ScalarRadioFlow(manet.getFirstVertex(), manet.getLastVertex(), new DataRate(1, Type.kilobit));
	ScalarRadioFlow flow2 = new ScalarRadioFlow(manet.getFirstVertex(), manet.getLastVertex(), new DataRate(2, Type.kilobit));
	ScalarRadioFlow flow3 = new ScalarRadioFlow(manet.getFirstVertex(), manet.getLastVertex(), new DataRate(3, Type.kilobit));

	manet.addFlow(flow1);
	manet.addFlow(flow2);
	manet.addFlow(flow3);
	
	UtilizationEvaluator utilizationEvaluator = new UtilizationEvaluator(new DoubleScope(0,1), 1, manet);
	
	System.out.println(String.format("minDemand: %s", utilizationEvaluator.getMinDemand()));
	System.out.println(String.format("maxDemand: %s", utilizationEvaluator.getMaxDemand()));
	
	System.out.println(utilizationEvaluator.getScore(new DataRate(0, Type.kilobit).get()));
	System.out.println(utilizationEvaluator.getScore(new DataRate(2, Type.kilobit).get()));
	System.out.println(utilizationEvaluator.getScore(new DataRate(3, Type.kilobit).get()));
	System.out.println(utilizationEvaluator.getScore(new DataRate(6, Type.kilobit).get()));
    }   
}
