package de.manetmodel.scenarios;

import java.util.function.Function;

import de.jgraphlib.graph.algorithms.DijkstraShortestPath;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.io.VertextPosition2DMapper;
import de.jgraphlib.graph.io.XMLImporter;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.evaluator.DoubleScope;
import de.manetmodel.evaluator.ScalarLinkQualityEvaluator;
import de.manetmodel.gui.printer.LinkQualityScorePrinter;
import de.manetmodel.mobilitymodel.PedestrianMobilityModel;
import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANET;
import de.manetmodel.network.MANETSupplier;
import de.manetmodel.network.Node;
import de.manetmodel.network.scalar.ScalarLinkQuality;
import de.manetmodel.network.scalar.ScalarRadioFlow;
import de.manetmodel.network.scalar.ScalarRadioLink;
import de.manetmodel.network.scalar.ScalarRadioMANET;
import de.manetmodel.network.scalar.ScalarRadioMANETSupplier;
import de.manetmodel.network.scalar.ScalarRadioModel;
import de.manetmodel.network.scalar.ScalarRadioNode;
import de.manetmodel.radiomodel.RadioModel;
import de.manetmodel.units.DataRate;
import de.manetmodel.units.Speed;
import de.manetmodel.units.Time;
import de.manetmodel.units.Unit;
import de.manetmodel.units.Watt;
import de.manetmodel.units.DataUnit.Type;
import de.manetmodel.units.Speed.SpeedRange;

public class NetworkTopology_1024x786 {

    public static void main(String args[]) {

	ScalarRadioModel radioModel = new ScalarRadioModel(new Watt(0.002d), new Watt(1e-11), 1000d, 2412000000d,/** maxCommunicationRange **/ 100d);
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

	XMLImporter<ScalarRadioNode, Position2D, ScalarRadioLink, ScalarLinkQuality> importer = 
		new XMLImporter<ScalarRadioNode, Position2D, ScalarRadioLink, ScalarLinkQuality>(
			manet, new VertextPosition2DMapper());

	importer.importGraph("xml/NetworkTopology_1024x786_100N_2021-07-31_09:55:37.120.xml");

	manet.initialize();

	DijkstraShortestPath<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> sp = 
		new DijkstraShortestPath<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(manet);

	Function<ScalarLinkQuality, Double> metric2 = (ScalarLinkQuality q) -> {return (double) q.getDistance();};

	ScalarRadioFlow flow1 = new ScalarRadioFlow(manet.getVertex(0), manet.getVertex(69), new DataRate(1500000));
	
	sp.compute(flow1.getSource(), flow1.getTarget(), metric2);
	
	manet.addFlow((ScalarRadioFlow) flow1);
	manet.deployFlow(flow1);

	VisualGraphApp<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> visualGraphApp = new VisualGraphApp<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(manet);
    }
}
