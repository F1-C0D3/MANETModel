package de.manetgraph.app.treeparser;

import de.manetmodel.util.Tuple;

public class TreeParser {
		
	RootOption options;
	
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
		
	public void consume(String string, Option options, Input input) {
		
		string = string.trim();	
										
		for(KeyOption keyOption : options.getKeyOptions()) 
		{						
			if(string.startsWith(String.format("%s%s", keyOption.getFlag().toString(), keyOption.getCommand().toString()))) 
			{															
				string = string.substring(String.format("%s%s", keyOption.getFlag(), keyOption.getCommand().toString()).length());			
				
				if(string.length() > 0) 				
					consume(string, keyOption, input);			
				else
					keyOption.getFunction().getConsumer().accept(input);
			}
		}
			
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
						case INT_TUPLE:  					
							String[] intParts = string.split(delimiter);
							input.INT_TUPLE = new Tuple<Integer,Integer>(
									Integer.parseInt(intParts[0].trim()), 
									Integer.parseInt(intParts[1].trim()));
							break; 			
						case DOUBLE_TUPLE:     					
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
}
