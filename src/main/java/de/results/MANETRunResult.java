package de.results;

import com.opencsv.bean.CsvBindByName;

public class MANETRunResult extends RunResult {

    @CsvBindByName(column = "overUtilization")
    private double overUtilization;

    @CsvBindByName(column = "utilization")
    private double utilization;

    public MANETRunResult() {
	overUtilization = 0d;
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
