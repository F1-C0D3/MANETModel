package de.manetmodel.network.scalar;

import de.manetmodel.network.Node;
import de.manetmodel.units.Watt;
import de.manetmodel.units.dBm;

public class ScalarRadioNode extends Node {

    private Watt transmissionPower;
    private Watt receptionThreshold;
    private double carrierFrequency;
    
    
    public Watt getTransmissionPower() {
        return transmissionPower;
    }
    
    public void setTransmissionPower(Watt transmissionPower) {
        this.transmissionPower = transmissionPower;
    }
    
    public Watt getReceptionThreshold() {
        return receptionThreshold;
    }
    
    public void setReceptionThreshold(Watt receptionThreshold) {
        this.receptionThreshold = receptionThreshold;
    }

    public double getCarrierFrequency() {
        return carrierFrequency;
    }

    public void setCarrierFrequency(double carrierFrequency) {
        this.carrierFrequency = carrierFrequency;
    }
      
}
