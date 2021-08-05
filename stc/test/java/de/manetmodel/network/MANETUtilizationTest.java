package de.manetmodel.network;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.graph.generator.GridGraphGenerator;
import de.jgraphlib.graph.generator.GridGraphProperties;
import de.jgraphlib.graph.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;
import de.jgraphlib.graph.suppliers.EdgeDistanceSupplier;
import de.jgraphlib.gui.EdgeIDPrinter;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.gui.LinkQualityPrinter;
import de.manetmodel.network.mobility.PedestrianMobilityModel;
import de.manetmodel.network.radio.ScalarRadioModel;
import de.manetmodel.network.unit.Speed;
import de.manetmodel.network.unit.Time;
import de.manetmodel.network.unit.Unit;
import de.manetmodel.network.unit.Speed.SpeedRange;

public class MANETUtilizationTest {

    @Test
    public void utilizationTest() throws IOException {

	MANET<Node, Link<LinkQuality>, LinkQuality, Flow<Node, Link<LinkQuality>, LinkQuality>> manet = new MANET<Node, Link<LinkQuality>, LinkQuality, Flow<Node, Link<LinkQuality>, LinkQuality>>(
		new MANETSupplier().getNodeSupplier(), new MANETSupplier().getLinkSupplier(),
		new MANETSupplier().getLinkQualitySupplier(), new MANETSupplier().getFlowSupplier(),
		new ScalarRadioModel(0.002d, 1e-11, 2000000d, 2412000000d),
		new PedestrianMobilityModel(RandomNumbers.getInstance(10),
			new SpeedRange(4d, 40d, Unit.Time.hour, Unit.Distance.kilometer),
			new Time(Unit.Time.second, 30l), new Speed(4d, Unit.Distance.kilometer, Unit.Time.hour), 10));

	GridGraphProperties properties = new GridGraphProperties(/* playground width */ 1024,
		/* playground height */ 768, /* distance between vertices */ 100, /* length of edges */ 100);

	GridGraphGenerator<Node, Link<LinkQuality>, LinkQuality> generator = new GridGraphGenerator<Node, Link<LinkQuality>, LinkQuality>(
		manet, new MANETSupplier().getLinkQualitySupplier(), new RandomNumbers());

	generator.generate(properties);

	manet.initialize();

	VisualGraphApp<Node, Link<LinkQuality>, LinkQuality> visualGraphApp = new VisualGraphApp<Node, Link<LinkQuality>, LinkQuality>(
		manet, new EdgeIDPrinter<Link<LinkQuality>, LinkQuality>());

	System.out.println(manet.getUtilizedLinksOf(manet.getEdge(100)));

	System.in.read();
    }

}
