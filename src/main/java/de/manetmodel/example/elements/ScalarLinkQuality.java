package de.manetmodel.example.elements;

import de.manetmodel.network.LinkQuality;

public class ScalarLinkQuality extends LinkQuality {

    private double mobilityQuality;
    private double receptionConfidence;
    
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
}
