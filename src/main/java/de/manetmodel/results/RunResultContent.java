package de.manetmodel.results;

import java.util.List;

import de.manetmodel.units.Time;

public class RunResultContent<I extends ResultParameter, A extends ResultParameter> {

    private int currentRun;
    private List<I> individualResultContent;
    private Time runDuration;
    private A averageResultContent;
    
    private boolean doRecord;

    public RunResultContent(int run) {
	this.currentRun = run;
    }

    public int getCurrentRun() {
	return currentRun;
    }

    public void setCurrentRun(int currentRun) {
	this.currentRun = currentRun;
    }

    public List<I> getIndividualResultContent() {
	return individualResultContent;
    }

    public void setIndividualResultContent(List<I> individualResultContent) {
	this.individualResultContent = individualResultContent;
    }

    public Time getRunDuration() {
	return runDuration;
    }

    public void setRunDuration(Time runDuration) {
	this.runDuration = runDuration;
    }

    public A getAverageResultContent() {
	return averageResultContent;
    }

    public void setAverageResultContent(A averageResultContent) {
	this.averageResultContent = averageResultContent;
    }

    

    public boolean doRecord() {
        return doRecord;
    }

    public void recordRun() {
        this.doRecord =true;
    }
    
    public void undoRecord() {
        this.doRecord =false;
    }
}
