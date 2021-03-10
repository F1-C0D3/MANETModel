package de.results;

import com.opencsv.bean.CsvBindByName;

public class MANETRunResult extends RunResult {

    @CsvBindByName(column = "overUtilization")
    private double overUtilization;

    @CsvBindByName(column = "utilization")
    private double utilization;

    @CsvBindByName(column = "pathParticipant")
    private boolean isPathParticipant;

    public MANETRunResult() {
	overUtilization = 0d;
	isPathParticipant = false;
    }

    public boolean isPathParticipant() {
	return isPathParticipant;
    }

    public void setPathParticipant(boolean isPathParticipant) {
	this.isPathParticipant = isPathParticipant;
    }

    public void setOverUtilization(double oUtilization) {
	this.overUtilization = oUtilization;
    }

    public void setUtilization(double utilization) {
	this.utilization = utilization;
    }

    public double getUtilization() {
	return this.utilization;
    }

    public double getOverUtilization() {
	return this.overUtilization;
    }

}
