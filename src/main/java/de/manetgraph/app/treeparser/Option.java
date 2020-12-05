package de.manetgraph.app.treeparser;

import java.util.ArrayList;
import java.util.Iterator;

import de.manetgraph.app.treeparser.KeyOption;
import de.manetgraph.app.treeparser.OptionManager;
import de.manetgraph.app.treeparser.OptionVisitor;
import de.manetgraph.app.treeparser.ValueOption;

public abstract class Option
{
	protected Info info;
	protected Function function;
	private ArrayList<Option> options;
	protected boolean isOptional;		
	
	public Option() {
		this.isOptional = false;
		this.options = new ArrayList<Option>();
	}
	
	public void accept(OptionVisitor optionVisitor) {
		optionVisitor.visit(this);
	}

	public void add(Option option) {
		options.add(option);
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
	
	@Override
	public String toString() {
		 StringBuilder stringBuilder = new StringBuilder();
	     this.print(stringBuilder, " ", " ");
	     return stringBuilder.toString();
	}
	
	protected String buildString() {
		return "";
	}
	
	private void print(StringBuilder stringBuilder, String prefix, String childrenPrefix) {
		stringBuilder.append(prefix);
		stringBuilder.append(this.buildString());
		stringBuilder.append('\n');
        
        Iterator<Option> iterator = options.iterator(); 
        
        while (iterator.hasNext()) {
        	Option next = iterator.next();   
        	
        	if (iterator.hasNext()) {
                next.print(stringBuilder, childrenPrefix + "├── ", childrenPrefix + "│   ");
            } else {
                next.print(stringBuilder, childrenPrefix + "└── ", childrenPrefix + "    ");
            }          
        }
    }
}