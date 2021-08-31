package de.example.elements;

import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.unit.DataRate;

public class ScalarRadioLink extends Link<LinkQuality> {

    private double receptionPower;

    public double getReceptionPower() {
	return receptionPower;
    }

    public void setReceptionPower(double receptionPower) {
	this.receptionPower = receptionPower;
    }

}
