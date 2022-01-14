package de.manetmodel.utilization;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.SwingUtilities;

import org.junit.Assert;
import org.junit.Test;

import de.jgraphlib.generator.GraphProperties.EdgeStyle;
import de.jgraphlib.graph.algorithms.DijkstraShortestPath;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.generator.GridGraphGenerator;
import de.jgraphlib.generator.GridGraphProperties;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.gui.printer.WeightedEdgeIDPrinter;
import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.algorithm.CplexFlowDistribution;
import de.manetmodel.evaluator.DoubleScope;
import de.manetmodel.evaluator.IdealLinkQualityEvaluator;
import de.manetmodel.evaluator.ScalarLinkQualityEvaluator;
import de.manetmodel.generator.OverUtilizedProblemProperties;
import de.manetmodel.generator.OverUtilzedProblemGenerator;
import de.manetmodel.gui.printer.LinkUtilizationPrinter;
import de.manetmodel.mobilitymodel.PedestrianMobilityModel;
import de.manetmodel.network.ideal.IdealLinkQuality;
import de.manetmodel.network.ideal.IdealRadioFlow;
import de.manetmodel.network.ideal.IdealRadioLink;
import de.manetmodel.network.ideal.IdealRadioMANET;
import de.manetmodel.network.ideal.IdealRadioMANETSupplier;
import de.manetmodel.network.ideal.IdealRadioModel;
import de.manetmodel.network.ideal.IdealRadioNode;
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

public class UtilizationTest {

    @Test
    public void idealRadioNoUtilizationTest()
	    throws InvocationTargetException, InterruptedException, IloException, IOException {

	IdealRadioModel radioModel = new IdealRadioModel(new DataRate(5, Type.megabit), 35d, 100);
	PedestrianMobilityModel mobilityModel = new PedestrianMobilityModel(new RandomNumbers(),
		new SpeedRange(0, 100, Unit.TimeSteps.second, Unit.Distance.meter),
		new Speed(50, Unit.Distance.meter, Unit.TimeSteps.second));
	IdealLinkQualityEvaluator evaluator = new IdealLinkQualityEvaluator(new DoubleScope(0d, 1d), radioModel);
	
	Supplier<IdealLinkQuality> linkPropertySupplier = new IdealRadioMANETSupplier().getLinkPropertySupplier();
	IdealRadioMANET manet = new IdealRadioMANET(new IdealRadioMANETSupplier().getNodeSupplier(),
		new IdealRadioMANETSupplier().getLinkSupplier(), linkPropertySupplier,
		new IdealRadioMANETSupplier().getFlowSupplier(), radioModel, mobilityModel, null);
	GridGraphProperties properties = new GridGraphProperties(/* playground width */ 800,
		/* playground height */ 200, /* distance between vertices */
		100, /* length of edges */
		100, EdgeStyle.BIDIRECTIONAL);

	GridGraphGenerator<IdealRadioNode, IdealRadioLink, IdealLinkQuality> generator = new GridGraphGenerator<IdealRadioNode, IdealRadioLink, IdealLinkQuality>(
		manet, linkPropertySupplier, new RandomNumbers(0));
	generator.generate(properties);

	manet.initialize();

	Function<IdealLinkQuality, Double> metric = (IdealLinkQuality w) -> {
	    return w.getRelativeDistance();
	};


	
	DijkstraShortestPath<IdealRadioNode, IdealRadioLink, IdealLinkQuality> sp = new DijkstraShortestPath<IdealRadioNode, IdealRadioLink, IdealLinkQuality>(manet);
	IdealRadioFlow flow1 = new IdealRadioFlow(manet.getVertex(0),manet.getVertex(24),new DataRate(1,Type.megabit));
	flow1.update(sp.compute(manet.getVertex(0),manet.getVertex(24), metric));
	
	IdealRadioFlow flow2 = new IdealRadioFlow(manet.getVertex(0),manet.getVertex(24),new DataRate(1,Type.megabit));
	flow2.update(sp.compute(manet.getVertex(2),manet.getVertex(26), metric));
	
	
	
	manet.deployFlow(flow1);
	manet.deployFlow(flow2);

	SwingUtilities.invokeAndWait(new VisualGraphApp<IdealRadioNode, IdealRadioLink, IdealLinkQuality>(manet,
		new LinkUtilizationPrinter<IdealRadioLink, IdealLinkQuality>()));

	System.in.read();
	Assert.assertFalse(manet.isOverutilized());
    }

    @Test
    public void idealRadioOverUtilizationTest()
	    throws InvocationTargetException, InterruptedException, IloException, IOException {

	IdealRadioModel radioModel = new IdealRadioModel(new DataRate(5, Type.megabit), 35d, 100);
	PedestrianMobilityModel mobilityModel = new PedestrianMobilityModel(new RandomNumbers(),
		new SpeedRange(0, 100, Unit.TimeSteps.second, Unit.Distance.meter),
		new Speed(50, Unit.Distance.meter, Unit.TimeSteps.second));
	IdealLinkQualityEvaluator evaluator = new IdealLinkQualityEvaluator(new DoubleScope(0d, 1d), radioModel);
	
	Supplier<IdealLinkQuality> linkPropertySupplier = new IdealRadioMANETSupplier().getLinkPropertySupplier();
	IdealRadioMANET manet = new IdealRadioMANET(new IdealRadioMANETSupplier().getNodeSupplier(),
		new IdealRadioMANETSupplier().getLinkSupplier(), linkPropertySupplier,
		new IdealRadioMANETSupplier().getFlowSupplier(), radioModel, mobilityModel, null);
	GridGraphProperties properties = new GridGraphProperties(/* playground width */ 800,
		/* playground height */ 200, /* distance between vertices */
		100, /* length of edges */
		100, EdgeStyle.BIDIRECTIONAL);

	GridGraphGenerator<IdealRadioNode, IdealRadioLink, IdealLinkQuality> generator = new GridGraphGenerator<IdealRadioNode, IdealRadioLink, IdealLinkQuality>(
		manet, linkPropertySupplier, new RandomNumbers(0));
	generator.generate(properties);

	manet.initialize();

	Function<IdealLinkQuality, Double> metric = (IdealLinkQuality w) -> {
	    return w.getRelativeDistance();
	};


	
	DijkstraShortestPath<IdealRadioNode, IdealRadioLink, IdealLinkQuality> sp = new DijkstraShortestPath<IdealRadioNode, IdealRadioLink, IdealLinkQuality>(manet);
	IdealRadioFlow flow1 = new IdealRadioFlow(manet.getVertex(0),manet.getVertex(24),new DataRate(1,Type.megabit));
	flow1.update(sp.compute(manet.getVertex(0),manet.getVertex(24), metric));
	
	IdealRadioFlow flow2 = new IdealRadioFlow(manet.getVertex(0),manet.getVertex(24),new DataRate(1,Type.megabit));
	flow2.update(sp.compute(manet.getVertex(2),manet.getVertex(26), metric));
	
	
	IdealRadioFlow flow3 = new IdealRadioFlow(manet.getVertex(1),manet.getVertex(2),new DataRate(2,Type.megabit));
	flow3.update(sp.compute(manet.getVertex(1),manet.getVertex(2), metric));
	
	
	manet.deployFlow(flow1);
	manet.deployFlow(flow2);
	manet.deployFlow(flow3);

	SwingUtilities.invokeAndWait(new VisualGraphApp<IdealRadioNode, IdealRadioLink, IdealLinkQuality>(manet,
		new LinkUtilizationPrinter<IdealRadioLink, IdealLinkQuality>()));

	System.in.read();
	Assert.assertFalse(manet.isOverutilized());
    }
}
