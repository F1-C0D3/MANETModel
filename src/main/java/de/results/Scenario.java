package de.results;

import java.nio.file.Path;
import java.nio.file.Paths;

import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.manetmodel.network.mobility.MobilityModel;
import de.manetmodel.network.radio.IRadioModel;
import de.manetmodel.network.unit.DataRate;

public class Scenario {

    private int flows;
    private int nodes;
    private String indivdualName;
    private DataRate meanTransmissionRate;

    public Scenario(String indivdualName, int flows, int nodes, DataRate meanTransmissionRate) {
	this.indivdualName = indivdualName;
	this.nodes = nodes;
	this.flows = flows;
	this.meanTransmissionRate = meanTransmissionRate;
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
	return new StringBuffer().append(indivdualName).append("_Flows_").append(flows).append("_")
		.append("MeanTransmissionRate").append("_").append(meanTransmissionRate.toString()).append("_Nodes_")
		.append(nodes).toString();
    }

}
