package de.manetgraph.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Consumer;

public class ConsoleApp
{
	static ArrayList<Option<String>> options = new ArrayList<Option<String>>();
	
	private static class Option<T>{
		private String flag;
		private String command;
		private String info;
		private Consumer<T> function;
		
		public Option(String flag, String command, String info, Consumer<T> function) {
			this.flag = flag;
			this.command = command;
			this.info = info;
			this.function = function;
		}	
		
		public String getFlag() {
			return this.flag;
		}
		
		public String getCommand() {
			return this.command;
		}
		
		public String getInfo() {
			return this.info;
		}
		
		public Consumer<T> getFuntion(){
			return this.function;
		}		
	}

    public static void main (String[] args) throws IOException  
    {	
    	Scanner scanner = new Scanner(System.in); 	
	
    	System.out.println("ConselApp");
   	
    	options.add(new Option<String>("", "test", "blablabla", ConsoleApp::test));	
    	options.add(new Option<String>("-", "help", "prints list of available commands", ConsoleApp::help));
	
    	String input = "";
    	
    	do {	
    		boolean validInput = false;
    		input = scanner.nextLine();		
    		
    		for(Option<String> option : options) {
    			if(input.startsWith(String.format("%s%s", option.flag, option.command))) {
    				option.getFuntion().accept("matched");
    				validInput = true;
    			}
    		}
    		
    		if(!validInput)
    			System.out.println(String.format("\"%s\" is not a valid command, type -h for available commands", input));			
    	}
    	while(!input.contains("exit"));   	   
    }
        
    private static void test(String test) {
    	System.out.println(test);
    }
    
    private static void help(String test) {
    	for(Option<String> option : options) {
    		System.out.println(String.format("%s%s : %s", option.getFlag(), option.getCommand(), option.getInfo()));	
    	}
    }

}