package de.results;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class RunResult {

    @CsvBindByName(column = "lId")
    int lId;

    @CsvBindByName(column = "n1Id")
    int n1Id;
    
    @CsvBindByName(column = "n2Id")
    int n2Id;

    public void setId(int lId, int n1Id, int n2Id) {
	this.lId = lId;
	this.n1Id = n1Id;
	this.n2Id = n2Id;
    }

    public int getlId() {
	return lId;
    }

    public void setlId(int lId) {
	this.lId = lId;
    }

    public int getN1Id() {
	return n1Id;
    }

    public void setN1Id(int n1Id) {
	this.n1Id = n1Id;
    }

    public int getN2Id() {
	return n2Id;
    }

    public void setN2Id(int n2Id) {
	this.n2Id = n2Id;
    }

}
