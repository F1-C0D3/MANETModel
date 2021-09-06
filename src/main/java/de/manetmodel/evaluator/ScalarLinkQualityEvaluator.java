package de.manetmodel.evaluator;

import de.manetmodel.network.scalar.ScalarLinkQuality;
import de.manetmodel.network.scalar.ScalarRadioLink;
import de.manetmodel.network.scalar.ScalarRadioModel;
import de.manetmodel.network.scalar.ScalarRadioNode;

public class ScalarLinkQualityEvaluator
	extends LinkQualityEvaluator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> {

    //private Function<Quadruple<Watt, Watt, Watt, Double>, Double> computeConfidenceRange;
    private ScalarRadioModel radioModel;

    private MobilityEvaluator<ScalarRadioNode> mobilityEvaluator;
    private ConfidenceRangeEvaluator confidenceRangeEvaluator;

    public ScalarLinkQualityEvaluator(DoubleScope scoreScope, ScalarRadioModel radioModel) {

	super(scoreScope);

	this.radioModel = radioModel;
	
	this.mobilityEvaluator = new MobilityEvaluator<ScalarRadioNode>(
		/* scoreScope */ new DoubleScope(0d, 1d),
		/* weight */ 1);

	this.confidenceRangeEvaluator = new ConfidenceRangeEvaluator(
		/* scoreScope */ new DoubleScope(0d, 1d), 
		/* weight */ 1);
	
	this.setPropertyScope(new DoubleScope(0d, mobilityEvaluator.getScoreScope().max + confidenceRangeEvaluator.getScoreScope().max));
    }

    @Override
    public boolean compute(ScalarRadioNode source, ScalarRadioLink link, ScalarRadioNode sink) {

	ScalarLinkQuality scalarLinkQuality = link.getWeight();

	scalarLinkQuality.setReceptionConfidence(confidenceRangeEvaluator.compute(source, link, sink, radioModel));
	
	scalarLinkQuality.setMobilityQuality(mobilityEvaluator.compute(source, sink));

	scalarLinkQuality.setScore(getScore(scalarLinkQuality.getMobilityQuality() + scalarLinkQuality.getReceptionConfidence()));

	return true;
    }

}
