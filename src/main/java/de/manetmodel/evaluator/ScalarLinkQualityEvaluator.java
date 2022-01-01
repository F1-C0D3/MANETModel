package de.manetmodel.evaluator;

import de.manetmodel.mobilitymodel.MobilityModel;
import de.manetmodel.network.scalar.ScalarLinkQuality;
import de.manetmodel.network.scalar.ScalarRadioLink;
import de.manetmodel.network.scalar.ScalarRadioModel;
import de.manetmodel.network.scalar.ScalarRadioNode;

public class ScalarLinkQualityEvaluator
	extends LinkQualityEvaluator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> {

    private SourceSinkSingleTickMobilityEvaluator<ScalarRadioNode> mobilityEvaluator;
    private ConfidenceRangeEvaluator confidenceRangeEvaluator;
    ScoreOrder scoreOrder;

    public ScalarLinkQualityEvaluator(DoubleScope scoreScope, ScoreOrder scoreOrder, ScalarRadioModel radioModel, MobilityModel mobilityModel) {	
	
	super(scoreScope);
	
	this.scoreOrder = scoreOrder;
	
	this.mobilityEvaluator = new SourceSinkSingleTickMobilityEvaluator<ScalarRadioNode>(
		/* scoreScope */ new DoubleScope(0d, 1d), 
		/* weight */ 1, 
		/* mobilityModel */ mobilityModel);

	this.confidenceRangeEvaluator = new ConfidenceRangeEvaluator(
		/* scoreScope */ new DoubleScope(0d, 1d),
		/* weight */ 1, radioModel);
	
	this.setPropertyScope(
		new DoubleScope(
			0d,
			mobilityEvaluator.getScoreScope().max + confidenceRangeEvaluator.getScoreScope().max));	
    }
    
    public ScalarLinkQualityEvaluator(DoubleScope scoreScope, ScalarRadioModel radioModel, MobilityModel mobilityModel) {

	super(scoreScope);

	scoreOrder = ScoreOrder.ASCENDING;
	
	this.mobilityEvaluator = new SourceSinkSingleTickMobilityEvaluator<ScalarRadioNode>(
		/* scoreScope */ new DoubleScope(0d, 1d), 
		/* weight */ 1, 
		/* mobilityModel */ mobilityModel);

	this.confidenceRangeEvaluator = new ConfidenceRangeEvaluator(
		/* scoreScope */ new DoubleScope(0d, 1d),
		/* weight */ 1, radioModel);
	
	this.setPropertyScope(
		new DoubleScope(
			0d,
			mobilityEvaluator.getScoreScope().max + confidenceRangeEvaluator.getScoreScope().max));
    }
 
    @Override
    public boolean compute(ScalarRadioNode source, ScalarRadioLink link, ScalarRadioNode sink) {
	
	ScalarLinkQuality scalarLinkQuality = link.getWeight();
	
	scalarLinkQuality.setReceptionConfidence(confidenceRangeEvaluator.compute(source, link, sink));
	
	scalarLinkQuality.setSpeedQuality(mobilityEvaluator.compute(source, sink));
	
	if(scoreOrder == ScoreOrder.ASCENDING)
	    scalarLinkQuality.setScore(getScore(scalarLinkQuality.getSpeedQuality() + scalarLinkQuality.getReceptionConfidence()));
	else
	    scalarLinkQuality.setScore(this.getScoreScope().max - getScore(scalarLinkQuality.getSpeedQuality() + scalarLinkQuality.getReceptionConfidence()));

	return true;
    }   
}
