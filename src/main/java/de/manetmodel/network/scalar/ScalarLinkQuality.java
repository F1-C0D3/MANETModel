package de.manetmodel.network.scalar;

import de.manetmodel.network.LinkQuality;

public class ScalarLinkQuality extends LinkQuality {

    private double speedQuality;
    private double receptionConfidence;
    private double relativeMobility;
    
    public double getReceptionConfidence() {
        return receptionConfidence;
    }
    
    public void setReceptionConfidence(double receptionConfidence) {
        this.receptionConfidence = receptionConfidence;
    }
    
    public double getSpeedQuality() {
        return speedQuality;
    }

    public void setSpeedQuality(double speedQuality) {
        this.speedQuality = speedQuality;
    }

    public double getRelativeMobility() {
        return relativeMobility;
    }

    public void setRelativeMobility(double relativeMobility) {
        this.relativeMobility = relativeMobility;
    }
    
    

}
