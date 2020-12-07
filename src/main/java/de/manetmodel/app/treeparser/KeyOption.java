package de.manetmodel.app.treeparser;

public class KeyOption extends Option {
	private Key flag;
	private Key command;
	
	public KeyOption(String flag, Key command, Info info, Function function, Requirement requirement) {
		this.flag = new Key(flag);
		this.command = command;
		super.info = info;
		super.function = function;	
		super.requirement = requirement;
	}
	
	public KeyOption(String flag, Key command, Info info, Function function) {
		this.flag = new Key(flag);
		this.command = command;
		super.info = info;
		super.function = function;	
	}	
			
	public KeyOption(Key command, Info info, Function function, Requirement requirement) {
		this.flag = new Key("");
		this.command = command;
		super.info = info;
		super.function = function;
		super.requirement = requirement;
	}
	
	public KeyOption(Key command, Info info, Function function) {
		this.flag = new Key("");
		this.command = command;
		super.info = info;
		super.function = function;	
	}
			
	public KeyOption(Key command, Info info, Requirement requirement) {
		this.flag = new Key("");
		this.command = command;
		super.info = info;
		super.requirement = requirement;
	}
	
	public KeyOption(Key command, Info info) {
		this.flag = new Key("");
		this.command = command;
		super.info = info;
	}
	
	public void accept(OptionVisitor visitor) {
        visitor.visit(this);
    }
	
	public Key getFlag() {
		return this.flag;
	}
	
	public Key getCommand() {
		return this.command;
	}
	
	@Override
	protected String buildString() {
		return String.format("KeyOption(Key(\"%s\"), Info(\"%s\"), Requirement(%s))", 
				this.getCommand().toString(), 
				this.getInfo().toString(),
				this.getRequirement().toString());
	}
}
