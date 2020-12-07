package de.manetmodel.app.treeparser;

public class ValueOption extends Option{
	private Value value;
	
	public ValueOption(Value value, Info info, Function function, Requirement requirement) {
		this.value = value;
		super.info = info;
		super.function = function;
		super.requirement = requirement;
	}
	
	public ValueOption(Value value, Info info, Function function) {
		this.value = value;
		super.info = info;
		super.function = function;
	}
	
	public ValueOption(Value value, Info info, Requirement requirement) {
		this.value = value;
		super.info = info;
		super.requirement = requirement;
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
	protected String buildString() {
		return String.format("ValueOpton(ValueType(\"%s\"), Requirement(\"%s\")", value.getType().toString(), this.getRequirement().toString());
	}
}
