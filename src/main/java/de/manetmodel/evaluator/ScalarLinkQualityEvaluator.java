package de.manetmodel.evaluator;

import de.manetmodel.evaluation.ConfidenceRangeEvaluation;
import de.manetmodel.evaluation.SourceSinkSingleTickMobilityEvaluation;
import de.manetmodel.mobilitymodel.MobilityModel;
import de.manetmodel.network.scalar.ScalarLinkQuality;
import de.manetmodel.network.scalar.ScalarRadioLink;
import de.manetmodel.network.scalar.ScalarRadioModel;
import de.manetmodel.network.scalar.ScalarRadioNode;

public class ScalarLinkQualityEvaluator
	extends LinkQualityEvaluator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> {

    private SourceSinkSingleTickMobilityEvaluation<ScalarRadioNode> mobilityEvaluator;
    private ConfidenceRangeEvaluation confidenceRangeEvaluator;

    ScoreOrder scoreOrder = ScoreOrder.ASCENDING;

    public ScalarLinkQualityEvaluator(DoubleScope scoreScope, ScalarRadioModel radioModel,
	    MobilityModel mobilityModel) {

	super(scoreScope);
	this.mobilityEvaluator = new SourceSinkSingleTickMobilityEvaluation<ScalarRadioNode>(
		/* scoreScope */ new DoubleScope(0d, 1d), /* weight */ 1, /* mobilityModel */ mobilityModel);
	this.confidenceRangeEvaluator = new ConfidenceRangeEvaluation(/* scoreScope */ new DoubleScope(0d, 1d),
		/* weight */ 1, radioModel);

	this.setPropertyScope(new DoubleScope(0d,
		mobilityEvaluator.getScoreScope().max + confidenceRangeEvaluator.getScoreScope().max));
    }

    @Override
    public boolean compute(ScalarRadioNode source, ScalarRadioLink link, ScalarRadioNode sink) {

	ScalarLinkQuality scalarLinkQuality = link.getWeight();

	scalarLinkQuality.setReceptionConfidence(confidenceRangeEvaluator.compute(source, link, sink));

	scalarLinkQuality.setSpeedQuality(mobilityEvaluator.compute(source, sink));

	if (scoreOrder == ScoreOrder.ASCENDING)
	    scalarLinkQuality.setScore(
		    getScore(scalarLinkQuality.getSpeedQuality() + scalarLinkQuality.getReceptionConfidence()));
	else
	    scalarLinkQuality.setScore(this.getScoreScope().max
		    - getScore(scalarLinkQuality.getSpeedQuality() + scalarLinkQuality.getReceptionConfidence()));

	return true;
    }
}
