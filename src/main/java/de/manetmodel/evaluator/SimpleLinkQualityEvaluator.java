package de.manetmodel.evaluator;

import de.manetmodel.mobilitymodel.MobilityModel;
import de.manetmodel.network.scalar.ScalarLinkQuality;
import de.manetmodel.network.scalar.ScalarRadioLink;
import de.manetmodel.network.scalar.ScalarRadioModel;
import de.manetmodel.network.scalar.ScalarRadioNode;

public class SimpleLinkQualityEvaluator
	extends LinkQualityEvaluator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> {

    private ScalarRadioModel radioModel;

    private SinkSingleTickMobilityEvaluator<ScalarRadioNode> sinkMobilityProperty;
    private ConfidenceDistanceEvaluator confidenceDistanceProperty;

    public SimpleLinkQualityEvaluator(DoubleScope scoreScope, ScalarRadioModel radioModel,
	    MobilityModel mobilityModel) {

	super(scoreScope);

	this.radioModel = radioModel;

	this.sinkMobilityProperty = new SinkSingleTickMobilityEvaluator<ScalarRadioNode>(
		/* scoreScope */ new DoubleScope(0d, 1d), /* weight */ 1, /* mobilityModel */ mobilityModel);

	this.confidenceDistanceProperty = new ConfidenceDistanceEvaluator(
		/* scoreScope */ new DoubleScope(0d, 1d), /* weight */ 1);

	this.setPropertyScope(new DoubleScope(0d, 1d));
    }

    @Override
    public boolean compute(ScalarRadioNode source, ScalarRadioLink link, ScalarRadioNode sink) {
	ScalarLinkQuality scalarLinkQuality = link.getWeight();
	scalarLinkQuality.setReceptionConfidence(confidenceDistanceProperty.compute(source, link, sink, radioModel));
	scalarLinkQuality.setMobilityQuality(sinkMobilityProperty.compute(source, sink));
	scalarLinkQuality.setScore(
		getScore(scalarLinkQuality.getMobilityQuality() + scalarLinkQuality.getReceptionConfidence()));
	return true;
    }

}
