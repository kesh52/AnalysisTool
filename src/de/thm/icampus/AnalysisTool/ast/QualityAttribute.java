package de.thm.icampus.AnalysisTool.ast;

import de.thm.icampus.AnalysisTool.aggregation.AggregationStrategy;
import de.thm.icampus.AnalysisTool.evaluation.EvaluationRuleStrategy;

public class QualityAttribute extends CompositeQualityElement {
    public QualityAttribute(String name, AggregationStrategy aggrType, EvaluationRuleStrategy evalRule) {
        super(name, aggrType, evalRule);
    }
}
