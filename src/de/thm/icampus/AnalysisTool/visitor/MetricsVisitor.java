package de.thm.icampus.AnalysisTool.visitor;

import java.util.ArrayList;

import de.thm.icampus.AnalysisTool.ast.Metric;
import de.thm.icampus.AnalysisTool.ast.QualityElement;
import de.thm.icampus.AnalysisTool.io.input.Parser;

public class MetricsVisitor implements Visitor {
    private static int metricCounter = 0;

    public void assignMetricsValues(ArrayList<QualityElement> compositeQualityElement, Parser parser) {
        if (compositeQualityElement != null) {
            for (QualityElement qualityElement : compositeQualityElement) {
                if (qualityElement instanceof Metric) {
                    try {
                        String value = parser.getValue(((Metric) qualityElement).getAbbreviation());
                        if (value != null) {
                            qualityElement.setValue(Double.parseDouble(value));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (qualityElement.getChildren() != null) {
                    assignMetricsValues(qualityElement.getChildren(), parser);
                }
            }
        }
    }

    public void visit(ArrayList<QualityElement> compositeQualityElement) {
        traverse(compositeQualityElement);
        System.out.println(metricCounter + " Metrics found");
    }

    private void traverse(ArrayList<QualityElement> compositeQualityElement) {
        if (compositeQualityElement != null) {
            for (QualityElement qualityElement : compositeQualityElement) {
                if (qualityElement instanceof Metric) {
                    System.out.println("Metric found");
                    metricCounter++;
                }
                if (qualityElement.getChildren() != null) {
                    traverse(qualityElement.getChildren());
                }
            }
        }
    }

}
