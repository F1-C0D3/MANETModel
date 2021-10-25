package de.manetmodel.results;

import com.opencsv.bean.CsvBindByName;

import de.manetmodel.units.Time;


public class TotalResultParameter extends ResultParameter {

    @CsvBindByName(column = "meanOverUtilization")
    private double meanOverUtilization;

    @CsvBindByName(column = "meanUtilization")
    private double meanUtilization;

    @CsvBindByName(column = "activePathParticipants")
    private double activePathParticipants;

    @CsvBindByName(column = "meanAverageConnectionStability")
    private Time meanAverageConnectionStability;
    
    @CsvBindByName(column = "minAverageConnectionStability")
    private Time minAverageConnectionStability;
    
    @CsvBindByName(column = "maxAverageConnectionStability")
    private Time maxAverageConnectionStability;
    
    @CsvBindByName(column = "meanNumberOfUndeployedFlows")
    private double meanNumberOfUndeployedFlows;

    @CsvBindByName(column = "meanAveragesimulationTime")
    private Time meanAveragesimulationTime;
    

    @CsvBindByName(column = "minAveragesimulationTime")
    private Time minAveragesimulationTime;

    @CsvBindByName(column = "maxAveragesimulationTime")
    private Time maxAveragesimulationTime;
    
    @CsvBindByName(column = "finishedRuns")
    private int finishedRuns;
    

    public TotalResultParameter() {
	meanOverUtilization = 0;
    }

    public double isActivePathParticipants() {
	return activePathParticipants;
    }

    public void setActivePathParticipants(double activePathParticipants) {
	this.activePathParticipants = activePathParticipants;
    }

    public Time getMeanAverageSimulationTime() {
	return meanAveragesimulationTime;
    }

    public void setMeanAverageSimulationTime(Time simulationTime) {
	this.meanAveragesimulationTime = simulationTime;
    }

    public void setAverageOverUtilization(double oUtilization) {
	this.meanOverUtilization = oUtilization;
    }

    public void setAverageUtilization(double utilization) {
	this.meanUtilization = utilization;
    }

    public double getUtilization() {
	return this.meanUtilization;
    }

    public double getOverUtilization() {
	return this.meanOverUtilization;
    }

    public Time getMeanAverageConnectionStability() {
	return meanAverageConnectionStability;
    }

    public void setMeanAverageConnectionStability(Time connectionStability) {
	this.meanAverageConnectionStability = connectionStability;
    }

        
    public Time getMinAveragesimulationTime() {
        return minAveragesimulationTime;
    }

    public void setMinAveragesimulationTime(Time minAveragesimulationTime) {
        this.minAveragesimulationTime = minAveragesimulationTime;
    }

    public Time getMaxAveragesimulationTime() {
        return maxAveragesimulationTime;
    }

    public void setMaxAveragesimulationTime(Time maxAveragesimulationTime) {
        this.maxAveragesimulationTime = maxAveragesimulationTime;
    }

    public Time getMinAverageConnectionStability() {
        return minAverageConnectionStability;
    }

    public void setMinAverageConnectionStability(Time minAverageConnectionStability) {
        this.minAverageConnectionStability = minAverageConnectionStability;
    }
    

    public Time getMaxAverageConnectionStability() {
        return maxAverageConnectionStability;
    }

    public void setMaxAverageConnectionStability(Time maxAverageConnectionStability) {
        this.maxAverageConnectionStability = maxAverageConnectionStability;
    }

    public double getNumberOfUndeployedFlows() {
        return meanNumberOfUndeployedFlows;
    }

    public void setNumberOfUndeployedFlows(double numberOfUndeployedFlows) {
        this.meanNumberOfUndeployedFlows = numberOfUndeployedFlows;
    }

    public int getFinishedRuns() {
        return finishedRuns;
    }

    public void setFinishedRuns(int finishedRuns) {
        this.finishedRuns = finishedRuns;
    }
    
    

    
}
