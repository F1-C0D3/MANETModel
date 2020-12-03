package de.manetgraph.app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import de.manetmodel.graph.ManetEdge;
import de.manetmodel.graph.ManetGraph;
import de.manetmodel.graph.ManetGraphSupplier;
import de.manetmodel.graph.ManetVertex;
import de.manetmodel.graph.Playground;
import de.manetmodel.graph.Playground.DoubleRange;
import de.manetmodel.graph.Playground.IntRange;
import de.manetmodel.util.Tuple;
import de.manetmodel.visualization.VisualGraphPanel;

public class App {
	
	static ManetGraph<ManetVertex, ManetEdge> graph;
	static VisualGraphPanel<ManetVertex, ManetEdge> panel;
	static boolean EXIT = false;
	
	static String delimiter = " ";

	public class Element { }
	
	public class Key extends Element{ 
		public final String string;
		private String aliases;
		
		public Key(String string) {
			this.string = string;
		}
				
		@Override
		public String toString() {
			return this.string;
		}
	}
	
	public enum ValueType {
		INT, DOUBLE, STRING, INT_INT, DOUBLE_DOUBLE;
	}
	
	public class Value extends Element{
		private ValueType type;
					
		public Value(ValueType type) {
			this.type = type;
		}
		
		public ValueType getType() {
			return this.type;
		}
	}
	
	public class Function extends Element {
		private Consumer<Input> consumer;
		
		public Function(Consumer<Input> consumer) {
			this.consumer = consumer;
		}
		
		public Consumer<Input> getConsumer(){
			return this.consumer;
		}
	}
	
	public class Info extends Element {
		public final String string;
		
		public Info(String string) {
			this.string = string;
		}
	}
	
	
	public class OptionManager {
		private ArrayList<Option> option;
	    private ArrayList<KeyOption> keyOptions;
	    private ArrayList<ValueOption> valueOptions;

	    private AddOptionVisitor addOptionsVisitor = new AddOptionVisitor(this);
	    private PrintOptionVisitor printOptionVisitor = new PrintOptionVisitor(this);
 
	    public OptionManager() {
	    	this.option = new ArrayList<>();
	    	this.keyOptions = new ArrayList<>();
	    	this.valueOptions = new ArrayList<>();
	    }
	    
	    public void add(Option option) {
	    	option.accept(addOptionsVisitor);
	    }
	    
	    public void add(KeyOption keyOption) {
	    	keyOptions.add(keyOption);
	    }
	    
	    public void add(ValueOption valueOption) {
	    	valueOptions.add(valueOption);
	    }
	   
	    public void print(Option option) {
	    	option.accept(printOptionVisitor);
	    }
	    
	    public void print(KeyOption keyOption) {
	    	System.out.println(String.format("%s%s", keyOption.getFlag().toString(), keyOption.getCommand().toString()));
	    }
	    
	    public void print(ValueOption valueOption) {
	    	System.out.println(String.format("%s", valueOption.getValue().getType().toString()));
	    }
	    
	    public ArrayList<KeyOption> getKeyOptions() {
	    	return this.keyOptions;
	    }
	    
	    public ArrayList<ValueOption> getValueOptions() {
	    	return this.valueOptions;
	    }
	}

	interface OptionVisitor {
	    void visit(KeyOption keyOption);
	    void visit(ValueOption valueOption);	
	}
	
	public class AddOptionVisitor implements OptionVisitor {
	    private OptionManager optionManager;

	    public AddOptionVisitor(OptionManager optionManager) {
	        this.optionManager = optionManager;
	    }
	    	   
	    public void visit(KeyOption keyOption) {
	    	this.optionManager.add(keyOption);
	    }

	    public void visit(ValueOption valueOption) {
	    	this.optionManager.add(valueOption);
	    }
	}
	
	public class PrintOptionVisitor implements OptionVisitor {

		private OptionManager optionManager;

	    public PrintOptionVisitor(OptionManager optionManager) {
	        this.optionManager = optionManager;
	    }
		    
		public void visit(KeyOption keyOption) {
			this.optionManager.print(keyOption);
		}

		public void visit(ValueOption valueOption) {
			this.optionManager.print(valueOption);
		}	
	}
		
	public abstract class Option
	{
		private Info info;
		private Function function;
		private ArrayList<Option> options;
		private boolean isOptional;		
		
		public Option() {
			this.isOptional = false;
		}
		
		public abstract void accept(OptionVisitor optionVisitor);

		public boolean add(Option option) {
			if(options == null)
				this.options = new ArrayList<Option>();
			return this.options.add(option);
		}
		
		public Boolean hasOptions() {
			return this.options != null;
		}
			
		public ArrayList<Option> getOptions() {
			return this.options;
		}
		
		public ArrayList<KeyOption> getKeyOptions(){
	        OptionManager optionManager = new OptionManager();        
			for(Option option : this.options) 
				optionManager.add(option);
			return optionManager.getKeyOptions();
		}
		
		public ArrayList<ValueOption> getValueOptions(){
	        OptionManager optionManager = new OptionManager();        
			for(Option option : this.options) 
				optionManager.add(option);
			return optionManager.getValueOptions();
		}
		
		public Info getInfo() {
			return this.info;
		}
		
		public Function getFunction() {
			return this.function;
		}
		
		public Boolean isOptional() {
			return isOptional;
		} 
		
		public void print(){
	        OptionManager optionManager = new OptionManager();        
			optionManager.print(this);
		}
	}
	
	public class KeyOption extends Option {
		private Key flag;
		private Key command;
		
		public KeyOption(String flag, Key command, Info info, Function function, boolean isOptional) {
			this.flag = new Key(flag);
			this.command = command;
			super.info = info;
			super.isOptional = isOptional;
			super.function = function;	
		}
		
		public KeyOption(String flag, Key command, Info info, Function function) {
			this.flag = new Key(flag);
			this.command = command;
			super.info = info;
			super.function = function;	
		}	
				
		public KeyOption(Key command, Info info, Function function, boolean isOptional) {
			this.flag = new Key("");
			this.command = command;
			super.info = info;
			super.isOptional = isOptional;
			super.function = function;	
		}
		
		public KeyOption(Key command, Info info, Function function) {
			this.flag = new Key("");
			this.command = command;
			super.info = info;
			super.function = function;	
		}
				
		public KeyOption(Key command, Info info,  boolean isOptional) {
			this.flag = new Key("");
			this.command = command;
			super.info = info;
			super.isOptional = isOptional;
		}
		
		public KeyOption(Key command, Info info) {
			this.flag = new Key("");
			this.command = command;
			super.info = info;
		}
		
		public void accept(OptionVisitor visitor) {
	        visitor.visit(this);
	    }
		
		public Key getFlag() {
			return this.flag;
		}
		
		public Key getCommand() {
			return this.command;
		}
	}
	
	public class ValueOption extends Option{
		private Value value;
		
		public ValueOption(Value value, Info info, Function function, boolean isOptional) {
			this.value = value;
			super.info = info;
			super.function = function;
			super.isOptional = isOptional;
		}
		
		public ValueOption(Value value, Info info, Function function) {
			this.value = value;
			super.info = info;
			super.function = function;
		}
		
		public ValueOption(Value value, Info info, boolean isOptional) {
			this.value = value;
			super.info = info;
			super.isOptional = isOptional;
		}
		
		public ValueOption(Value value, Info info) {
			this.value = value;
			super.info = info;
		}
		
		public void accept(OptionVisitor visitor) {
	        visitor.visit(this);
	    }
		
		public Value getValue() {
			return this.value;
		}
	}
				
	public static class Input {
		
		ValueType valueType;
		
		public Integer INT;
		public Double DOUBLE;
		public String STRING;
		public Tuple<Integer,Integer> INT_TUPLE;		
		public Tuple<Double, Double> DOUBLE_TUPLE;
			
		public Input() {}	
	}
	

	public Option buildOptions() {
		
		KeyOption options = new KeyOption(new Key(""), new Info(""));
						
		// create 
		// create 100
		// create random 100		
		KeyOption create = new KeyOption(new Key("create"), new Info("create an empty graph"), new Function(App::createEmpty));	
		
		KeyOption empty = new KeyOption(new Key("empty"), new Info("create an empty graph"), new Function(App::createEmpty));
		create.add(empty);		
		KeyOption random = new KeyOption(new Key("random"), new Info("create a random graph"), new Function(App::createRandom));
		random.add(new ValueOption(new Value(ValueType.INT), new Info("number of nodes"), new Function(App::createRandom))); 	
		create.add(random);		
		KeyOption grid = new KeyOption(new Key("random"), new Info("create a grid graph"), new Function(App::createGrid));
		grid.add(new ValueOption(new Value(ValueType.INT), new Info("number of nodes"), new Function(App::createGrid))); 
		create.add(grid);		
		options.add(create);
				
		// add vertex
		// add edge
		KeyOption add = new KeyOption(new Key("add"), new Info("add new vertex or edge to graph"));		
		KeyOption vertex = new KeyOption(new Key("vertex"), new Info("add a new vertex to graph"), new Function(App::addVertex));
		vertex.add(new ValueOption(new Value(ValueType.DOUBLE_DOUBLE), new Info("coordinate"), new Function(App::addVertex))); 
		add.add(vertex);		
		KeyOption edge = new KeyOption(new Key("edge"), new Info("add a new edge to graph"), new Function(App::addEdge));
		edge.add(new ValueOption(new Value(ValueType.INT_INT), new Info("IDs of source and target vertices"), new Function(App::addEdge))); 
		add.add(vertex);	
		options.add(add);
		
		// remove vertex
		// remove edge
		KeyOption remove = new KeyOption(new Key("add"), new Info("add new vertex or edge to graph"));		
		KeyOption removeVertex = new KeyOption(new Key("vertex"), new Info("remove vertex from graph"));
		vertex.add(new ValueOption(new Value(ValueType.INT), new Info("vertex ID"), new Function(App::removeVertex))); 
		remove.add(removeVertex);		
		KeyOption removeEdge = new KeyOption(new Key("edge"), new Info("remove edege from graph"));
		removeEdge.add(new ValueOption(new Value(ValueType.INT), new Info("edgeID"), new Function(App::removeEdge))); 
		remove.add(removeEdge);		
		options.add(remove);
			
		// print
		KeyOption print = new KeyOption(new Key("print"), new Info("print graph"), new Function(App::print));	
		options.add(print);
					
		// import	
		KeyOption importGraph = new KeyOption(new Key("import"), new Info("import graph"));	
		importGraph.add(new ValueOption(new Value(ValueType.STRING), new Info("path and filename"), new Function(App::importGraph))); 
		options.add(importGraph);
				
		// export
		KeyOption exportGraph = new KeyOption(new Key("export"), new Info("export graph"));	
		importGraph.add(new ValueOption(new Value(ValueType.STRING), new Info("path and filename"), new Function(App::exportGraph))); 
		options.add(exportGraph);
				
		// help		
		// help create
		KeyOption help = new KeyOption(new Key("help"), new Info("the help"));	
		help.add(new ValueOption(new Value(ValueType.STRING), new Info("name of command"), new Function(App::importGraph))); 
		options.add(help);

		// plot
		KeyOption plot = new KeyOption(new Key("plot"), new Info("plot graph"), new Function(App::plot));	
		options.add(plot);
		
		// clear
		KeyOption clear = new KeyOption(new Key("clear"), new Info("clear console"), new Function(App::clear));	
		options.add(clear);
		
		// exit
		KeyOption exit = new KeyOption(new Key("exit"), new Info("close app"), new Function(App::exit));	
		options.add(exit);
				
		return options;		
	}
	

	public static void consume(String string, Option options, Input input) {
						
		string = string.trim();	
										
		// Consume FLAG & COMMAND
			
		for(KeyOption keyOption : options.getKeyOptions()) {
							
			if(string.startsWith(String.format("%s%s", keyOption.getFlag().toString(), keyOption.getCommand().toString()))) {	
					
				System.out.println(keyOption.getCommand().toString());
								
				string = string.substring(String.format("%s%s", keyOption.getFlag(), keyOption.getCommand().toString()).length());
				
				if(string.length() > 0) 				
					consume(string, keyOption, input);			
				else
					keyOption.getFunction().getConsumer().accept(input);
			}
		}
			
		// Consume VALUE
			
		for(ValueOption valueOption : options.getValueOptions()) {					
			if(string.length() > 0) {		
				try {
					switch(valueOption.getValue().getType()) {  					
						case INT: 
							input.INT = Integer.parseInt(string);
							break;
						case STRING: 
							break;
						case DOUBLE:
							input.DOUBLE = Double.parseDouble(string);
							break;					
						case INT_INT:  					
							String[] intParts = string.split(delimiter);
							input.INT_TUPLE = new Tuple<Integer,Integer>(
									Integer.parseInt(intParts[0].trim()), 
									Integer.parseInt(intParts[1].trim()));
							break; 			
						case DOUBLE_DOUBLE:     					
							String[] doubleParts = string.split(delimiter);
							input.DOUBLE_TUPLE = new Tuple<Double,Double>(
									Double.parseDouble(doubleParts[0].trim()), 
									Double.parseDouble(doubleParts[1].trim()));
							break; 						
						default: break;    						
					}  
					valueOption.getFunction().getConsumer().accept(input);
				}
				catch(Exception e) {}		
			}		
		}
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
   		
   		JFrame frame = new JFrame("VisualGraphPanel");
   		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   		frame.getContentPane().add(panel);
   		frame.pack();
   		frame.setLocationRelativeTo(null);
   		frame.setVisible(true); 
			
		System.out.println("Hi");
		App app = new App();	
		Option options = app.buildOptions();			
		Scanner scanner = new Scanner(System.in); 	 			
		do consume(scanner.nextLine(), options, new Input()); while(!EXIT);	
    	scanner.close(); 	
	}

	
	private static void createEmpty(Input input) {		
		
		graph = new ManetGraph<ManetVertex, ManetEdge>(new ManetGraphSupplier.ManetVertexSupplier(), new ManetGraphSupplier.ManetEdgeSupplier());
		
		update(input);
	}
	
	private static void createRandom(Input input) {	
		
		System.out.println("createRandom");
		System.out.println(input.INT);
		
		Playground pg = new Playground();
		pg.height = new IntRange(0, 10000);
		pg.width = new IntRange(0, 10000);
		pg.edgeCount = new IntRange(4, 4);
		pg.vertexCount = new IntRange(input.INT, input.INT);
		pg.edgeDistance = new DoubleRange(100d, 100d);	
		
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
		graph.generateRandomGraph(pg);	
		
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
	
	private static void exportGraph(Input input) {
		
	}
	
	private static void help(Input input) {
		System.out.println(String.format("the help",""));
    }
	
	private static void printOptions(Option options) {									
		options.print();			
		if(options.hasOptions()) 
			for(Option option : options.getOptions()) 
				printOptions(option);		
	}
	
	private static void clear(Input input) {

    }
	
	private static void exit(Input input) {
    	System.out.println("Bye");
    	EXIT = true;
    }		
}
