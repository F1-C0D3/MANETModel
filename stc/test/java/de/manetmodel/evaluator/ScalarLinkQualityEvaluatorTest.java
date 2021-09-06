package de.manetmodel.evaluator;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.junit.Test;

import de.jgraphlib.graph.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.evaluator.DoubleScope;
import de.manetmodel.evaluator.ScalarLinkQualityEvaluator;
import de.manetmodel.gui.LinkQualityScorePrinter;
import de.manetmodel.mobilitymodel.PedestrianMobilityModel;
import de.manetmodel.network.scalar.ScalarLinkQuality;
import de.manetmodel.network.scalar.ScalarRadioLink;
import de.manetmodel.network.scalar.ScalarRadioMANET;
import de.manetmodel.network.scalar.ScalarRadioMANETSupplier;
import de.manetmodel.network.scalar.ScalarRadioModel;
import de.manetmodel.network.scalar.ScalarRadioNode;
import de.manetmodel.units.Speed;
import de.manetmodel.units.Unit;
import de.manetmodel.units.Watt;
import de.manetmodel.units.Speed.SpeedRange;

public class ScalarLinkQualityEvaluatorTest {

    @Test
    public void scalarLinkQualityEvaluatorTest() throws InvocationTargetException, InterruptedException, IOException {

	ScalarRadioMANET manet = new ScalarRadioMANET(new ScalarRadioMANETSupplier().getNodeSupplier(),
		new ScalarRadioMANETSupplier().getLinkSupplier(),
		new ScalarRadioMANETSupplier().getLinkPropertySupplier(),
		new ScalarRadioMANETSupplier().getFlowSupplier(),
		new ScalarRadioModel(new Watt(0.002d), new Watt(1e-11), 1000d, 2412000000d), 
		new PedestrianMobilityModel(new RandomNumbers(), new SpeedRange(0, 100, Unit.TimeSteps.second, Unit.Distance.meter), new Speed(50, Unit.Distance.meter, Unit.TimeSteps.second)), 
		new ScalarLinkQualityEvaluator(new DoubleScope(1d,10d), new ScalarRadioModel(new Watt(0.002d), new Watt(1e-11), 1000d, 2412000000d)));

	NetworkGraphProperties graphProperties = new NetworkGraphProperties(
		/* playground width */ 1024,
		/* playground height */ 768, 
		/* number of vertices */ new IntRange(10, 10),
		/* distance between vertices */ new DoubleRange(50d, 100d), 
		/* edge distance */ new DoubleRange(100d, 100d));	

	RandomNumbers random = new RandomNumbers();

	NetworkGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> generator = new NetworkGraphGenerator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(
		manet, new ScalarRadioMANETSupplier().getLinkPropertySupplier(), random);

	generator.generate(graphProperties);

	manet.initialize();

	SwingUtilities.invokeAndWait(new VisualGraphApp<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(manet, new LinkQualityScorePrinter<ScalarLinkQuality>()));
	
	System.in.read();
    }
}
