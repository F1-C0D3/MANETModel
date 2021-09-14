package de.manetmodel.evaluator;

import de.manetmodel.network.scalar.ScalarRadioLink;
import de.manetmodel.network.scalar.ScalarRadioModel;
import de.manetmodel.network.scalar.ScalarRadioNode;
import de.manetmodel.units.Watt;
import de.manetmodel.units.dBm;

public class ReceptionPowerEvaluator extends PropertyStandardization {

    public ReceptionPowerEvaluator(DoubleScope scoreScope, double weight) {
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
