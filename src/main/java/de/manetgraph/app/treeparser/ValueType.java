package de.manetgraph.app.treeparser;

public enum ValueType {
	INT{
		public String toString() { return "INT";};
	}, 
	DOUBLE{
		public String toString() { return "DOUBLE";};
	}, 
	STRING{
		public String toString() { return "STRING";};
	},
	INT_TUPLE {
		public String toString() { return "INT,INT";};
	}, 
	DOUBLE_TUPLE {
		public String toString() { return "DOUBLE,DOUBLE";};
	};
}
