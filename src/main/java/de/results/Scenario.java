package de.results;

import java.nio.file.Path;
import java.nio.file.Paths;

import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.manetmodel.generator.MANETProperties;
import de.manetmodel.network.mobility.MobilityModel;
import de.manetmodel.network.radio.IRadioModel;

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

    public Scenario() {
	this.flows = -1;
	this.nodes = -1;
    }

    public Path getResultFolder() {
	return Paths.get(new StringBuffer().append(indivdualName).toString());
    }

    public String getResultFile() {
	return new StringBuffer().append(indivdualName).append("_Flows_").append(flows).append("_Nodes_").append(nodes)
		.toString();
    }

}
