package de.manetmodel.generator;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.junit.Test;

import de.jgraphlib.graph.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.graph.algorithms.DijkstraShortestPath;
import de.jgraphlib.graph.algorithms.RandomPath;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.gui.printer.EdgeIDPrinter;
import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.gui.LinkQualityPrinter;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANETSupplier;
import de.manetmodel.network.Node;
import de.manetmodel.network.example.myFlow;
import de.manetmodel.network.example.myMANET;
import de.manetmodel.network.example.myMANETSupplier;
import de.manetmodel.network.mobility.PedestrianMobilityModel;
import de.manetmodel.network.radio.ScalarRadioModel;
import de.manetmodel.network.unit.DataRate;
import de.manetmodel.network.unit.Speed;
import de.manetmodel.network.unit.Time;
import de.manetmodel.network.unit.Unit;
import de.manetmodel.network.unit.Speed.SpeedRange;

public class OverUtilzedProblemGeneratorTest {

    @Test
    public void overUtilzedProblemGeneratorTest() {

	myMANET manet = new myMANET(new myMANETSupplier().getNodeSupplier(), new myMANETSupplier().getLinkSupplier(),
		new myMANETSupplier().getLinkQualitySupplier(), new myMANETSupplier().getMyFlowSupplier(),
		new ScalarRadioModel(0.002d, 1e-11, 1000d, 2412000000d),
		new PedestrianMobilityModel(RandomNumbers.getInstance(10),
			new SpeedRange(4d, 40d, Unit.Time.hour, Unit.Distance.kilometer),
			new Time(Unit.Time.second, 30l), new Speed(4d, Unit.Distance.kilometer, Unit.Time.hour), 10));

	NetworkGraphProperties graphProperties = new NetworkGraphProperties(/* playground width */ 300,
		/* playground height */ 300, /* number of vertices */ new IntRange(100, 100),
		/* distance between vertices */ new DoubleRange(50d, 100d),
		/* edge distance */ new DoubleRange(100d, 100d));

	NetworkGraphGenerator<Node, Link<LinkQuality>, LinkQuality> generator = new NetworkGraphGenerator<Node, Link<LinkQuality>, LinkQuality>(
		manet, new MANETSupplier().getLinkPropertySupplier(), new RandomNumbers());

	generator.generate(graphProperties);

	manet.initialize();

	Function<LinkQuality, Double> metric = (LinkQuality w) -> {
	    return (double) w.getUtilization().get();
	};
	
	OverUtilzedProblemGenerator<Node, Link<LinkQuality>, LinkQuality, myFlow> overUtilizedProblemGenerator = 
		new OverUtilzedProblemGenerator<Node, Link<LinkQuality>, LinkQuality, myFlow>(manet, metric);

	OverUtilizedProblemProperties flowProblemProperties = new OverUtilizedProblemProperties();
	flowProblemProperties.pathCount = 1;
	flowProblemProperties.minLength = 10;
	flowProblemProperties.maxLength = 20;
	flowProblemProperties.minDemand = new DataRate(1000);
	flowProblemProperties.maxDemand = new DataRate(2000);
	flowProblemProperties.overUtilizationPercentage = 0;

	List<myFlow> flowProblems = overUtilizedProblemGenerator.compute(flowProblemProperties, Duration.ofSeconds(1));
	
	if (!Objects.isNull(flowProblems)) {
	    VisualGraphApp<Node, Link<LinkQuality>, LinkQuality> visualGraphApp = new VisualGraphApp<Node, Link<LinkQuality>, LinkQuality>(
		    manet, flowProblems, new LinkQualityPrinter());
	    try {
		System.in.read();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

    }
}
