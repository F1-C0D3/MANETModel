package de.manetmodel.app.treeparser;

public enum ValueType {
    INT {
	public String toString() {
	    return "INT";
	};
    },
    DOUBLE {
	public String toString() {
	    return "DOUBLE";
	};
    },
    STRING {
	public String toString() {
	    return "STRING";
	};
    },
}
