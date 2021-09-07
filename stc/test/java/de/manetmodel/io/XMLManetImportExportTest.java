package de.manetmodel.io;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Function;

import javax.swing.SwingUtilities;

import org.junit.Test;

import de.jgraphlib.graph.DirectedWeighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.graph.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;
import de.jgraphlib.graph.io.VertextPosition2DMapper;
import de.jgraphlib.graph.io.XMLExporter;
import de.jgraphlib.graph.io.XMLImporter;
import de.jgraphlib.graph.suppliers.EdgeDistanceSupplier;
import de.jgraphlib.graph.suppliers.Weighted2DGraphSupplier;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.evaluator.DoubleScope;
import de.manetmodel.evaluator.ScalarLinkQualityEvaluator;
import de.manetmodel.gui.LinkQualityScorePrinter;
import de.manetmodel.gui.LinkUtilizationPrinter;
import de.manetmodel.mobilitymodel.PedestrianMobilityModel;
import de.manetmodel.network.MANET;
import de.manetmodel.network.scalar.ScalarLinkQuality;
import de.manetmodel.network.scalar.ScalarRadioFlow;
import de.manetmodel.network.scalar.ScalarRadioLink;
import de.manetmodel.network.scalar.ScalarRadioMANET;
import de.manetmodel.network.scalar.ScalarRadioMANETSupplier;
import de.manetmodel.network.scalar.ScalarRadioModel;
import de.manetmodel.network.scalar.ScalarRadioNode;
import de.manetmodel.units.DataRate;
import de.manetmodel.units.Speed;
import de.manetmodel.units.Unit;
import de.manetmodel.units.Watt;
import de.manetmodel.units.Speed.SpeedRange;

public class XMLManetImportExportTest {
  
    @Test
	public void exportMANET() {

	ScalarRadioModel radioModel = new ScalarRadioModel(new Watt(0.002d), new Watt(1e-11), 1000d, 2412000000d);
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

	NetworkGraphProperties graphProperties = new NetworkGraphProperties(/* playground width */ 1024,
		/* playground height */ 768, /* number of vertices */ new IntRange(100, 100),
		/* distance between vertices */ new DoubleRange(50d, 100d),
		/* edge distance */ new DoubleRange(100d, 100d));

	NetworkGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> generator = 
		new NetworkGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(
			manet, new ScalarRadioMANETSupplier().getLinkPropertySupplier(), new RandomNumbers());
	
	generator.generate(graphProperties);
	manet.initialize();

		XMLExporter<ScalarRadioNode, Position2D, ScalarRadioLink, ScalarLinkQuality> exporter = new XMLExporter<ScalarRadioNode, Position2D, ScalarRadioLink, ScalarLinkQuality>(
			manet, new VertextPosition2DMapper());
	
		exporter.exportGraph(String.format("%s.xml", this.getClass().getName()));
		
		System.out.println(manet.toString());
	}

	@Test
	public void importMANET() throws IOException {

		ScalarRadioModel radioModel = new ScalarRadioModel(new Watt(0.002d), new Watt(1e-11), 1000d, 2412000000d);
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

		XMLImporter<ScalarRadioNode, Position2D, ScalarRadioLink, ScalarLinkQuality> importer = new XMLImporter<ScalarRadioNode, Position2D, ScalarRadioLink, ScalarLinkQuality>(
			manet, new VertextPosition2DMapper());

		importer.importGraph(String.format("%s.xml", this.getClass().getName()));

		VisualGraphApp<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> app = new VisualGraphApp<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(manet);
		
		System.out.println(manet.toString());
		
		System.in.read();
	}
}
