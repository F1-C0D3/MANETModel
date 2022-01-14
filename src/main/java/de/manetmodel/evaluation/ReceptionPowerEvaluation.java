package de.manetmodel.evaluation;

import de.manetmodel.evaluator.DoubleScope;
import de.manetmodel.evaluator.LinearStandardization;
import de.manetmodel.network.scalar.ScalarRadioLink;
import de.manetmodel.network.scalar.ScalarRadioNode;
import de.manetmodel.units.dBm;

public class ReceptionPowerEvaluation extends LinearStandardization {

    public ReceptionPowerEvaluation(DoubleScope scoreScope, double weight) {
	super(scoreScope, weight);

	this.setPropertyScope(new DoubleScope(0d, 100d));
    }

    public double compute(ScalarRadioNode source, ScalarRadioLink link, ScalarRadioNode sink) {

	dBm receptionPower = link.getReceptionPower().todBm();
	
	dBm receptionThreshold = sink.getReceptionThreshold().todBm();
	
	double receptionQuality = receptionPower.get()/receptionThreshold.get();
	
	return this.getScore(receptionQuality);
    }
}
