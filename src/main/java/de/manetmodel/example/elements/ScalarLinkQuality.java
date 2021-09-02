package de.manetmodel.example.elements;

import de.manetmodel.network.LinkQuality;

public class ScalarLinkQuality extends LinkQuality {

    private double mobilityQuality;
    private double receptionConfidence;
    
    
    public double getSinkSpeed() {
        return mobilityQuality;
    }
    public void setSinkSpeed(double sinkSpeed) {
        this.mobilityQuality = sinkSpeed;
    }
    public double getReceptionConfidence() {
        return receptionConfidence;
    }
    public void setReceptionConfidence(double receptionConfidence) {
        this.receptionConfidence = receptionConfidence;
    }
}
