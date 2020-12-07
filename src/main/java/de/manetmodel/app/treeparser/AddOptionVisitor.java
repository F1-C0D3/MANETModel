package de.manetmodel.app.treeparser;

import de.manetmodel.app.treeparser.KeyOption;
import de.manetmodel.app.treeparser.ValueOption;

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
