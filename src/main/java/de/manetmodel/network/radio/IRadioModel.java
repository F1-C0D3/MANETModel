package de.manetmodel.network.radio;

public interface IRadioModel {

    long transmissionBitrate(double distance);

    double receptionPower(double distance);

    boolean interferencePresent(double distance);
}
