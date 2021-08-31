package de.example.topologies;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.example.radio.ScalarRadioModel;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;
import de.jgraphlib.graph.io.VertextPosition2DMapper;
import de.jgraphlib.graph.io.XMLExporter;
import de.jgraphlib.graph.io.XMLImporter;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANET;
import de.manetmodel.network.MANETSupplier;
import de.manetmodel.network.Node;
import de.manetmodel.network.mobility.PedestrianMobilityModel;
import de.manetmodel.network.unit.Speed;
import de.manetmodel.network.unit.Time;
import de.manetmodel.network.unit.Unit;
import de.manetmodel.network.unit.Speed.SpeedRange;

//@formatter:off

public class NetworkTopology_1024x786_100N {
    
    public static String xmlFilePath;
    
    NetworkTopology_1024x786_100N(){}
    
    public Boolean create() {
	
	MANET<Node, Link<LinkQuality>, LinkQuality, Flow<Node, Link<LinkQuality>, LinkQuality>> manet = 
		new MANET<Node, Link<LinkQuality>, LinkQuality, Flow<Node, Link<LinkQuality>, LinkQuality>>(
			new MANETSupplier().getNodeSupplier(), 
			new MANETSupplier().getLinkSupplier(),
			new MANETSupplier().getLinkPropertySupplier(),
			new MANETSupplier().getFlowSupplier(),
			new ScalarRadioModel(0.002d, 1e-11, 2000000d, 2412000000d), 
			new PedestrianMobilityModel(RandomNumbers.getInstance(10),
				new SpeedRange(4d, 40d, Unit.Time.hour, Unit.Distance.kilometer),
				new Time(Unit.Time.second, 30l),
				new Speed(4d, Unit.Distance.kilometer, Unit.Time.hour), 10));
			
	NetworkGraphProperties properties = new NetworkGraphProperties(
		/* playground width */ 		1024,
		/* playground height */ 	768, 
		/* number of vertices */ 	new IntRange(100, 100),
		/* distance between vertices */ new DoubleRange(50d, 100d),
		/* edge distance */ 		new DoubleRange(100d, 100d));

	NetworkGraphGenerator<Node, Link<LinkQuality>, LinkQuality> generator = new NetworkGraphGenerator<Node, Link<LinkQuality>, LinkQuality>(
		manet, 
		new MANETSupplier().getLinkPropertySupplier(), 
		new RandomNumbers());

	generator.generate(properties);

	XMLExporter<Node, Position2D, Link<LinkQuality>, LinkQuality> exporter = new XMLExporter<Node, Position2D, Link<LinkQuality>, LinkQuality>(
		manet, 
		new VertextPosition2DMapper());

	xmlFilePath = String.format(
		"xml/%s_%s.xml", 
		NetworkTopology_1024x786_100N.class.getSimpleName(), 
		new SimpleDateFormat("yyyy-MM-dd'_'HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())));
	
	return exporter.exportGraph(xmlFilePath);
    }
    
    public static void main(String args[]) {

	NetworkTopology_1024x786_100N topology = new NetworkTopology_1024x786_100N();
	
	topology.create();
		
	MANET<Node, Link<LinkQuality>, LinkQuality, Flow<Node, Link<LinkQuality>, LinkQuality>> manet = 
		new MANET<Node, Link<LinkQuality>, LinkQuality, Flow<Node, Link<LinkQuality>, LinkQuality>>(
			new MANETSupplier().getNodeSupplier(), 
			new MANETSupplier().getLinkSupplier(), 
			new MANETSupplier().getLinkPropertySupplier(),
			new MANETSupplier().getFlowSupplier(),
			new ScalarRadioModel(0.002d, 1e-11, 2000000d, 2412000000d), 
			new PedestrianMobilityModel(RandomNumbers.getInstance(10),
				new SpeedRange(4d, 40d, Unit.Time.hour, Unit.Distance.kilometer),
				new Time(Unit.Time.second, 30l),
				new Speed(4d, Unit.Distance.kilometer, Unit.Time.hour), 10));
	
	XMLImporter<Node, Position2D, Link<LinkQuality>, LinkQuality> importer = new XMLImporter<Node, Position2D, Link<LinkQuality>, LinkQuality>(
		manet, 
		new VertextPosition2DMapper());
	
	importer.importGraph(xmlFilePath);
	
	manet.initialize();
	
	VisualGraphApp<Node, Link<LinkQuality>, LinkQuality> visualGraphApp = new VisualGraphApp<Node, Link<LinkQuality>, LinkQuality>(manet);			
    }   
}
