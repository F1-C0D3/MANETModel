package de.manetmodel.results;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultRecorder {
    protected Date date;
    private SimpleDateFormat dateFormatter;

    public ResultRecorder() {
	date = new Date();
	dateFormatter = new SimpleDateFormat("mm:HH:MM:yyyy");
	
    }
    
    protected String getDate() {
	return dateFormatter.format(date); 
    }
}
