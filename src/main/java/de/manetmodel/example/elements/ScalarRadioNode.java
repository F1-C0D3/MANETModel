package de.manetmodel.example.elements;

import de.manetmodel.network.Node;

public class ScalarRadioNode extends Node {

    private double transmissionPower;
    private double receptionThreshold;
    private double carrierFrequency;
    
    
    public double getTransmissionPower() {
        return transmissionPower;
    }
    
    public void setTransmissionPower(double transmissionPower) {
        this.transmissionPower = transmissionPower;
    }
    
    public double getReceptionThreshold() {
        return receptionThreshold;
    }
    
    public void setReceptionThreshold(double receptionThreshold) {
        this.receptionThreshold = receptionThreshold;
    }

    public double getCarrierFrequency() {
        return carrierFrequency;
    }

    public void setCarrierFrequency(double carrierFrequency) {
        this.carrierFrequency = carrierFrequency;
    }
    
    
    
}
