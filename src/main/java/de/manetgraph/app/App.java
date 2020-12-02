package de.manetgraph.app;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.function.Consumer;

import javax.swing.JFrame;

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
	
	public class Element {
		boolean mandatory;
		public String info;
		
		public Element() {
			this.mandatory = false;
		}
		
		public void setMandatory() {
			this.mandatory = true;
		}
		
		public Boolean isOptional() {
			return !this.mandatory;
		}
	}
	
	public class Keyword extends Element { 
		public final String string;
		
		public Keyword(String string, String info) {
			this.string = string;
			this.info = info;
		}
		
		public Keyword(String string, String info, boolean mandatory) {
			this.string = string;
			this.info = info;
			this.mandatory = true;
		}
	}
	
	public enum ValueType {
		INT, DOUBLE, STRING, INT_INT, DOUBLE_DOUBLE;
	}
	
	public class Value extends Element {
		public final ValueType type;
		
		public Value(ValueType type, String info) {
			this.type = type;
			this.info = info;
		}
		
		public Value(ValueType type, String info, boolean mandatory) {
			this.type = type;
			this.info = info;
			this.mandatory = mandatory;
		}
	}
			
	public class Option extends Argument {	
	
		private ArrayList<Argument> arguments;
		
		public Option(String flag, String command, Value value, Consumer<Input> function) {			
			super(new Keyword(flag, "flag", true), new Keyword(command, "command", true), value, function);		
			this.arguments = new ArrayList<Argument>();
		}
			
		public void addArgument(Argument argument) {
			this.arguments.add(argument);
		}	
		
		public ArrayList<Argument> getArguments(){
			return this.arguments;
		}
		
		@Override
		public boolean requiresKeyOrValue() {
			for(Argument argument : arguments)
				for(Element element : argument.getElements())
					if(!element.isOptional()) return true;
			return false;
		}		
	}
	
	public class Argument {

		private Keyword flag;
		private Keyword command;		
		private Value value;	
		private Consumer<Input> function;		
		protected LinkedList<Element> elements;
		
		public Argument(Keyword flag, Keyword command, Value value, Consumer<Input> function) {
			this.flag = flag;
			this.command = command;
			this.value = value;
			this.function = function;	
			this.elements = new LinkedList<Element>();
		}		
		
		public Argument(Keyword command, Value value, Consumer<Input> function) {
			this.command = command;
			this.value = value;
			this.function = function;	
			this.elements = new LinkedList<Element>();
		}
		
		public Argument(Keyword command, Consumer<Input> function) {
			this.command = command;
			this.function = function;	
			this.elements = new LinkedList<Element>();
		}
		
		public LinkedList<Element> getElements() {
			return this.elements;
		}	
		
		public void setValue(Value value) {
			this.value = value;
		}
				
		public Value getValue() {
			return this.value;
		}
		
		public Boolean hasValue() {
			return this.value != null;
		}
		
		public boolean requiresKeyOrValue() {
			for(Element element : elements)
				if(!element.isOptional()) return true;
			return false;
		}
	}	
	
	public static class Input {
		
		LinkedList<Element> elements;
		
		ValueType valueType;
		public Integer INT;
		public Double DOUBLE;
		public String STRING;
		public Tuple<Integer,Integer> INT_INT;		
		public Tuple<Double, Double> DOUBLE_DOUBLE;
		
		public Input(Option option) {
			this.command = option.command.string;
			if(option.hasValue()) this.valueType = option.getValue().type;
		}	
		
		public Boolean containsArgument() {
			return argument != null;
		}
		
		public Boolean containsValue() {
			switch(valueType) {
				case INT: return INT != null;
				case DOUBLE: return DOUBLE != null;
				case STRING: return STRING != null;
			}
			return false;
		}
	}
	
	public ArrayList<Option> buildOptions() {
		
		ArrayList<Option> options = new ArrayList<Option>();
				
		Option option;
		
		// create 
		// create 100
		// create random 100		
		option = new Option("", "create", App::create);
		/*option.addArgument(new Keyword("empty","create an empty graph"));
		option.addArgument(new Keyword("random","create a random graph"));
		option.addArgument(new Keyword("grid","create a grid graph"));
		option.setValue(new Value(ValueType.INT, "number of vertices"));	
		options.add(option);*/
		
		/*// add vertex 1.0,1,0	
		option = new Option("", "add vertex", App::addVertex);
		option.setValue(new Value(ValueType.DOUBLE_DOUBLE, "Coordinate", true));	
		options.add(option);
		
		// remove vertex id	
		option = new Option("", "remove vertex", App::addVertex);
		option.setValue(new Value(ValueType.INT, "vertex ID", true));	
		options.add(option);
		
		// remove edge id	
		option = new Option("", "remove edge", App::addVertex);
		option.setValue(new Value(ValueType.INT, "edge ID", true));	
		options.add(option);
		
		// print
		option = new Option("", "print", App::print);
		options.add(option);
				
		// plot
		option = new Option("", "plot", App::plot);
		options.add(option);
		
		// plot
		option = new Option("", "update", App::plot);
		options.add(option);
		
		// import
		option = new Option("", "import", App::importGraph);
		options.add(option);
				
		// export
		option = new Option("", "export", App::exportGraph);
		options.add(option);
		
		// help		
		// help create
		option = new Option("", "help", App::help);
		option.setValue(new Value(ValueType.STRING, "command name"));	
		options.add(option);
		
		// exit		
		option = new Option("", "exit", App::exit);
		options.add(option);*/
		
		return options;		
	}
	
	public static void main(String[] args) {
		
		System.out.println("Hi");
		App app = new App();
		Scanner scanner = new Scanner(System.in); 	 			
		ArrayList<Option> options = app.buildOptions();
    	String line = "";
    		
    	EXIT = false;
    	    		
    	/*do {	    	  		   	
    		boolean COMMAND = false;
    		boolean ARGUMENT = false;
    		boolean VALUE = false;
    		    		
    		line = scanner.nextLine().toLowerCase();
    		
    		for(Option option : options) {
    			  		
    			Input input = new Input(option);
    			
    			if(line.startsWith(String.format("%s%s", option.flag.string, option.command.string.toLowerCase()))) {			
    				COMMAND = true;
    				line = line.substring(String.format("%s%s", option.flag.string, option.command.string).length()).trim();
    				  				
    				do {
    					// Match arguments
    				    for(Keyword argument : option.getArguments()) {
    				    	if(line.startsWith(argument.string.toLowerCase())) {
    				    		input.argument = argument.string;
    							line = line.substring(argument.string.length()).trim();
    							ARGUMENT = true;
    							break;
    						}			
    					}	
    				    
    				    // Print arguments options
    				    if(!ARGUMENT && option.requiresKeyOrValue()) 
    				    	for(Keyword argument : option.getArguments())
    				    		System.out.println(String.format("%s : %s", argument.keyword, argument.info));
    				    
    				}
    				while(!ARGUMENT && option.requiresKeyOrValue());
    								
    				if(option.hasValue() && line.length() > 0) {
   
    					do {	
    						try {
    							String[] parts;						
    							switch(option.getValue().type) {  					
    								case INT: 
    									input.INT = Integer.parseInt(line);
    									break;
    								
    								case DOUBLE:
    									input.DOUBLE = Double.parseDouble(line);
    									break;
        							
    								case INT_INT:  					
    									parts = line.split(delimiter);
    									input.INT_INT = new Tuple<Integer,Integer>(Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim()));
    									break; 
    								
    								case DOUBLE_DOUBLE:     					
    									parts = line.split(delimiter);
    									input.DOUBLE_DOUBLE = new Tuple<Double,Double>(Double.parseDouble(parts[0].trim()), Double.parseDouble(parts[1].trim()));
    									break; 	
    								
    								case STRING: break;
    							
    								default: break;    						
    							}  
    							VALUE = true;					
    						}
    						catch(Exception e) {
    							System.out.println(String.format("\"%s\" is not a valid %s, please enter a new value", line, option.getValue().type.toString(), "help"));
    						}
    						    						
    						if(!VALUE)
    							if(option.requiresKeyOrValue()) 
    								System.out.println(String.format("\"%s\" requires a value, pleaser enter a value"));
    							if(!option.getValue().isOptional())	
    								line = scanner.nextLine().toLowerCase();		  							
    					}
    					while(!VALUE);
    				}  
    				option.function.accept(input);
    			}
    		}	
    		
			if(!COMMAND)
				System.out.println(String.format("\"%s\" is not a valid command, please type \"%s\" for assistance", line, "help"));
    	}
    	while(!EXIT);  */
    	
    	scanner.close(); 	
		
	}
	
	private void create(Input input) {		
				
		graph = new ManetGraph<ManetVertex, ManetEdge>(new ManetGraphSupplier.ManetVertexSupplier(), new ManetGraphSupplier.ManetEdgeSupplier());
		
		Playground pg;
		
		if(input.containsArgument()) {		
			switch(input.argument) {	
				case "random":
					pg = new Playground();
					pg.height = new IntRange(0, 10000);
					pg.width = new IntRange(0, 10000);
					pg.edgeCount = new IntRange(4, 4);
					pg.vertexCount = new IntRange(input.INT, input.INT);
					pg.edgeDistance = new DoubleRange(100d, 100d);		
					graph.generateRandomGraph(pg);
					break;
					
				case "grid":
					pg = new Playground();
					pg.height = new IntRange(0, 1000);
					pg.width = new IntRange(0, 1000);
					pg.edgeCount = new IntRange(4, 4);
					pg.vertexCount = new IntRange(input.INT, input.INT);
					pg.edgeDistance = new DoubleRange(100d, 100d);
					graph.generateGridGraph(pg);
					break;
			}
		}
	}
	
	private static void addVertex(Input input) {				
		if(graph != null)
			graph.addVertex(input.DOUBLE_DOUBLE.getFirst(), input.DOUBLE_DOUBLE.getSecond());
    }
	
	private static void addEdge(Input input) {
		
    }
	
	private static void print(Input input) {
		
    }

	private static void plot(Input input) {		
		
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
	}
	
	private static void update(Input input) {
		if(panel != null)
			panel.updateVisualGraph(graph.toVisualGraph());
			panel.repaint();
	}
	
	private static void importGraph(Input input) {
		
	}
	
	private static void exportGraph(Input input) {
		
	}
	
	private static void help(Input input) {
		System.out.println(String.format("the help",""));
    }
	
	private static void exit(Input input) {
    	EXIT = true;
    	System.out.println("Bye");
    }
		
}
