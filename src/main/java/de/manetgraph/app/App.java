package de.manetgraph.app;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
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
		public final String keyword;
		
		public Keyword(String keyword, String info) {
			this.keyword = keyword;
			this.info = info;
		}
	}
	
	public class Value extends Element {
		public final ValueType type;
		
		public Value(ValueType type, String info) {
			this.type = type;
			this.info = info;
		}
	}
		
	public enum ValueType {
		INT, DOUBLE, STRING, INT_INT, DOUBLE_DOUBLE;
	}
	
	public class Option {		
		
		public final Keyword flag;
		public final Keyword command;		
		private ArrayList<Keyword> arguments;
		private Value value;	
		public final Consumer<Input> function;
				
		public Option(String flag, String command, Consumer<Input> function) {
			this.flag = new Keyword(flag, "flag");
			this.flag.setMandatory();
			this.command = new Keyword(command, "command");
			this.command.setMandatory();
			this.arguments = new ArrayList<Keyword>();
			this.function = function;	
		}
		
		public void addArgument(Keyword argument) {
			this.arguments.add(argument);
		}
		
		public ArrayList<Keyword> getArguments(){
			return this.arguments;
		}
		
		public boolean requiresArgument() {
			for(Keyword argument : this.arguments)
				if(!argument.isOptional()) return true;
			return false;
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
		
		public Boolean requiresValue() {
			if(hasValue()) return !this.value.isOptional();
			return false;
		}
	}
	
	public static class Input {
		
		public String command;
		public String argument;	
		
		public Integer INT;
		public Double DOUBLE;
		public Tuple<Integer,Integer> INT_INT;		
		public Tuple<Double, Double> DOUBLE_DOUBLE;
		
		public Input() {}	
	}
	
	public ArrayList<Option> buildOptions() {
		
		ArrayList<Option> options = new ArrayList<Option>();
		
		Option option;

		// create 
		// create 100
		// create random 100		
		option = new Option("", "create", App::create);
		option.addArgument(new Keyword("random","create a random graph"));
		option.addArgument(new Keyword("grid","create a grid graph"));
		option.setValue(new Value(ValueType.INT, "number of nodes"));	
		options.add(option);
		
		// add vertex COORDINATE	
		option = new Option("", "add vertex", App::addVertex);
		option.setValue(new Value(ValueType.DOUBLE_DOUBLE, "Coordinate"));	
		options.add(option);
		
		// add edge ID, ID		
		option = new Option("", "add edge", App::addVertex);
		option.setValue(new Value(ValueType.INT_INT, "IDs of vertices"));	
		options.add(option);
		
		// print
		option = new Option("", "print", App::print);
		options.add(option);
		
		// plot
		option = new Option("", "plot", App::plot);
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
		options.add(option);
		
		return options;		
	}
	
	public static void main(String[] args) {
		
		System.out.println("Hi");
		App app = new App();
		Scanner scanner = new Scanner(System.in); 	 			
		ArrayList<Option> options = app.buildOptions();
    	String line = "";
    		
    	EXIT = false;
    	    		
    	do {	    		
    		line = scanner.nextLine();
    		
    		Input input = new Input();
    		
    		boolean COMMAND = false;
    		boolean ARGUMENT = false;
    		boolean VALUE = false;
    		
    		for(Option option : options) {
    			  		
    			// Flag & Command  			
    			if(line.startsWith(String.format("%s%s", option.flag.keyword, option.command.keyword))) {			
    				input.command = option.command.keyword;
    				COMMAND = true;
    				line = line.substring(String.format("%s%s", option.flag.keyword, option.command.keyword).length()).trim();
    				  				
    				// Argument
    				do {
    				    for(Keyword argument : option.getArguments()) {
    				    	if(line.startsWith(argument.keyword)) {
    				    		input.argument = argument.keyword;
    							line = line.substring(argument.keyword.length()).trim();
    							ARGUMENT = true;
    							break;
    						}			
    					}	
    				    
    				    if(!ARGUMENT && option.requiresArgument()) 
    				    	for(Keyword argument : option.getArguments())
    				    		System.out.println(String.format("%s : %s", argument.keyword, argument.info));
    				    
    				}
    				while(!ARGUMENT && option.requiresArgument());
    								
    				// Value   				
    				if(option.hasValue()) {
    					do {					
    						switch(option.getValue().type) {					
    							case INT: 
    								try {
        								input.INT = Integer.parseInt(line);
    									VALUE = true;
    								}
    								catch (Exception e){
    									System.out.println("..");
    								}								
    								break;					
    							case DOUBLE: break;
    							case INT_INT: break;
    							case DOUBLE_DOUBLE: break; 								
    							default: break;    						
    						}  					
    					}
    					while(!VALUE && option.requiresValue());
    				}  
    				option.function.accept(input);
    			}
    		}	
    		
			if(!COMMAND)
				System.out.println(String.format("\"%s\" is not a valid command, please type \"%s\" for assitance", line, "help"));
    	}
    	while(!EXIT);  
    	
    	scanner.close(); 	
		
	}
	
	private static void create(Input input) {		
				
		graph = new ManetGraph<ManetVertex, ManetEdge>(new ManetGraphSupplier.ManetVertexSupplier(), new ManetGraphSupplier.ManetEdgeSupplier());
		
		Playground pg;
		
		switch(input.argument) {		
			case "random":
				pg = new Playground();
				pg.height = new IntRange(0, 1000);
				pg.width = new IntRange(0, 1000);
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
			default:
				break;	
		}		
	}
	
	private static void addVertex(Input input) {
		
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
