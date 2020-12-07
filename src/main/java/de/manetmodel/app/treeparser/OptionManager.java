package de.manetmodel.app.treeparser;

import java.util.ArrayList;

import de.manetmodel.app.treeparser.AddOptionVisitor;
import de.manetmodel.app.treeparser.KeyOption;
import de.manetmodel.app.treeparser.ValueOption;

public class OptionManager {
	private ArrayList<Option> option;
    private ArrayList<KeyOption> keyOptions;
    private ArrayList<ValueOption> valueOptions;

    private AddOptionVisitor addOptionsVisitor = new AddOptionVisitor(this);

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

    public ArrayList<KeyOption> getKeyOptions() {
    	return this.keyOptions;
    }
    
    public ArrayList<ValueOption> getValueOptions() {
    	return this.valueOptions;
    }
}
