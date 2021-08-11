package de.manetmodel.scenarios;

import java.util.function.Function;

import de.jgraphlib.graph.algorithms.DijkstraShortestPath;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.io.VertextPosition2DMapper;
import de.jgraphlib.graph.io.XMLImporter;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.gui.LinkQualityPrinter;
import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANET;
import de.manetmodel.network.MANETSupplier;
import de.manetmodel.network.Node;
import de.manetmodel.network.mobility.PedestrianMobilityModel;
import de.manetmodel.network.radio.ScalarRadioModel;
import de.manetmodel.network.unit.DataRate;
import de.manetmodel.network.unit.Speed;
import de.manetmodel.network.unit.Time;
import de.manetmodel.network.unit.Unit;
import de.manetmodel.network.unit.Speed.SpeedRange;

public class NetworkTopology_1024x786 {

    public static void main(String args[]) {

	MANET<Node, Link<LinkQuality>, LinkQuality, Flow<Node, Link<LinkQuality>, LinkQuality>> manet = new MANET<Node, Link<LinkQuality>, LinkQuality, Flow<Node, Link<LinkQuality>, LinkQuality>>(
		new MANETSupplier().getNodeSupplier(), new MANETSupplier().getLinkSupplier(),
		new MANETSupplier().getLinkPropertySupplier(), new MANETSupplier().getFlowSupplier(),
		new ScalarRadioModel(0.002d, 1e-11, 2000000d, 2412000000d),
		new PedestrianMobilityModel(RandomNumbers.getInstance(10),
			new SpeedRange(4d, 40d, Unit.Time.hour, Unit.Distance.kilometer),
			new Time(Unit.Time.second, 30l), new Speed(4d, Unit.Distance.kilometer, Unit.Time.hour), 10));

	XMLImporter<Node, Position2D, Link<LinkQuality>, LinkQuality> importer = new XMLImporter<Node, Position2D, Link<LinkQuality>, LinkQuality>(
		manet, new VertextPosition2DMapper());

	importer.importGraph("xml/NetworkTopology_1024x786_100N_2021-07-31_09:55:37.120.xml");
	
	manet.initialize();

	DijkstraShortestPath<Node, Link<LinkQuality>, LinkQuality> sp = new DijkstraShortestPath<Node, Link<LinkQuality>, LinkQuality>(manet);
	
	Function<LinkQuality,Double> metric1 = (LinkQuality w) -> {return (double) w.getNumberOfUtilizedLinks();};
	Function<LinkQuality,Double> metric2 = (LinkQuality w) -> {return (double) w.getDistance();};
	
	Flow<Node, Link<LinkQuality>, LinkQuality> flow1 = 
		new Flow<Node, Link<LinkQuality>, LinkQuality>(manet.getVertex(0), manet.getVertex(69), new DataRate(1500000));	
	sp.compute(flow1.getSource(), flow1.getTarget(), metric2);
	manet.addFlow(flow1);
	manet.deployFlow(flow1);
	
	Flow<Node, Link<LinkQuality>, LinkQuality> flow2 = 
		new Flow<Node, Link<LinkQuality>, LinkQuality>(manet.getVertex(4), manet.getVertex(81), new DataRate(1500000));		
	sp.compute(flow2.getSource(), flow2.getTarget(), metric2);	
	manet.addFlow(flow2);
	manet.deployFlow(flow2);
	
	Flow<Node, Link<LinkQuality>, LinkQuality> flow3 = 
		new Flow<Node, Link<LinkQuality>, LinkQuality>(manet.getVertex(9), manet.getVertex(79), new DataRate(1500000));		
	sp.compute(flow3.getSource(), flow3.getTarget(), metric2);	
	manet.addFlow(flow3);
	manet.deployFlow(flow3);
	
	Flow<Node, Link<LinkQuality>, LinkQuality> flow4 = 
		new Flow<Node, Link<LinkQuality>, LinkQuality>(manet.getVertex(46), manet.getVertex(100), new DataRate(1500000));		
	sp.compute(flow4.getSource(), flow4.getTarget(), metric2);		
	manet.addFlow(flow4);
	manet.deployFlow(flow4);
			
	VisualGraphApp<Node, Link<LinkQuality>, LinkQuality> visualGraphApp = new VisualGraphApp<Node, Link<LinkQuality>, LinkQuality>(manet, null, new LinkQualityPrinter());					
    }
}
