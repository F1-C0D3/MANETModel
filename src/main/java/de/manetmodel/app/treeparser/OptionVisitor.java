package de.manetmodel.app.treeparser;

import de.manetmodel.app.treeparser.KeyOption;
import de.manetmodel.app.treeparser.ValueOption;

interface OptionVisitor {
	void visit(Option option);
	void visit(KeyOption keyOption);
    void visit(ValueOption valueOption);	
}