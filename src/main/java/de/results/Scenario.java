package de.results;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Scenario {

    private int flows;
    private int nodes;
    private String indivdualName;

    public Scenario(String indivdualName, int flows, int nodes) {
	this.indivdualName = indivdualName;
	this.nodes = nodes;
	this.flows = flows;
    }

    public Scenario(int flows, int nodes) {
	this.indivdualName = new String();
	this.nodes = nodes;
	this.flows = flows;
    }

    public Path getResultFolder() {
	return Paths.get(new StringBuffer().append(indivdualName).toString());
    }

    public String getResultFile() {
	return new StringBuffer().append(indivdualName).append("_Flows_").append(flows).append("_Nodes_").append(nodes)
		.toString();
    }
}
