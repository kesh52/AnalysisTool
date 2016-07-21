package de.thm.icampus.AnalysisTool.aggregation;

import java.util.List;

import de.thm.icampus.AnalysisTool.ast.QualityElement;

public class WeightedMeanAggregation implements AggregationStrategy {

    private String name = "WeightedMeanAggregation";

    public String getName() {
        return name;
    }

    @Override
    public double compute(List<Double> result, QualityElement obj) {
        // TODO Auto-generated method stub
        return -50;
    }

    @Override
    public String toString() {
        return "WeightedMeanAggregation [name=" + name + "]";
    }

}
