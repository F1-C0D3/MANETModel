package de.manetmodel.app;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import de.manetmodel.algo.DijkstraShortestPath;
import de.manetmodel.algo.RandomPath;
import de.manetmodel.app.gui.VisualGraphFrame;
import de.manetmodel.app.gui.VisualGraphPanel;
import de.manetmodel.app.gui.visualgraph.VisualEdgeDistanceTextBuilder;
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
import de.manetmodel.graph.Edge;
import de.manetmodel.graph.Path;
import de.manetmodel.graph.Playground;
import de.manetmodel.graph.Vertex;
import de.manetmodel.graph.WeightedUndirectedGraph;
import de.manetmodel.graph.WeightedUndirectedGraphSupplier;
import de.manetmodel.graph.generator.GraphGenerator;
import de.manetmodel.graph.Playground.DoubleRange;
import de.manetmodel.graph.Playground.IntRange;
import de.manetmodel.graph.io.XMLExporter;
import de.manetmodel.graph.io.XMLImporter;
import de.manetmodel.util.RandomNumbers;
import de.manetmodel.util.Tuple;

public class VisualGraphApp<V extends Vertex, E extends Edge> {

    private VisualGraphFrame<V, E> frame;
    private WeightedUndirectedGraph<V, E> graph;
    private TreeParser treeParser;

    public VisualGraphApp(WeightedUndirectedGraph<V, E> graph) {
	this.graph = graph;
	this.initialize();
    }

    public void buildOptions(TreeParser parser) {

	/* create */
	KeyOption create = new KeyOption(new Key("create"), new Info("create empty, random or grid graph"));

	// create empty
	KeyOption empty = new KeyOption(new Key("empty"), new Info("create an empty graph"),
		new Function(this::createEmpty), new Requirement(true));
	create.add(empty);

	// create random 100
	KeyOption random = new KeyOption(new Key("random"), new Info("create a random graph"), new Requirement(true));
	random.add(new ValueOption(new Value(ValueType.INT), new Info("number of vertices"),
		new Function(this::createRandom), new Requirement(true)));
	create.add(random);

	// create grid 100
	KeyOption grid = new KeyOption(new Key("grid"), new Info("create a grid graph"), new Requirement(true));
	grid.add(new ValueOption(new Value(ValueType.INT), new Info("number of vertices"),
		new Function(this::createGrid), new Requirement(true)));
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
	
	/* exit */
	KeyOption exit = new KeyOption(new Key("exit"), new Info("close app"), new Function(this::exit));
	parser.addOption(exit);
    }

    public static void main(String[] args) {

	WeightedUndirectedGraph<Vertex, Edge> graph = new WeightedUndirectedGraph<Vertex, Edge>(
		new WeightedUndirectedGraphSupplier.VertexSupplier(),
		new WeightedUndirectedGraphSupplier.EdgeSupplier());

	VisualGraphApp<Vertex, Edge> app = new VisualGraphApp<Vertex, Edge>(graph);
    }

    public void initialize() {

	this.treeParser = new TreeParser();
	buildOptions(treeParser);

	GraphGenerator<V, E> generator = new GraphGenerator<V, E>(graph);
	Playground playground = new Playground(1024, 768, new IntRange(200, 300), new DoubleRange(50d, 100d),
		new IntRange(2, 4), new DoubleRange(50d, 100d));
	generator.generateRandomGraph(playground);

	VisualGraph<V, E> visualGraph = new VisualGraph<V, E>(graph,
		new VisualGraphMarkUp<E>(new VisualEdgeDistanceTextBuilder<E>()));

	RandomPath<V, E> randomPath = new RandomPath<V, E>(graph);

	for (int i = 1; i <= 20; i++)
	    visualGraph.addPath(
		    randomPath.compute(graph.getVertex(RandomNumbers.getRandom(0, graph.getVertices().size())), 5));

	frame = new VisualGraphFrame<V, E>(visualGraph);
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	frame.setPreferredSize(
		new Dimension((int) screenSize.getWidth() * 3 / 4, (int) screenSize.getHeight() * 3 / 4));
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.pack();
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);

	frame.addTerminalInputListener(this::acceptCommand);
	
	frame.getTerminalPanel().addMessage("VisualGraphApp v1.0\n----------------------------------------------\n");
	frame.getTerminalPanel().addMessage("Press \"F1\" to show/hide terminal, type \"help\" for assistance \n\n");
    }

    public void acceptCommand(String command) {
	treeParser.consume(command, treeParser.getOptions(), new Input());
    }

    private void createEmpty(Input input) {
	graph.clear();
	frame.getVisualGraphPanel().updateVisualGraph(
		new VisualGraph<V, E>(graph, new VisualGraphMarkUp<E>(new VisualEdgeDistanceTextBuilder<E>())));
	frame.getVisualGraphPanel().repaint();
    }

    private void createRandom(Input input) {

	graph.clear();

	Playground playground = new Playground(1024, 768, new IntRange(input.INT.get(0), input.INT.get(0)),
		new DoubleRange(50d, 100d), new IntRange(2, 4), new DoubleRange(50d, 100d));

	GraphGenerator<V, E> generator = new GraphGenerator<V, E>(this.graph);
	generator.generateRandomGraph(playground);

	frame.getVisualGraphPanel().updateVisualGraph(
		new VisualGraph<V, E>(graph, new VisualGraphMarkUp<E>(new VisualEdgeDistanceTextBuilder<E>())));
	frame.getVisualGraphPanel().repaint();
    }

    private void createGrid(Input input) {
	GraphGenerator<V, E> generator = new GraphGenerator<V, E>(graph);
	generator.generateGridGraph(1000, 1000, 50, 200);
	/*
	 * panel.updateVisualGraph( new VisualGraph<V, E>(graph, new
	 * VisualGraphMarkUp<E>(new VisualEdgeDistanceTextBuilder<E>())));
	 * panel.repaint();
	 */
	frame.toFront();
    }

    private void addVertex(Input input) {

    }

    private void addEdge(Input input) {

    }

    private void addShortestPath(Input input) {

	DijkstraShortestPath<V, E> dijkstraShortestPath = new DijkstraShortestPath<V, E>(graph);

	java.util.function.Function<Tuple<E, V>, Double> metric = (Tuple<E, V> t) -> {
	    return 1d;
	};

	Path<V, E> shortestPath = dijkstraShortestPath.compute(graph.getVertex(input.INT.get(0)),
		graph.getVertex(input.INT.get(1)), metric);

	frame.getVisualGraphPanel().getVisualGraph().addPath(shortestPath);
	frame.getVisualGraphPanel().repaint();
    }

    private void removeVertex(Input input) {

    }

    private void removeEdge(Input input) {

    }

    private void setPlayground(Input input) {

    }

    private void importGraph(Input input) {
	XMLImporter<V, E> xmlImporter = new XMLImporter<V, E>(this.graph);
	xmlImporter.importGraph(input.STRING.get(0));
	// panel.updateVisualGraph(
	// new VisualGraph<V, E>(graph, new VisualGraphMarkUp<E>(new
	// VisualEdgeDistanceTextBuilder<E>())));
	// panel.repaint();
	frame.toFront();
    }

    private void exportGraph(Input input) {
	XMLExporter<V, E> xmlExporter = new XMLExporter<V, E>(this.graph);
	xmlExporter.exportGraph(input.STRING.get(0));
    }

    private void toImage(Input input) {
	Container container = frame.getContentPane();
	BufferedImage image = new BufferedImage(container.getWidth(), container.getHeight(),
		BufferedImage.TYPE_INT_ARGB);
	container.paint(image.getGraphics());
	try {
	    ImageIO.write(image, "PNG", new File(String.format("%s.png", input.STRING)));
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void help(Input input) {
	this.frame.getTerminalPanel().addMessage(treeParser.getOptions().toString());
    }

    private void helpCommand(Input input) {
	Option option = treeParser.findKeyOption(treeParser.getOptions(), new Key(input.STRING.get(0)));
	if (option != null)
	    System.out.println(option.toString());
    }

    private void clear(Input input) {
	frame.getTerminalPanel().clear();
    }
    
    private void exit(Input input) {
	frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}
