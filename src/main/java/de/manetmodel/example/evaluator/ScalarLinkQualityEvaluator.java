package de.manetmodel.example.evaluator;

import de.manetmodel.evaluator.DoubleScope;
import de.manetmodel.example.elements.ScalarLinkQuality;
import de.manetmodel.example.elements.ScalarRadioLink;
import de.manetmodel.example.elements.ScalarRadioNode;
import de.manetmodel.example.radio.ScalarRadioModel;

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
