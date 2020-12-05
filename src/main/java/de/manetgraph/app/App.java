package de.manetgraph.app;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import de.manetgraph.app.treeparser.Function;
import de.manetgraph.app.treeparser.Info;
import de.manetgraph.app.treeparser.Input;
import de.manetgraph.app.treeparser.Key;
import de.manetgraph.app.treeparser.Value;
import de.manetgraph.app.treeparser.KeyOption;
import de.manetgraph.app.treeparser.TreeParser;
import de.manetgraph.app.treeparser.ValueOption;
import de.manetgraph.app.treeparser.ValueType;
import de.manetmodel.graph.ManetEdge;
import de.manetmodel.graph.ManetGraph;
import de.manetmodel.graph.ManetGraphSupplier;
import de.manetmodel.graph.ManetVertex;
import de.manetmodel.graph.Playground;
import de.manetmodel.graph.Playground.DoubleRange;
import de.manetmodel.graph.Playground.IntRange;
import de.manetmodel.visualization.VisualGraphPanel;

public class App {
	
	static ManetGraph<ManetVertex, ManetEdge> graph;	
	static JFrame frame;
	static VisualGraphPanel<ManetVertex, ManetEdge> panel;	
	static boolean EXIT = false;
	
	public static void buildOptions(TreeParser parser) {
				
		/* create */	
		KeyOption create = new KeyOption(new Key("create"), new Info("create an empty graph"), new Function(App::createEmpty));	
		
		// create empty 	
		KeyOption empty = new KeyOption(new Key("empty"), new Info("create an empty graph"), new Function(App::createEmpty));
		create.add(empty);	
		
		// create random 100	
		KeyOption random = new KeyOption(new Key("random"), new Info("create a random graph"), new Function(App::createRandom));
		random.add(new ValueOption(new Value(ValueType.INT), new Info("number of nodes"), new Function(App::createRandom))); 	
		
		create.add(random);	
		// create grid 100
		KeyOption grid = new KeyOption(new Key("grid"), new Info("create a grid graph"), new Function(App::createGrid));
		grid.add(new ValueOption(new Value(ValueType.INT), new Info("number of nodes"), new Function(App::createGrid))); 
		create.add(grid);	
		
		parser.addOption(create);
		
		/* add */
		KeyOption add = new KeyOption(new Key("add"), new Info("add new vertex or edge to graph"));		
		
		// add vertex
		KeyOption vertex = new KeyOption(new Key("vertex"), new Info("add a new vertex to graph"), new Function(App::addVertex));
		vertex.add(new ValueOption(new Value(ValueType.DOUBLE_TUPLE), new Info("coordinate"), new Function(App::addVertex))); 
		add.add(vertex);		
		// add edge
		KeyOption edge = new KeyOption(new Key("edge"), new Info("add a new edge to graph"), new Function(App::addEdge));
		edge.add(new ValueOption(new Value(ValueType.INT_TUPLE), new Info("IDs of source and target vertices"), new Function(App::addEdge))); 
		add.add(edge);
		
		parser.addOption(add);
		
		/* remove */
		KeyOption remove = new KeyOption(new Key("add"), new Info("add new vertex or edge to graph"));		
		
		// remove vertex
		KeyOption removeVertex = new KeyOption(new Key("vertex"), new Info("remove vertex from graph"));
		removeVertex.add(new ValueOption(new Value(ValueType.INT), new Info("vertex ID"), new Function(App::removeVertex))); 
		remove.add(removeVertex);
		
		// remove edge
		KeyOption removeEdge = new KeyOption(new Key("edge"), new Info("remove edege from graph"));
		removeEdge.add(new ValueOption(new Value(ValueType.INT), new Info("edgeID"), new Function(App::removeEdge))); 
		remove.add(removeEdge);	
		
		parser.addOption(remove);
		
		/* print */			
		KeyOption print = new KeyOption(new Key("print"), new Info("print graph"), new Function(App::print));	
		parser.addOption(print);
				
		/* import */	
		KeyOption importGraph = new KeyOption(new Key("import"), new Info("import graph"));	
		importGraph.add(new ValueOption(new Value(ValueType.STRING), new Info("path and filename"), new Function(App::importGraph))); 
		parser.addOption(importGraph);
				
		/* export */	
		KeyOption exportGraph = new KeyOption(new Key("export"), new Info("export graph"));	
		exportGraph.add(new ValueOption(new Value(ValueType.STRING), new Info("path and filename"), new Function(App::exportGraph))); 
		parser.addOption(exportGraph);
				
		/* help */	
		KeyOption help = new KeyOption(new Key("help"), new Info("the help"), new Function(App::importGraph));
		
		// help create
		help.add(new ValueOption(new Value(ValueType.STRING), new Info("name of command"), new Function(App::importGraph))); 
		parser.addOption(help);

		/* plot */		
		KeyOption plot = new KeyOption(new Key("plot"), new Info("plot graph"), new Function(App::plot));	
		parser.addOption(plot);
		
		/* image */	
		KeyOption toImage = new KeyOption(new Key("image"), new Info("shot a picture of graph"));	
		toImage.add(new ValueOption(new Value(ValueType.STRING), new Info("path and filename"), new Function(App::toImage))); 
		parser.addOption(toImage);
		
		/* clear */	
		KeyOption clear = new KeyOption(new Key("clear"), new Info("clear console"), new Function(App::clear));	
		parser.addOption(clear);
		
		/* exit */	
		KeyOption exit = new KeyOption(new Key("exit"), new Info("close app"), new Function(App::exit));	
		parser.addOption(exit);	
	}
		
	public static void main(String[] args) {	
		
		graph = new ManetGraph<ManetVertex, ManetEdge>(new ManetGraphSupplier.ManetVertexSupplier(), new ManetGraphSupplier.ManetEdgeSupplier());
		graph.generateSimpleGraph();
		
		panel = new VisualGraphPanel<ManetVertex, ManetEdge>(graph.toVisualGraph());
   		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
   		int width = (int) screenSize.getWidth() * 3/4;
   		int height = (int) screenSize.getHeight() * 3/4;
   		panel.setPreferredSize(new Dimension(width, height));
   		panel.setFont(new Font("Consolas", Font.PLAIN, 16));  
   		panel.setLayout(null);
   		
   		frame = new JFrame("VisualGraphPanel");
   		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   		frame.getContentPane().add(panel);
   		frame.pack();
   		frame.setLocationRelativeTo(null);
   		frame.setVisible(true); 
		
		TreeParser parser = new TreeParser();
		buildOptions(parser);	
		
		System.out.println(parser.getOptions().toString());
		
		Scanner scanner = new Scanner(System.in); 				
		do parser.consume(scanner.nextLine(), parser.getOptions(), new Input()); while(!EXIT);		
    	scanner.close();
	}
	
	private static void createEmpty(Input input) {				
		graph = new ManetGraph<ManetVertex, ManetEdge>(new ManetGraphSupplier.ManetVertexSupplier(), new ManetGraphSupplier.ManetEdgeSupplier());		
		panel.updateVisualGraph(graph.toVisualGraph());
		panel.repaint();
	}
	
	private static void createRandom(Input input) {		
		Playground pg = new Playground();
		pg.height = new IntRange(0, 10000);
		pg.width = new IntRange(0, 10000);
		pg.edgeCount = new IntRange(4, 4);
		pg.vertexCount = new IntRange(input.INT, input.INT);
		pg.edgeDistance = new DoubleRange(50d, 100d);			
		graph = new ManetGraph<ManetVertex, ManetEdge>(new ManetGraphSupplier.ManetVertexSupplier(), new ManetGraphSupplier.ManetEdgeSupplier());
		graph.generateRandomGraph(pg);			
		panel.updateVisualGraph(graph.toVisualGraph());
		panel.repaint();
	}
	
	private static void createGrid(Input input) {		
		Playground pg = new Playground();
		pg.height = new IntRange(0, 1000);
		pg.width = new IntRange(0, 1000);
		pg.edgeCount = new IntRange(4, 4);
		pg.vertexCount = new IntRange(input.INT, input.INT);
		pg.edgeDistance = new DoubleRange(100d, 100d);		
		graph = new ManetGraph<ManetVertex, ManetEdge>(new ManetGraphSupplier.ManetVertexSupplier(), new ManetGraphSupplier.ManetEdgeSupplier());
		graph.generateGridGraph(pg);			
		panel.updateVisualGraph(graph.toVisualGraph());
		panel.repaint();
	}
	
	private static void addVertex(Input input) {				
		graph.addVertex(
				input.DOUBLE_TUPLE.getFirst(), 
				input.DOUBLE_TUPLE.getSecond());
    }
	
	private static void addEdge(Input input) {
		graph.addEdge(
				graph.getVertex(input.INT_TUPLE.getFirst()), 
				graph.getVertex(input.INT_TUPLE.getSecond()));
    }
	
	private static void removeVertex(Input input) {
		
    }
	
	private static void removeEdge(Input input) {
		
    }
	
	
	private static void print(Input input) {
		
    }

	private static void plot(Input input) {		
				
	}
	
	private static void update(Input input) {

	}
	
	private static void importGraph(Input input) {
		
	}
	
	private static  void exportGraph(Input input) {
		
	}
	
	private static void toImage(Input input) {
		Container container = frame.getContentPane();
		BufferedImage image = new BufferedImage(container.getWidth(), container.getHeight(), BufferedImage.TYPE_INT_ARGB);
		container.paint(image.getGraphics());
		try {
			ImageIO.write(image, "PNG", new File("shot.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	private static void help(Input input) {

    }
	
	private static void printOptions() {										
		
	}	
		
	private static void clear(Input input) {

    }
	
	private static  void exit(Input input) {
    	System.out.println("Bye");
    	EXIT = true;
    }		
}
