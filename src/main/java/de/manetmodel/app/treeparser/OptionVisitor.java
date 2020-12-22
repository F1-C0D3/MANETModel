package de.manetmodel.app.treeparser;

interface OptionVisitor {
	void visit(Option option);
	void visit(KeyOption keyOption);
    void visit(ValueOption valueOption);	
}