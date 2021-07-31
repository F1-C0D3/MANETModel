package de.manetmodel.network;

import java.io.IOException;

import org.junit.Test;

import de.jgraphlib.graph.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;
import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.network.mobility.PedestrianMobilityModel;
import de.manetmodel.network.radio.ScalarRadioModel;
import de.manetmodel.network.unit.Speed;
import de.manetmodel.network.unit.Time;
import de.manetmodel.network.unit.Unit;
import de.manetmodel.network.unit.Speed.SpeedRange;

public class MANETCopyTest {

    @Test
    public void copyTest() {

	MANET<Node, Link<LinkQuality>, LinkQuality, Flow<Node, Link<LinkQuality>, LinkQuality>> manet = new MANET<Node, Link<LinkQuality>, LinkQuality, Flow<Node, Link<LinkQuality>, LinkQuality>>(
		new MANETSupplier().getNodeSupplier(), new MANETSupplier().getLinkSupplier(),
		new MANETSupplier().getLinkQualitySupplier(), new MANETSupplier().getFlowSupplier(),
		new ScalarRadioModel(0.002d, 1e-11, 2000000d, 2412000000d),
		new PedestrianMobilityModel(RandomNumbers.getInstance(10),
			new SpeedRange(4d, 40d, Unit.Time.hour, Unit.Distance.kilometer),
			new Time(Unit.Time.second, 30l), new Speed(4d, Unit.Distance.kilometer, Unit.Time.hour), 10));

	NetworkGraphProperties properties = new NetworkGraphProperties(/* playground width */ 1024,
		/* playground height */ 768, /* number of vertices */ new IntRange(100, 200),
		/* distance between vertices */ new DoubleRange(50d, 100d),
		/* edge distance */ new DoubleRange(100d, 100d));

	NetworkGraphGenerator<Node, Link<LinkQuality>, LinkQuality> generator = new NetworkGraphGenerator<Node, Link<LinkQuality>, LinkQuality>(
		manet, new MANETSupplier().getLinkQualitySupplier(), new RandomNumbers());
	
	MANET<Node, Link<LinkQuality>, LinkQuality, Flow<Node, Link<LinkQuality>, LinkQuality>> copy = new MANET<Node, Link<LinkQuality>, LinkQuality, Flow<Node, Link<LinkQuality>, LinkQuality>>(manet);
	
    }

}
