package de.manetgraph.app.treeparser;

import java.util.function.Consumer;

import de.manetgraph.app.treeparser.TreeParser.Input;

public class Function extends Element {
	private Consumer<Input> consumer;
	
	public Function(Consumer<Input> consumer) {
		this.consumer = consumer;
	}
	
	public Consumer<Input> getConsumer(){
		return this.consumer;
	}
	
	@Override
	public String toString() {
		return this.consumer.toString();
	}
}
