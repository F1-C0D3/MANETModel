package de.manetmodel.example.topologies;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.jgraphlib.graph.generator.GridGraphProperties;
import de.jgraphlib.graph.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.generator.GridGraphGenerator;
import de.jgraphlib.graph.io.VertextPosition2DMapper;
import de.jgraphlib.graph.io.XMLExporter;
import de.jgraphlib.graph.io.XMLImporter;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.example.radio.ScalarRadioModel;
import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANET;
import de.manetmodel.network.MANETSupplier;
import de.manetmodel.network.Node;
import de.manetmodel.network.mobility.PedestrianMobilityModel;
import de.manetmodel.network.radio.RadioModel;
import de.manetmodel.network.unit.DataRate;
import de.manetmodel.network.unit.DataUnit.Type;
import de.manetmodel.network.unit.Speed;
import de.manetmodel.network.unit.Time;
import de.manetmodel.network.unit.Unit;
import de.manetmodel.network.unit.Speed.SpeedRange;

public class GridTopology_1024x786 {

    public static String xmlFilePath;
    
    GridTopology_1024x786() {}

    public Boolean create() {

	MANET<Node, Link<LinkQuality>, LinkQuality, Flow<Node, Link<LinkQuality>, LinkQuality>> manet = new MANET<Node, Link<LinkQuality>, LinkQuality, Flow<Node, Link<LinkQuality>, LinkQuality>>(
		new MANETSupplier().getNodeSupplier(), new MANETSupplier().getLinkSupplier(),
		new MANETSupplier().getLinkPropertySupplier(), new MANETSupplier().getFlowSupplier(),
		new RadioModel<Node, Link<LinkQuality>, LinkQuality>(new DataRate(10,Type.megabit)),
		new PedestrianMobilityModel(RandomNumbers.getInstance(10),
			new SpeedRange(4d, 40d, Unit.TimeSteps.hour, Unit.Distance.kilometer),
			new Time(Unit.TimeSteps.second, 30l), new Speed(4d, Unit.Distance.kilometer, Unit.TimeSteps.hour), 10));

	GridGraphProperties properties = new GridGraphProperties(/* playground width */ 1024,
		/* playground height */ 768, /* distance between vertices */ 100, /* length of edges */ 100);

	GridGraphGenerator<Node, Link<LinkQuality>, LinkQuality> generator = new GridGraphGenerator<Node, Link<LinkQuality>, LinkQuality>(
		manet, new RandomNumbers());

	generator.generate(properties);

	XMLExporter<Node, Position2D, Link<LinkQuality>, LinkQuality> exporter = new XMLExporter<Node, Position2D, Link<LinkQuality>, LinkQuality>(
		manet, new VertextPosition2DMapper());

	xmlFilePath = String.format(
		"xml/%s_%s.xml", 
		GridTopology_1024x786.class.getSimpleName(), 
		new SimpleDateFormat("yyyy-MM-dd'_'HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())));
	
	return exporter.exportGraph(xmlFilePath);	
    }

    public static void main(String args[]) {

	GridTopology_1024x786 topology = new GridTopology_1024x786();

	topology.create();

	MANET<Node, Link<LinkQuality>, LinkQuality, Flow<Node, Link<LinkQuality>, LinkQuality>> manet = new MANET<Node, Link<LinkQuality>, LinkQuality, Flow<Node, Link<LinkQuality>, LinkQuality>>(
		new MANETSupplier().getNodeSupplier(), new MANETSupplier().getLinkSupplier(),
		new MANETSupplier().getLinkPropertySupplier(), new MANETSupplier().getFlowSupplier(),
		new RadioModel<Node, Link<LinkQuality>, LinkQuality>(new DataRate(10,Type.megabit)),
		new PedestrianMobilityModel(RandomNumbers.getInstance(10),
			new SpeedRange(4d, 40d, Unit.TimeSteps.hour, Unit.Distance.kilometer),
			new Time(Unit.TimeSteps.second, 30l), new Speed(4d, Unit.Distance.kilometer, Unit.TimeSteps.hour), 10));

	XMLImporter<Node, Position2D, Link<LinkQuality>, LinkQuality> importer = new XMLImporter<Node, Position2D, Link<LinkQuality>, LinkQuality>(
		manet, new VertextPosition2DMapper());

	importer.importGraph(xmlFilePath);

	VisualGraphApp<Node, Link<LinkQuality>, LinkQuality> visualGraphApp = new VisualGraphApp<Node, Link<LinkQuality>, LinkQuality>(manet);
    }
}
