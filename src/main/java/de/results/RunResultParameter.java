package de.results;

import com.opencsv.bean.CsvBindByName;

public class RunResultParameter extends ResultParameter {

    @CsvBindByName(column = "lId")
    int lId;

    @CsvBindByName(column = "n1Id")
    int n1Id;

    @CsvBindByName(column = "n2Id")
    int n2Id;

    @CsvBindByName(column = "overUtilization")
    private long overUtilization;

    @CsvBindByName(column = "utilization")
    private long utilization;

    @CsvBindByName(column = "isPathParticipant")
    private boolean isPathParticipant;

    @CsvBindByName(column = "connectionStability")
    private int connectionStability;

    public void setIndividualRunResultParameter(int lId, int n1Id, int n2Id) {
	this.lId = lId;
	this.n1Id = n1Id;
	this.n2Id = n2Id;
    }

    public RunResultParameter() {
	overUtilization = 0;
	isPathParticipant = false;
    }

    public int getlId() {
	return lId;
    }

    public void setlId(int lId) {
	this.lId = lId;
    }

    public int getN1Id() {
	return n1Id;
    }

    public void setN1Id(int n1Id) {
	this.n1Id = n1Id;
    }

    public int getN2Id() {
	return n2Id;
    }

    public void setN2Id(int n2Id) {
	this.n2Id = n2Id;
    }

    public boolean isPathParticipant() {
	return isPathParticipant;
    }

    public void setPathParticipant(boolean isPathParticipant) {
	this.isPathParticipant = isPathParticipant;
    }

    public void setOverUtilization(long oUtilization) {
	this.overUtilization = oUtilization;
    }

    public void setUtilization(long utilization) {
	this.utilization = utilization;
    }

    public long getUtilization() {
	return this.utilization;
    }

    public long getOverUtilization() {
	return this.overUtilization;
    }

    public int getConnectionStability() {
	return connectionStability;
    }

    public void setConnectionStability(int connectionStability) {
	this.connectionStability = connectionStability;
    }

}
