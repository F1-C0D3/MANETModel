package de.manetmodel.network.radio;

public interface IRadioModel {

    long computeTransmissionBitrate(double distance);

    double computeReception(double distance);

    boolean interferencePresent(double distance);
}
