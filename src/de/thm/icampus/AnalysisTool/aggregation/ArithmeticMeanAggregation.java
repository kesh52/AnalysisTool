package de.thm.icampus.AnalysisTool.aggregation;

import java.util.List;

import de.thm.icampus.AnalysisTool.ast.QualityElement;

public class ArithmeticMeanAggregation implements AggregationStrategy {

    private String name = "ArithmeticMeanAggregation";

    public String getName() {
        return name;
    }

    public double compute(List<Double> result, QualityElement obj) {
        return result.stream().mapToDouble(i -> i).sum()
                / ((QualityElement) obj).getChildren().size();
    }

    @Override
    public String toString() {
        return "ArithmeticMeanAggregation [name=" + name + "]";
    }

}
