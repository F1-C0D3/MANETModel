package de.manetgraph.app.treeparser;

import de.manetgraph.app.treeparser.TreeParser.KeyOption;
import de.manetgraph.app.treeparser.TreeParser.ValueOption;

public class AddOptionVisitor implements OptionVisitor {
    private OptionManager optionManager;

    public AddOptionVisitor(OptionManager optionManager) {
        this.optionManager = optionManager;
    }
    	
    public void visit(Option option) {
    	this.optionManager.add(option);
    }
    
    public void visit(KeyOption keyOption) {
    	this.optionManager.add(keyOption);
    }

    public void visit(ValueOption valueOption) {
    	this.optionManager.add(valueOption);
    }
}
