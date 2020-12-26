package de.manetmodel.app;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import de.manetmodel.algo.DijkstraShortestPath;
import de.manetmodel.algo.RandomPath;
import de.manetmodel.app.gui.VisualGraphFrame;
import de.manetmodel.app.gui.visualgraph.VisualGraph;
import de.manetmodel.app.gui.visualgraph.VisualGraphMarkUp;
import de.manetmodel.app.treeparser.Function;
import de.manetmodel.app.treeparser.Info;
import de.manetmodel.app.treeparser.Input;
import de.manetmodel.app.treeparser.Key;
import de.manetmodel.app.treeparser.KeyOption;
import de.manetmodel.app.treeparser.Option;
import de.manetmodel.app.treeparser.Requirement;
import de.manetmodel.app.treeparser.TreeParser;
import de.manetmodel.app.treeparser.Value;
import de.manetmodel.app.treeparser.ValueOption;
import de.manetmodel.app.treeparser.ValueType;
import de.manetmodel.graph.EdgeDistance;
import de.manetmodel.graph.EdgeDistanceSupplier;
import de.manetmodel.graph.EdgeWeightSupplier;
import de.manetmodel.graph.Path;
import de.manetmodel.graph.Position2D;
import de.manetmodel.graph.UndirectedWeighted2DGraph;
import de.manetmodel.graph.Vertex;
import de.manetmodel.graph.WeightedEdge;
import de.manetmodel.graph.WeightedGraphSupplier;
import de.manetmodel.graph.generator.GridGraphProperties;
import de.manetmodel.graph.generator.NetworkGraphGenerator;
import de.manetmodel.graph.generator.NetworkGraphProperties;
import de.manetmodel.graph.generator.RandomGraphGenerator;
import de.manetmodel.graph.generator.GraphProperties.DoubleRange;
import de.manetmodel.graph.generator.GraphProperties.IntRange;
import de.manetmodel.graph.generator.GridGraphGenerator;
import de.manetmodel.util.RandomNumbers;
import de.manetmodel.util.Tuple;

public class VisualGraphApp<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance> {

    private VisualGraphFrame<V, E> frame;
    private UndirectedWeighted2DGraph<V, E, W> graph;
    private EdgeWeightSupplier<W> edgeWeightSupplier;
    private TreeParser treeParser;

    public VisualGraphApp(UndirectedWeighted2DGraph<V, E, W> graph, EdgeWeightSupplier<W> edgeWeightSupplier) {
	this.graph = graph;
	this.edgeWeightSupplier = edgeWeightSupplier;
	this.initialize();
    }

    public void buildOptions(TreeParser parser) {

	/* create */
	KeyOption create = new KeyOption(new Key("create"), new Info("create empty, random or grid graph"));

	// create empty
	KeyOption empty = new KeyOption(new Key("empty"), new Info("create an empty graph"),
		new Function(this::createEmpty), new Requirement(true));
	create.add(empty);

	// create network
	KeyOption network = new KeyOption(new Key("network"), new Info("create a network graph"),
		new Requirement(true));
	network.add(new ValueOption(new Value(ValueType.INT), new Info("number of vertices"),
		new Function(this::createNetworkGraph), new Requirement(true)));
	create.add(network);

	// create random
	KeyOption random = new KeyOption(new Key("random"), new Info("create a random graph"), new Requirement(true));
	random.add(new ValueOption(new Value(ValueType.INT), new Info("number of vertices"),
		new Function(this::createRandomGraph), new Requirement(true)));
	create.add(random);

	// create grid
	KeyOption grid = new KeyOption(new Key("grid"), new Info("create a grid graph"), new Requirement(true));
	grid.add(new ValueOption(new Value(ValueType.INT), new Info("number of vertices"),
		new Function(this::createGridGraph), new Requirement(true)));
	create.add(grid);

	parser.addOption(create);

	// playground
	KeyOption playground = new KeyOption(new Key("playground"), new Info("adjust playground"));
	ValueOption width = new ValueOption(new Value(ValueType.DOUBLE), new Info("width"), new Requirement(true),
		new ValueOption(new Value(ValueType.DOUBLE), new Info("height"), new Requirement(true), new ValueOption(
			new Value(ValueType.INT), new Info("number of vertices"), new Requirement(true),
			new ValueOption(new Value(ValueType.INT), new Info("number of edges"), new Requirement(true),
				new ValueOption(new Value(ValueType.DOUBLE), new Info("length of edges"),
					new Function(this::setPlayground), new Requirement(true))))));
	playground.add(width);

	parser.addOption(playground);

	/* add */
	KeyOption add = new KeyOption(new Key("add"), new Info("add vertex or edge to graph"));

	// add vertex
	KeyOption vertex = new KeyOption(new Key("vertex"), new Info("add a new vertex to graph"),
		new Requirement(true));
	vertex.add(new ValueOption(new Value(ValueType.DOUBLE), new Info("x"), new Requirement(true), new ValueOption(
		new Value(ValueType.DOUBLE), new Info("y"), new Function(this::addVertex), new Requirement(true))));
	add.add(vertex);

	// add edge
	KeyOption edge = new KeyOption(new Key("edge"), new Info("add a new edge to graph"), new Requirement(true));
	edge.add(new ValueOption(new Value(ValueType.INT), new Info("source ID"), new Requirement(true),
		new ValueOption(new Value(ValueType.INT), new Info("target ID"), new Function(this::addEdge),
			new Requirement(true))));
	add.add(edge);

	// add path
	KeyOption path = new KeyOption(new Key("path"), new Info("add a path to graph"));
	path.add(new ValueOption(new Value(ValueType.INT), new Info("ID of source vertex"), new Requirement(true),
		new ValueOption(new Value(ValueType.INT), new Info("ID of target vertex"),
			new Function(this::addShortestPath), new Requirement(true))));

	add.add(path);

	parser.addOption(add);

	/* remove */
	KeyOption remove = new KeyOption(new Key("remove"), new Info("remove vertex or edge from graph"));

	// remove vertex
	KeyOption removeVertex = new KeyOption(new Key("vertex"), new Info("remove vertex from graph"));
	removeVertex.add(
		new ValueOption(new Value(ValueType.INT), new Info("vertex ID"), new Function(this::removeVertex)));
	remove.add(removeVertex);

	// remove edge
	KeyOption removeEdge = new KeyOption(new Key("edge"), new Info("remove edege from graph"));
	removeEdge.add(new ValueOption(new Value(ValueType.INT), new Info("edgeID"), new Function(this::removeEdge)));
	remove.add(removeEdge);

	parser.addOption(remove);

	/* import */
	KeyOption importGraph = new KeyOption(new Key("import"), new Info("import graph"));
	importGraph.add(new ValueOption(new Value(ValueType.STRING), new Info("path and filename"),
		new Function(this::importGraph), new Requirement(true)));
	parser.addOption(importGraph);

	/* export */
	KeyOption exportGraph = new KeyOption(new Key("export"), new Info("export graph"));
	exportGraph.add(new ValueOption(new Value(ValueType.STRING), new Info("path and filename"),
		new Function(this::exportGraph), new Requirement(true)));
	parser.addOption(exportGraph);

	/* help */
	KeyOption help = new KeyOption(new Key("help"), new Info("the help"), new Function(this::help));

	// help create
	help.add(new ValueOption(new Value(ValueType.STRING), new Info("name of command"),
		new Function(this::helpCommand)));
	parser.addOption(help);

	/* image */
	KeyOption toImage = new KeyOption(new Key("image"), new Info("shot a picture of graph"));
	toImage.add(new ValueOption(new Value(ValueType.STRING), new Info("path and filename"),
		new Function(this::toImage), new Requirement(true)));
	parser.addOption(toImage);

	/* clear */
	KeyOption clear = new KeyOption(new Key("clear"), new Info("clear terminal"), new Function(this::clear));
	parser.addOption(clear);

	/* scale */
	KeyOption scale = new KeyOption(new Key("scale"), new Info("scale ui"));
	scale.add(new ValueOption(new Value(ValueType.DOUBLE), new Info("scale factor"), new Function(this::scale),
		new Requirement(true)));
	parser.addOption(scale);

	/* exit */
	KeyOption exit = new KeyOption(new Key("exit"), new Info("close app"), new Function(this::exit));
	parser.addOption(exit);

	/* terminal */

	KeyOption terminal = new KeyOption(new Key("terminal"), new Info("change terminal properties"),
		new Requirement(true));

	KeyOption font = new KeyOption(new Key("font"), new Info("font"), new Requirement(true));
	KeyOption size = new KeyOption(new Key("size"), new Info("size"), new Requirement(true));
	size.add(new ValueOption(new Value(ValueType.INT), new Info("value"), new Function(this::setTerminalFontSize)));
	font.add(size);

	KeyOption color = new KeyOption(new Key("color"), new Info("color"), new Requirement(true));
	color.add(new ValueOption(new Value(ValueType.STRING), new Info("value"),
		new Function(this::setTerminalFontColor)));
	font.add(color);

	terminal.add(font);

	terminal.add(color);
	parser.addOption(terminal);
    }

    public static void main(String[] args) {

	// Create Graph
	UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> graph = new UndirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
		new WeightedGraphSupplier<Position2D, EdgeDistance>().getVertexSupplier(),
		new WeightedGraphSupplier<Position2D, EdgeDistance>().getEdgeSupplier());

	// Create App
	VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> app = new VisualGraphApp<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
		graph, new EdgeDistanceSupplier());
    }

    public void acceptTerminalCommand(String command) {
	treeParser.match(command);
    }

    public void acceptTreeParserOutput(String message) {
	frame.getTerminal().appendText(message);
    }

    public void initialize() {

	treeParser = new TreeParser();
	treeParser.addOutputListener(this::acceptTreeParserOutput);
	buildOptions(treeParser);

	NetworkGraphProperties properties = new NetworkGraphProperties(1024, 768, new IntRange(100, 200),
		new DoubleRange(50d, 100d), 100);

	NetworkGraphGenerator<V, E, W> generator = new NetworkGraphGenerator<V, E, W>(graph, edgeWeightSupplier);
	generator.generate(properties);

	VisualGraph<V, E> visualGraph = new VisualGraph<V, E>(graph, new VisualGraphMarkUp());

	RandomPath<V, E> randomPath = new RandomPath<V, E>(graph);

	for (int i = 1; i <= 20; i++)
	    visualGraph.addVisualPath(
		    randomPath.compute(graph.getVertex(RandomNumbers.getRandom(0, graph.getVertices().size())), 5));

	frame = new VisualGraphFrame<V, E>(visualGraph);
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	frame.setPreferredSize(
		new Dimension((int) screenSize.getWidth() * 3 / 4, (int) screenSize.getHeight() * 3 / 4));
	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	frame.pack();
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);
	frame.getTerminal().addInputListener(this::acceptTerminalCommand);
	frame.getTerminal().setText("ManetModel v1.0\n\n");
    }

    private void scale(Input input) {
	frame.scale(input.DOUBLE.get(0));
    }

    private void setTerminalFontSize(Input input) {
	frame.getTerminal().getTextArea().setFont(new Font("Monospaced", Font.PLAIN, input.INT.get(0)));
    }

    private void setTerminalFontColor(Input input) {
	frame.getTerminal().getTextArea().setForeground(null);
    }

    private void createEmpty(Input input) {
	graph.clear();
	frame.getVisualGraphPanel().updateVisualGraph(new VisualGraph<V, E>(graph, new VisualGraphMarkUp()));
	frame.getVisualGraphPanel().repaint();
    }

    private void createNetworkGraph(Input input) {
	graph.clear();

	NetworkGraphProperties properties = new NetworkGraphProperties(1024, 768,
		new IntRange(input.INT.get(0), input.INT.get(0)), new DoubleRange(50d, 100d), 100);

	NetworkGraphGenerator<V, E, W> generator = new NetworkGraphGenerator<V, E, W>(graph, edgeWeightSupplier);

	generator.generate(properties);

	frame.getVisualGraphPanel().updateVisualGraph(new VisualGraph<V, E>(graph, new VisualGraphMarkUp()));
	frame.getVisualGraphPanel().repaint();
    }

    private void createRandomGraph(Input input) {
	graph.clear();

	NetworkGraphProperties properties = new NetworkGraphProperties(1024, 768,
		new IntRange(input.INT.get(0), input.INT.get(0)), new DoubleRange(50d, 100d), 75);

	RandomGraphGenerator<V, E, W> generator = new RandomGraphGenerator<V, E, W>(graph, edgeWeightSupplier);

	generator.generate(properties);

	frame.getVisualGraphPanel().updateVisualGraph(new VisualGraph<V, E>(graph, new VisualGraphMarkUp()));
	frame.getVisualGraphPanel().repaint();
    }

    private void createGridGraph(Input input) {
	graph.clear();

	GridGraphProperties properties = new GridGraphProperties(1000, 1000, 100, 200);

	GridGraphGenerator<V, E, W> generator = new GridGraphGenerator<V, E, W>(graph, edgeWeightSupplier);

	generator.generate(properties);

	frame.getVisualGraphPanel().updateVisualGraph(new VisualGraph<V, E>(graph, new VisualGraphMarkUp()));
	frame.getVisualGraphPanel().repaint();
    }

    private void addVertex(Input input) {
	graph.addVertex(input.DOUBLE.get(0), input.DOUBLE.get(1));
	frame.getVisualGraphPanel().updateVisualGraph(new VisualGraph<V, E>(graph, new VisualGraphMarkUp()));
	frame.getVisualGraphPanel().repaint();
    }

    private void addEdge(Input input) {
	graph.addEdge(graph.getVertex(input.INT.get(0)), graph.getVertex(input.INT.get(1)));
	frame.getVisualGraphPanel().updateVisualGraph(new VisualGraph<V, E>(graph, new VisualGraphMarkUp()));
	frame.getVisualGraphPanel().repaint();
    }

    private void addShortestPath(Input input) {

	DijkstraShortestPath<V, E> dijkstraShortestPath = new DijkstraShortestPath<V, E>(graph);

	java.util.function.Function<Tuple<E, V>, Double> metric = (Tuple<E, V> t) -> {
	    return 1d;
	};

	Path<V, E> shortestPath = dijkstraShortestPath.compute(graph.getVertex(input.INT.get(0)),
		graph.getVertex(input.INT.get(1)), metric);

	frame.getVisualGraphPanel().getVisualGraph().addVisualPath(shortestPath);
	frame.getVisualGraphPanel().repaint();
    }

    private void removeVertex(Input input) {

    }

    private void removeEdge(Input input) {

    }

    private void setPlayground(Input input) {

    }

    private void importGraph(Input input) {
	// XMLImporter<V, E> xmlImporter = new XMLImporter<V, E>(this.graph);
	// xmlImporter.importGraph(input.STRING.get(0));
	// panel.updateVisualGraph(
	// new VisualGraph<V, E>(graph, new VisualGraphMarkUp<E>(new
	// VisualEdgeDistanceTextBuilder<E>())));
	// panel.repaint();
    }

    private void exportGraph(Input input) {
	// XMLExporter<V, E> xmlExporter = new XMLExporter<V, E>(this.graph);
	// xmlExporter.exportGraph(input.STRING.get(0));
    }

    private void toImage(Input input) {
	frame.getTerminal().setVisible(false);
	Container container = frame.getContentPane();
	BufferedImage image = new BufferedImage(container.getWidth(), container.getHeight(),
		BufferedImage.TYPE_INT_ARGB);
	container.paint(image.getGraphics());
	try {
	    ImageIO.write(image, "PNG", new File(String.format("%s.png", input.STRING.get(0))));
	} catch (IOException e) {
	    e.printStackTrace();
	}
	frame.getTerminal().setVisible(true);
    }

    private void help(Input input) {
	this.frame.getTerminal().appendText(treeParser.getOptions().toString());
    }

    private void helpCommand(Input input) {
	Option option = treeParser.findKeyOption(treeParser.getOptions(), new Key(input.STRING.get(0)));
	if (option != null)
	    System.out.println(option.toString());
    }

    private void clear(Input input) {
	frame.getTerminal().clear();
    }

    private void exit(Input input) {
	frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}
