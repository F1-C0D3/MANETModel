package de.manetmodel.results;

import com.opencsv.bean.CsvBindByName;

import de.manetmodel.units.Time;


public class AverageRunResultParameter extends ResultParameter {

    @CsvBindByName(column = "overUtilization")
    private long overUtilization;

    @CsvBindByName(column = "utilization")
    private long utilization;

    @CsvBindByName(column = "activePathParticipants")
    private double activePathParticipants;

    @CsvBindByName(column = "meanConnectionStability")
    private Time meanConnectionStability;
    
    @CsvBindByName(column = "minConnectionStability")
    private Time minConnectionStability;
    
    @CsvBindByName(column = "maxConnectionStability")
    private Time maxConnectionStability;
    
    @CsvBindByName(column = "numberOfUndeployedFlows")
    private double numberOfUndeployedFlows;

    @CsvBindByName(column = "simulationTime")
    private Time simulationTime;
    
    @CsvBindByName(column = "runNumber")
    private int runNumber;

    public AverageRunResultParameter() {
	overUtilization = 0;
    }

    public double getActivePathParticipants() {
	return activePathParticipants;
    }

    public void setActivePathParticipants(double activePathParticipants) {
	this.activePathParticipants = activePathParticipants;
    }

    public Time getSimulationTime() {
	return simulationTime;
    }

    public void setSimulationTime(Time simulationTime) {
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

    public Time getMaxConnectionStability() {
        return maxConnectionStability;
    }

    public void setMaxConnectionStability(Time maxConnectionStability) {
        this.maxConnectionStability = maxConnectionStability;
    }

    public Time getMinConnectionStability() {
        return minConnectionStability;
    }

    public void setMinConnectionStability(Time minConnectionStability) {
        this.minConnectionStability = minConnectionStability;
    }

    public Time getMeanConnectionStability() {
	return meanConnectionStability;
    }

    public void setMeanConnectionStability(Time connectionStability) {
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
