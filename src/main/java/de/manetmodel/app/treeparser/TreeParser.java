package de.manetmodel.app.treeparser;

import java.util.Scanner;

import de.manetmodel.util.Tuple;

class State {
	Option option;
	Input input;
	
	public State(Option option, Input input) {
		this.option = option;
		this.input = input;
	}
	
	public Option getOption() {
		return this.option;
	}
}

public class TreeParser {
		
	RootOption options;
	State state;
	
	static String delimiter = " ";
	
	public TreeParser() {
		options = new RootOption();
	}
	
	public void addOption(Option option) {
		this.options.add(option);
	}
	
	public Option getOptions() {
		return this.options;
	}
	
	private void setState(Option option, Input input) {
		this.state = new State(option, input);
	}
	
	private State getState() {
		State state = this.state;
		this.state = null;
		return state;
	}
	
	private Boolean hasState() {
		return state != null;
	}
	
	public void consume(String string, Option option, Input input) {
		
		string = string.trim();
		
		if(this.hasState()) 
			option = this.getState().getOption();
		
		if(string.isEmpty()) {
			if(option.requiresOption()) {
				System.out.format("%s\n", option.getOptions().toString());
				this.setState(option, input);
				return;		
			}
			else option.getFunction().getConsumer().accept(input);
		}
									
		for(KeyOption keyOption : option.getKeyOptions()) 					
			if(string.startsWith(keyOption.getFlag().toString() + keyOption.getCommand().toString())) 
				consume(string.substring(keyOption.getFlag().toString().length() + keyOption.getCommand().toString().length()), keyOption, input);
								
		for(ValueOption valueOption : option.getValueOptions()) {			
			Scanner scanner = new Scanner(string);
			scanner.skip("[^0-9]*");		
			try {
				switch(valueOption.getValue().getType()) {  					
					case INT: 
						input.INT = scanner.nextInt();
						consume(string.substring(input.INT.toString().length()), valueOption, input);
						break;
					case STRING: 
						break;
					case DOUBLE:
						input.DOUBLE = scanner.nextDouble();
						consume(string.substring(input.DOUBLE.toString().length()), valueOption, input);
						break;					
					case INT_TUPLE:  					
						String[] intParts = string.split(delimiter);
						input.INT_TUPLE = new Tuple<Integer,Integer>(Integer.parseInt(intParts[0].trim()), Integer.parseInt(intParts[1].trim()));
						break; 			
					case DOUBLE_TUPLE:     					
						String[] doubleParts = string.split(delimiter);
						input.DOUBLE_TUPLE = new Tuple<Double,Double>(Double.parseDouble(doubleParts[0].trim()), Double.parseDouble(doubleParts[1].trim()));
						break; 						
				}  			
			}
			catch(Exception e) {}		
		}		
	}		
}
