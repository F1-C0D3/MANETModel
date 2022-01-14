package de.manetmodel.network.ideal;

import de.manetmodel.network.LinkQuality;

public class IdealLinkQuality extends LinkQuality {

    private double relativeDistance;

    public double getRelativeDistance() {
	return relativeDistance;
    }

    public void setRelativeDistance(double relativeDistance) {
	this.relativeDistance = relativeDistance;
    }

}
