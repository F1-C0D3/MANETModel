package de.manetmodel.results;

import com.opencsv.bean.CsvBindByName;

import de.manetmodel.units.Time;


public class AverageResultParameter extends ResultParameter {

    @CsvBindByName(column = "overUtilization")
    private long overUtilization;

    @CsvBindByName(column = "utilization")
    private long utilization;

    @CsvBindByName(column = "activePathParticipants")
    private double activePathParticipants;

    @CsvBindByName(column = "connectionStability")
    private double connectionStability;

    @CsvBindByName(column = "simulationTime")
    private Time simulationTime;

    public AverageResultParameter() {
	overUtilization = 0;
    }

    public double isActivePathParticipants() {
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

    public double getConnectionStability() {
	return connectionStability;
    }

    public void setConnectionStability(double connectionStability) {
	this.connectionStability = connectionStability;
    }

}
