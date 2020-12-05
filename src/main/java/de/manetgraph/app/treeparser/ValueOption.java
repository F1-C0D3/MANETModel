package de.manetgraph.app.treeparser;

public class ValueOption extends Option{
	private Value value;
	
	public ValueOption(Value value, Info info, Function function, boolean isOptional) {
		this.value = value;
		super.info = info;
		super.function = function;
		super.isOptional = isOptional;
	}
	
	public ValueOption(Value value, Info info, Function function) {
		this.value = value;
		super.info = info;
		super.function = function;
	}
	
	public ValueOption(Value value, Info info, boolean isOptional) {
		this.value = value;
		super.info = info;
		super.isOptional = isOptional;
	}
	
	public ValueOption(Value value, Info info) {
		this.value = value;
		super.info = info;
	}
	
	public void accept(OptionVisitor visitor) {
        visitor.visit(this);
    }
	
	public Value getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return String.format("%s : %s", this.getValue().toString(), this.getInfo().toString());
	}
	
	@Override
	protected String buildString() {
		return value.getType().toString();
	}
}
