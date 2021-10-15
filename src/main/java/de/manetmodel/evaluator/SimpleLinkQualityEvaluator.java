package de.manetmodel.evaluator;

import de.manetmodel.mobilitymodel.MobilityModel;
import de.manetmodel.network.scalar.ScalarLinkQuality;
import de.manetmodel.network.scalar.ScalarRadioLink;
import de.manetmodel.network.scalar.ScalarRadioModel;
import de.manetmodel.network.scalar.ScalarRadioNode;

public class SimpleLinkQualityEvaluator
	extends LinkQualityEvaluator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> {

    private SinkWeightedMovingAverageMobilityEvaluator<ScalarRadioNode> sinkMobilityProperty;
    private ConfidenceDistanceEvaluator confidenceDistanceProperty;
    private SourceSinkRelativeDirectionEvaluator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> relativeDirectionProperty;

    public SimpleLinkQualityEvaluator(DoubleScope scoreScope, ScalarRadioModel radioModel,
	    MobilityModel mobilityModel) {

	super(scoreScope);

	this.sinkMobilityProperty = new SinkWeightedMovingAverageMobilityEvaluator<ScalarRadioNode>(
		/* scoreScope */ new DoubleScope(0d, 1d), /* weight */ 1d, /* mobilityModel */ mobilityModel);

	this.confidenceDistanceProperty = new ConfidenceDistanceEvaluator(/* scoreScope */ new DoubleScope(0d, 1d),
		/* weight */ 0.33d, radioModel);
	this.relativeDirectionProperty = new SourceSinkRelativeDirectionEvaluator<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality>(
		/* scoreScope */ new DoubleScope(0d, 1d), /* weight */ 0.33d, mobilityModel);

	this.setPropertyScope(new DoubleScope(0d, 1d));
    }

    @Override
    public boolean compute(ScalarRadioNode source, ScalarRadioLink link, ScalarRadioNode sink) {
	ScalarLinkQuality scalarLinkQuality = link.getWeight();
	scalarLinkQuality.setReceptionConfidence(confidenceDistanceProperty.compute(source, link, sink));
	scalarLinkQuality.setSpeedQuality(sinkMobilityProperty.compute(source, sink));
	scalarLinkQuality.setRelativeMobility(relativeDirectionProperty.compute(source, sink, link));
	scalarLinkQuality
		.setScore(getScore(scalarLinkQuality.getSpeedQuality() + scalarLinkQuality.getReceptionConfidence()));
	return true;
    }

}
