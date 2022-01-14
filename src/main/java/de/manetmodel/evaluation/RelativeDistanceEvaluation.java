package de.manetmodel.evaluation;

import de.manetmodel.evaluator.DoubleScope;
import de.manetmodel.evaluator.LinearStandardization;
import de.manetmodel.network.ideal.IdealRadioModel;
import de.manetmodel.network.scalar.ScalarRadioLink;
import de.manetmodel.network.scalar.ScalarRadioModel;
import de.manetmodel.network.scalar.ScalarRadioNode;
import de.manetmodel.units.Watt;
import de.manetmodel.units.dBm;

public class RelativeDistanceEvaluation extends LinearStandardization {

    private IdealRadioModel radioModel;

    public RelativeDistanceEvaluation(DoubleScope scoreScope, double weight, IdealRadioModel radioModel) {
	super(scoreScope, weight);
	this.radioModel = radioModel;
	this.setPropertyScope(new DoubleScope(0d, 100d));
    }

    public double compute(ScalarRadioNode source, ScalarRadioLink link, ScalarRadioNode sink) {

	double relativeValue = 0d;

	double maxTransmissionDistance = radioModel.getLinkMaxTransmissionRange();
	double minTransmissionDistance = radioModel.getLinkMinConnumicationRange();

	relativeValue = ((link.getWeight().getDistance() - minTransmissionDistance)
		/ (maxTransmissionDistance - minTransmissionDistance));
	relativeValue = 1d - relativeValue;

	return relativeValue;
    }
}
