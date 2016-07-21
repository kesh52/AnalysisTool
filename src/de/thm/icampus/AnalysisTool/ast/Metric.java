package de.thm.icampus.AnalysisTool.ast;

import de.thm.icampus.AnalysisTool.evaluation.EvaluationRuleStrategy;

public class Metric extends AtomicQualityElement {

    private String tool = "";
    private String abbreviation = "";

    public Metric(String name, double value, EvaluationRuleStrategy evalRule) {
        // TODO replace with id from xmi?
        this.id = getNextId().incrementAndGet();
        this.name = name;
        this.value = value;
        this.setEvalRule(evalRule);
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

    // Dummy function for normalization
    public double normalize(int normalizationValue) {
        return this.value / normalizationValue;
    }
}
