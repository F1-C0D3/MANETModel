package de.manetmodel.generator;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Function;

import javax.swing.SwingUtilities;

import org.junit.Test;

import de.jgraphlib.graph.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.gui.LinkQualityPrinter;

import de.manetmodel.network.example._Flow;
import de.manetmodel.network.example._Link;
import de.manetmodel.network.example._LinkQuality;
import de.manetmodel.network.example._MANET;
import de.manetmodel.network.example._MANETSupplier;
import de.manetmodel.network.example._Node;
import de.manetmodel.network.radio.ScalarRadioModel;
import de.manetmodel.network.unit.DataRate;

public class OverUtilzedProblemGeneratorTest {
  
    @Test
    public void overUtilzedProblemGeneratorTest() throws IOException, InvocationTargetException, InterruptedException {

	_MANET manet = new _MANET(new _MANETSupplier().getNodeSupplier(), new _MANETSupplier().getLinkSupplier(),
		new _MANETSupplier().getLinkQualitySupplier(), new _MANETSupplier().getMyFlowSupplier(),
		new ScalarRadioModel(0.002d, 1e-11, 1000d, 2412000000d));

	NetworkGraphProperties graphProperties = new NetworkGraphProperties(/* playground width */ 1024,
		/* playground height */ 768, /* number of vertices */ new IntRange(100, 100),
		/* distance between vertices */ new DoubleRange(50d, 100d),
		/* edge distance */ new DoubleRange(100d, 100d));

	NetworkGraphGenerator<_Node, _Link, _LinkQuality> generator = new NetworkGraphGenerator<_Node, _Link, _LinkQuality>(
		manet, new _MANETSupplier().getLinkQualitySupplier(), new RandomNumbers());
	
	generator.generate(graphProperties);
	manet.initialize();

	Function<_LinkQuality, Double> metric = (_LinkQuality w) -> {
	    return (double) w.getUtilization().get();
	};

	OverUtilzedProblemGenerator<_Node, _Link, _LinkQuality, _Flow> overUtilizedProblemGenerator 
		= new OverUtilzedProblemGenerator<_Node, _Link, _LinkQuality, _Flow>(
			manet, metric);

	OverUtilizedProblemProperties problemProperties = new OverUtilizedProblemProperties();
	problemProperties.pathCount = 5;
	problemProperties.minLength = 10;
	problemProperties.maxLength = 20;
	problemProperties.minDemand = new DataRate(100);
	problemProperties.maxDemand = new DataRate(200);
	problemProperties.overUtilizationPercentage = 5;

	List<_Flow> flowProblems = overUtilizedProblemGenerator.compute(problemProperties);
	
	SwingUtilities.invokeAndWait(new VisualGraphApp<_Node, _Link, _LinkQuality>(manet, new LinkQualityPrinter<_LinkQuality>()));
	
	System.in.read();
    }
}
