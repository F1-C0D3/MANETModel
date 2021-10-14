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
    
    @CsvBindByName(column = "meanNumberOfUndeployedFlows")
    private double meanNumberOfUndeployedFlows;

    @CsvBindByName(column = "averagesimulationTime")
    private Time averagesimulationTime;

    public TotalResultParameter() {
	meanOverUtilization = 0;
    }

    public double isActivePathParticipants() {
	return activePathParticipants;
    }

    public void setActivePathParticipants(double activePathParticipants) {
	this.activePathParticipants = activePathParticipants;
    }

    public Time getAverageSimulationTime() {
	return averagesimulationTime;
    }

    public void setAverageSimulationTime(Time simulationTime) {
	this.averagesimulationTime = simulationTime;
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

    
    public Time getMinAverageConnectionStability() {
        return minAverageConnectionStability;
    }

    public void setMinAverageConnectionStability(Time minAverageConnectionStability) {
        this.minAverageConnectionStability = minAverageConnectionStability;
    }

    public double getNumberOfUndeployedFlows() {
        return meanNumberOfUndeployedFlows;
    }

    public void setNumberOfUndeployedFlows(double numberOfUndeployedFlows) {
        this.meanNumberOfUndeployedFlows = numberOfUndeployedFlows;
    }

    
}
