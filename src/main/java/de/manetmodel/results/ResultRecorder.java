package de.manetmodel.results;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.manetmodel.scenarios.Scenario;

public class ResultRecorder {
    private Date date;
    private SimpleDateFormat dateFormatter;
    
    protected Scenario scenario;
    protected List<Path> directoryStructure;

    public ResultRecorder(Scenario scenario) {
	this.scenario = scenario;
	directoryStructure = new ArrayList<Path>();
	directoryStructure.add(Paths.get(scenario.getScenarioName()));

	if (scenario.getDatePrefixFlag() == 1) {
	    date = new Date();
	    dateFormatter = new SimpleDateFormat("mm:HH:MM:yyyy");
	    directoryStructure.add(Paths.get(String.format("ResultRecord_%s", formateDate())));
	}
	else{

	    directoryStructure.add(Paths.get("ResultRecord_NoSystemTime"));
	}

    }

    private String formateDate() {
	return dateFormatter.format(date);
    }
    
    public Date getDate() {
	return this.date;
    }
    
    public void setDate(Date date) {
	this.date = date;
    }

    protected String outputFilename() {

	StringBuffer filenameBuffer = new StringBuffer();
	
	if (date != null) 
	    filenameBuffer.append(String.format("RR=%s_", formateDate()));
	
	filenameBuffer.append(String.format("%s", scenario.getScenarioName()))	;
	
	return filenameBuffer.toString();
	
    }
}
