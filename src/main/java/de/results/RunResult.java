package de.results;

import com.opencsv.bean.CsvBindByName;

public class RunResult {

    @CsvBindByName(column = "lId")
    int lId;

    public void setId(int lId) {
	this.lId = lId;
    }

}
