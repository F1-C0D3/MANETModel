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
import de.manetmodel.graph.viz.VisualEdgeDistanceTextBuilder;
import de.manetmodel.graph.viz.VisualGraph;
import de.manetmodel.graph.viz.VisualGraphMarkUp;
import de.manetmodel.graph.viz.VisualGraphPanel;
import de.manetmodel.util.RandomNumbers;
import de.manetmodel.util.Tuple;

public class ManetModelApp<V extends Vertex, E extends Edge> {

    JFrame frame;
    // ManetModelFrame frame;
    WeightedUndirectedGraph<V, E> graph;
    VisualGraphPanel<V, E> panel;
    TreeParser treeParser;

    public ManetModelApp(WeightedUndirectedGraph<V, E> graph) {
	this.graph = graph;
	this.initialize();
    }

    static boolean EXIT = false;

    public VisualGraphPanel<V, E> getPanel() {
	return this.panel;
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

	/* exit */
	KeyOption exit = new KeyOption(new Key("exit"), new Info("close app"), new Function(this::exit));
	parser.addOption(exit);
    }

    public static void main(String[] args) {

	WeightedUndirectedGraph<Vertex, Edge> graph = new WeightedUndirectedGraph<Vertex, Edge>(
		new WeightedUndirectedGraphSupplier.VertexSupplier(),
		new WeightedUndirectedGraphSupplier.EdgeSupplier());

	ManetModelApp<Vertex, Edge> app = new ManetModelApp<Vertex, Edge>(graph);

	app.run();
    }

    public void initialize() {

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int windowWidth = (int) screenSize.getWidth() * 3 / 4;
	int windowHeight = (int) screenSize.getHeight() * 3 / 4;

	// XMLImporter xmlImporter = new XMLImporter();
	// this.graph = (WeightedUndirectedGraph<V, E>)
	// xmlImporter.importGraph("graph.xml");

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

	panel = new VisualGraphPanel<V, E>(visualGraph);
	panel.setPreferredSize(new Dimension(windowWidth, windowHeight));
	panel.setFont(new Font("Consolas", Font.PLAIN, 16));
	panel.setLayout(null);

	frame = new JFrame("VisualGraphPanel");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().add(panel);
	frame.pack();
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);

	this.treeParser = new TreeParser();
	buildOptions(treeParser);
    }

    public void run() {
	System.out.println("ManetModel v1.0\nType \"help\" for assistance.\n");
	Scanner scanner = new Scanner(System.in);
	do
	    treeParser.consume(scanner.nextLine(), treeParser.getOptions(), new Input());
	while (!EXIT);
	scanner.close();
    }

    private void createEmpty(Input input) {
	graph.clear();
	panel.updateVisualGraph(
		new VisualGraph<V, E>(graph, new VisualGraphMarkUp<E>(new VisualEdgeDistanceTextBuilder<E>())));
	panel.repaint();
    }

    private void createRandom(Input input) {

	this.graph.clear();

	/*Playground playground = new Playground(1024, 768, 
		new IntRange(input.INT.get(0), input.INT.get(0)), new DoubleRange(50d, 100d), 
		new IntRange(2, 4), new DoubleRange(50d, 100d));*/
	
	Playground playground = new Playground(100000, 100000, 
	new IntRange(input.INT.get(0), input.INT.get(0)), new DoubleRange(50d, 100d), 
	new IntRange(2, 4), new DoubleRange(50d, 100d));
	
	GraphGenerator<V, E> generator = new GraphGenerator<V, E>(this.graph);
	generator.generateRandomGraph(playground);

	panel.updateVisualGraph(
		new VisualGraph<V, E>(graph, new VisualGraphMarkUp<E>(new VisualEdgeDistanceTextBuilder<E>())));
	panel.repaint();
	frame.toFront();
    }

    private void createGrid(Input input) {
	GraphGenerator<V, E> generator = new GraphGenerator<V, E>(graph);
	generator.generateGridGraph(1000, 1000, 50, 200);
	panel.updateVisualGraph(
		new VisualGraph<V, E>(graph, new VisualGraphMarkUp<E>(new VisualEdgeDistanceTextBuilder<E>())));
	panel.repaint();
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

	Path<V, E> shortestPath = dijkstraShortestPath.compute(graph.getVertex(input.INT.get(0)), graph.getVertex(input.INT.get(1)), metric);
	
	panel.getVisualGraph().addPath(shortestPath);
	
	panel.repaint();
	frame.toFront();
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
	panel.updateVisualGraph(
		new VisualGraph<V, E>(graph, new VisualGraphMarkUp<E>(new VisualEdgeDistanceTextBuilder<E>())));
	panel.repaint();
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
	System.out.println(treeParser.getOptions().toString());
    }

    private void helpCommand(Input input) {
	Option option = treeParser.findKeyOption(treeParser.getOptions(), new Key(input.STRING.get(0)));
	if (option != null)
	    System.out.println(option.toString());
    }

    private void exit(Input input) {
	System.out.println("Bye");
	frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	EXIT = true;
    }
}
