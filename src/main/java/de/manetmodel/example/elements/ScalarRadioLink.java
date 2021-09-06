package de.manetmodel.example.elements;

import de.manetmodel.network.Link;
import de.manetmodel.network.unit.Watt;

public class ScalarRadioLink extends Link<ScalarLinkQuality> {

    private Watt receptionPower;

    public Watt getReceptionPower() {
	return receptionPower;
    }

    public void setReceptionPower(Watt receptionPower) {
	this.receptionPower = receptionPower;
    }

}
