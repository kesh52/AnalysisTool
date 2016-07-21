package de.thm.icampus.AnalysisTool.ast;

import de.thm.icampus.AnalysisTool.aggregation.AggregationStrategy;
import de.thm.icampus.AnalysisTool.evaluation.EvaluationRuleStrategy;

public class Concept extends CompositeQualityElement {
    public Concept(String name, AggregationStrategy aggrType, EvaluationRuleStrategy evalRule) {
        super(name, aggrType, evalRule);
    }
}
