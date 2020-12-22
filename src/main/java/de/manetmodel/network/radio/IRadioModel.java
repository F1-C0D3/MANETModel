package de.manetmodel.network.radio;

import de.manetmodel.network.unit.DataRate;

public interface IRadioModel {

    DataRate transmissionBitrate(double distance);

    double receptionPower(double distance);

    boolean interferencePresent(double distance);
}
