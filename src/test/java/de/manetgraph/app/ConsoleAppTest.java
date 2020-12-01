package de.manetgraph.app;

public class ConsoleAppTest {
	
	public static void main(String[] args) {
		
		String input = "create random 100";
		
		String argumentAndValue = input.substring("create".length());
		
		
		System.out.println(argumentAndValue);
		
	}
}


