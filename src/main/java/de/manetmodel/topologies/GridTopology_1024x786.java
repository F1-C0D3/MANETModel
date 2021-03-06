package de.manetmodel.topologies;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Supplier;

import de.jgraphlib.generator.GraphProperties.EdgeStyle;
import de.jgraphlib.generator.GridGraphGenerator;
import de.jgraphlib.generator.GridGraphProperties;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.io.VertextPosition2DMapper;
import de.jgraphlib.graph.io.XMLExporter;
import de.jgraphlib.graph.io.XMLImporter;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.gui.printer.WeightedEdgeIDPrinter;
import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.evaluator.DoubleScope;
import de.manetmodel.evaluator.ScalarLinkQualityEvaluator;
import de.manetmodel.mobilitymodel.PedestrianMobilityModel;
import de.manetmodel.network.scalar.ScalarLinkQuality;
import de.manetmodel.network.scalar.ScalarRadioLink;
import de.manetmodel.network.scalar.ScalarRadioMANET;
import de.manetmodel.network.scalar.ScalarRadioMANETSupplier;
import de.manetmodel.network.scalar.ScalarRadioModel;
import de.manetmodel.network.scalar.ScalarRadioNode;
import de.manetmodel.units.Speed;
import de.manetmodel.units.Speed.SpeedRange;
import de.manetmodel.units.Unit;
import de.manetmodel.units.Watt;

public class GridTopology_1024x786 {

    public static String xmlFilePath;

    GridTopology_1024x786() {}

    public Boolean create() {

	ScalarRadioModel radioModel = new ScalarRadioModel(new Watt(0.002d), new Watt(1e-11), 1000d, 2412000000d,/** maxCommunicationRange **/ 35d,100d);
	PedestrianMobilityModel mobilityModel = new PedestrianMobilityModel(new RandomNumbers(), new SpeedRange(0, 100, Unit.TimeSteps.second, Unit.Distance.meter), new Speed(50, Unit.Distance.meter, Unit.TimeSteps.second));
	ScalarLinkQualityEvaluator evaluator = new ScalarLinkQualityEvaluator(new DoubleScope(0d, 1d), radioModel,
		mobilityModel);
	

	Supplier<ScalarLinkQuality> linkPropertySupplier = new ScalarRadioMANETSupplier().getLinkPropertySupplier();
	ScalarRadioMANET manet = new ScalarRadioMANET(new ScalarRadioMANETSupplier().getNodeSupplier(),
		new ScalarRadioMANETSupplier().getLinkSupplier(),
		linkPropertySupplier,
		new ScalarRadioMANETSupplier().getFlowSupplier(),
		radioModel, 
		mobilityModel,
		evaluator);

	GridGraphProperties properties = new GridGraphProperties(
		/* playground width */ 1024,
		/* playground height */ 768, 
		/* distance between vertices */ 100, 
		/* length of edges */ 100,EdgeStyle.BIDIRECTIONAL);

	GridGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> generator = 
		new GridGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(manet,linkPropertySupplier, new RandomNumbers());

	generator.generate(properties);

	XMLExporter<ScalarRadioNode, Position2D, ScalarRadioLink, ScalarLinkQuality> exporter = 
		new XMLExporter<ScalarRadioNode, Position2D, ScalarRadioLink, ScalarLinkQuality>(
			manet, new VertextPosition2DMapper());

	xmlFilePath = String.format("xml/%s_%s.xml", GridTopology_1024x786.class.getSimpleName(),
		new SimpleDateFormat("yyyy-MM-dd'_'HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())));

	return exporter.exportGraph(xmlFilePath);
    }

    public static void main(String args[]) {

	GridTopology_1024x786 topology = new GridTopology_1024x786();

	topology.create();

	ScalarRadioModel radioModel = new ScalarRadioModel(new Watt(0.002d), new Watt(1e-11), 1000d, 2412000000d,/** maxCommunicationRange **/ 35d,100d);
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

	importer.importGraph(xmlFilePath);

	VisualGraphApp<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> visualGraphApp = 
		new VisualGraphApp<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(manet, new WeightedEdgeIDPrinter<ScalarRadioLink, ScalarLinkQuality>());
    }
}
