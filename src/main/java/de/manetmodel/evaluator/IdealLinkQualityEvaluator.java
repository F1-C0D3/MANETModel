package de.manetmodel.evaluator;

import de.manetmodel.evaluation.RelativeDistanceEvaluation;
import de.manetmodel.network.ideal.IdealLinkQuality;
import de.manetmodel.network.ideal.IdealRadioLink;
import de.manetmodel.network.ideal.IdealRadioModel;
import de.manetmodel.network.ideal.IdealRadioNode;
import de.manetmodel.network.scalar.ScalarLinkQuality;
import de.manetmodel.network.scalar.ScalarRadioLink;
import de.manetmodel.network.scalar.ScalarRadioNode;

public class IdealLinkQualityEvaluator
	extends LinkQualityEvaluator<IdealRadioNode, IdealRadioLink, IdealLinkQuality> {

    private RelativeDistanceEvaluation relativeDistanceEvaluationProperty;

    public IdealLinkQualityEvaluator(DoubleScope scoreScope, IdealRadioModel radioModel) {

	super(scoreScope);


	this.relativeDistanceEvaluationProperty = new RelativeDistanceEvaluation(/* scoreScope */ new DoubleScope(0d, 1d),
		/* weight */ 0.33d, radioModel);


	this.setPropertyScope(new DoubleScope(0d, 1d));
    }

    @Override
    public boolean compute(IdealRadioNode source, IdealRadioLink link, IdealRadioNode sink) {
	IdealLinkQuality linkQuality = link.getWeight();
	linkQuality
		.setScore(getScore(linkQuality.getRelativeDistance()));
	return true;
    }

}
