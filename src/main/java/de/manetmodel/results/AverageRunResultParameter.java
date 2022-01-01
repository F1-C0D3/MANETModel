package de.manetmodel.results;

import com.opencsv.bean.CsvBindByName;

import de.manetmodel.units.Time;


public class AverageRunResultParameter extends ResultParameter {

    
    @CsvBindByName(column = "linkQuality")
    private double linkQuality;
    
    @CsvBindByName(column = "overUtilization")
    private long overUtilization;

    @CsvBindByName(column = "utilization")
    private long utilization;

    @CsvBindByName(column = "activePathParticipants")
    private double activePathParticipants;

    @CsvBindByName(column = "meanConnectionStability")
    private double meanConnectionStability;
    
    @CsvBindByName(column = "minConnectionStability")
    private double minConnectionStability;
    
    @CsvBindByName(column = "maxConnectionStability")
    private double maxConnectionStability;
    
    @CsvBindByName(column = "numberOfUndeployedFlows")
    private double numberOfUndeployedFlows;

    @CsvBindByName(column = "simulationTime")
    private double simulationTime;
    
    @CsvBindByName(column = "runNumber")
    private int runNumber;
    
    
    public double getLinkQuality() {
        return linkQuality;
    }

    public void setLinkQuality(double linkQuality) {
        this.linkQuality = linkQuality;
    }

    public AverageRunResultParameter() {
	overUtilization = 0;
    }

    public double getActivePathParticipants() {
	return activePathParticipants;
    }

    public void setActivePathParticipants(double activePathParticipants) {
	this.activePathParticipants = activePathParticipants;
    }

    public double getSimulationTime() {
	return simulationTime;
    }

    public void setSimulationTime(double simulationTime) {
	this.simulationTime = simulationTime;
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

    public double getMaxConnectionStability() {
        return maxConnectionStability;
    }

    public void setMaxConnectionStability(double maxConnectionStability) {
        this.maxConnectionStability = maxConnectionStability;
    }

    public double getMinConnectionStability() {
        return minConnectionStability;
    }

    public void setMinConnectionStability(double minConnectionStability) {
        this.minConnectionStability = minConnectionStability;
    }

    public double getMeanConnectionStability() {
	return meanConnectionStability;
    }

    public void setMeanConnectionStability(double connectionStability) {
	this.meanConnectionStability = connectionStability;
    }

    public double getNumberOfUndeployedFlows() {
        return numberOfUndeployedFlows;
    }

    public void setNumberOfUndeployedFlows(double numberOfUndeployedFlows) {
        this.numberOfUndeployedFlows = numberOfUndeployedFlows;
    }

    public int getRunNumber() {
        return runNumber;
    }

    public void setRunNumber(int runNumber) {
        this.runNumber = runNumber;
    }
    
    

    
}
