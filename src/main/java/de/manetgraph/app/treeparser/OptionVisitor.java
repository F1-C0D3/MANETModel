package de.manetgraph.app.treeparser;

import de.manetgraph.app.treeparser.TreeParser.KeyOption;
import de.manetgraph.app.treeparser.TreeParser.ValueOption;

interface OptionVisitor {
	void visit(Option option);
	void visit(KeyOption keyOption);
    void visit(ValueOption valueOption);	
}