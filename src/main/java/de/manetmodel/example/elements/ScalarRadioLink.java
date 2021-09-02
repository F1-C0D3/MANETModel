package de.manetmodel.example.elements;

import de.manetmodel.network.Link;

public class ScalarRadioLink extends Link<ScalarLinkQuality> {

    private double receptionPower;

    public double getReceptionPower() {
	return receptionPower;
    }

    public void setReceptionPower(double receptionPower) {
	this.receptionPower = receptionPower;
    }

}
