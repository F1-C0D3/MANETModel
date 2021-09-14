package de.manetmodel.network.scalar;

import de.manetmodel.network.LinkQuality;

public class ScalarLinkQuality extends LinkQuality {

    private double mobilityQuality;
    private double receptionConfidence;
    private double receptionQuality;
    
    public double getReceptionConfidence() {
        return receptionConfidence;
    }
    
    public void setReceptionConfidence(double receptionConfidence) {
        this.receptionConfidence = receptionConfidence;
    }
    
    public double getMobilityQuality() {
	return this.mobilityQuality;
    }
    
    public void setMobilityQuality(double mobilityQuality) {
	this.mobilityQuality = mobilityQuality;
    }

    public double getReceptionQuality() {
        return receptionQuality;
    }

    public void setReceptionQuality(double receptionQuality) {
        this.receptionQuality = receptionQuality;
    }
    
    
}
