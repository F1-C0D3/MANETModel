package de.manetmodel.results;

import com.opencsv.bean.CsvBindByName;



public class TotalResultParameter extends ResultParameter {

    @CsvBindByName(column = "linkQuality")
    private double linkQuality;
    
    @CsvBindByName(column = "meanOverUtilization")
    private double meanOverUtilization;

    @CsvBindByName(column = "meanUtilization")
    private double meanUtilization;

    @CsvBindByName(column = "activePathParticipants")
    private double activePathParticipants;

    @CsvBindByName(column = "meanAverageConnectionStability")
    private double meanAverageConnectionStability;
    
    @CsvBindByName(column = "minAverageConnectionStability")
    private double minAverageConnectionStability;
    
    @CsvBindByName(column = "maxAverageConnectionStability")
    private double maxAverageConnectionStability;
    
    @CsvBindByName(column = "meanNumberOfUndeployedFlows")
    private double meanNumberOfUndeployedFlows;

    @CsvBindByName(column = "meanAveragesimulationTime")
    private double meanAveragesimulationTime;
    

    @CsvBindByName(column = "minAveragesimulationTime")
    private double minAveragesimulationTime;

    @CsvBindByName(column = "maxAveragesimulationTime")
    private double maxAveragesimulationTime;
    
    @CsvBindByName(column = "finishedRuns")
    private int finishedRuns;
    

    public TotalResultParameter() {
	meanOverUtilization = 0;
    }
    
    public double getLinkQuality() {
        return linkQuality;
    }

    public void setLinkQuality(double linkQuality) {
        this.linkQuality = linkQuality;
    }
    
    public double isActivePathParticipants() {
	return activePathParticipants;
    }

    public void setActivePathParticipants(double activePathParticipants) {
	this.activePathParticipants = activePathParticipants;
    }

    public double getMeanAverageSimulationTime() {
	return meanAveragesimulationTime;
    }

    public void setMeanAverageSimulationTime(double simulationTime) {
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

    public double getMeanAverageConnectionStability() {
	return meanAverageConnectionStability;
    }

    public void setMeanAverageConnectionStability(double connectionStability) {
	this.meanAverageConnectionStability = connectionStability;
    }

        
    public double getMinAveragesimulationTime() {
        return minAveragesimulationTime;
    }

    public void setMinAveragesimulationTime(double minAveragesimulationTime) {
        this.minAveragesimulationTime = minAveragesimulationTime;
    }

    public double getMaxAveragesimulationTime() {
        return maxAveragesimulationTime;
    }

    public void setMaxAveragesimulationTime(double maxAveragesimulationTime) {
        this.maxAveragesimulationTime = maxAveragesimulationTime;
    }

    public double getMinAverageConnectionStability() {
        return minAverageConnectionStability;
    }

    public void setMinAverageConnectionStability(double minAverageConnectionStability) {
        this.minAverageConnectionStability = minAverageConnectionStability;
    }
    

    public double getMaxAverageConnectionStability() {
        return maxAverageConnectionStability;
    }

    public void setMaxAverageConnectionStability(double maxAverageConnectionStability) {
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
