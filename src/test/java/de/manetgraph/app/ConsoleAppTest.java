package de.manetgraph.app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class ConsoleAppTest {
	
	public static class TreeNode {

	    final String name;
	    final List<TreeNode> children;

	    public TreeNode(String name, List<TreeNode> children) {
	        this.name = name;
	        this.children = children;
	    }

	    public String toString() {
	        StringBuilder buffer = new StringBuilder(50);
	        print(buffer, "", "");
	        return buffer.toString();
	    }

	    private void print(StringBuilder buffer, String prefix, String childrenPrefix) {
	        buffer.append(prefix);
	        buffer.append(name);
	        buffer.append('\n');
	        for (Iterator<TreeNode> it = children.iterator(); it.hasNext();) {
	            TreeNode next = it.next();
	            if (it.hasNext()) {
	                next.print(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
	            } else {
	                next.print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
	            }
	        }
	    }
	}
		
	public static void main(String[] args) {
		
		
		List<TreeNode> children = new ArrayList<TreeNode>();
		children.add(new TreeNode("b", new ArrayList<TreeNode>()));
		children.add(new TreeNode("c", new ArrayList<TreeNode>()));
		
		TreeNode root = new TreeNode("a", children);
		
		System.out.println(root.toString());
		
	}
}


