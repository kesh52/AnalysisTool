package de.thm.icampus.AnalysisTool.aggregation;

import java.util.List;

import de.thm.icampus.AnalysisTool.ast.QualityElement;

public interface AggregationStrategy {
    double compute(List<Double> result, QualityElement obj);
}
