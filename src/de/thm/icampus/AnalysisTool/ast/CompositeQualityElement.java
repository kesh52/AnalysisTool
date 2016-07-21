package de.thm.icampus.AnalysisTool.ast;

import java.util.ArrayList;

import de.thm.icampus.AnalysisTool.Demo;
import de.thm.icampus.AnalysisTool.aggregation.AggregationStrategy;
import de.thm.icampus.AnalysisTool.evaluation.EvaluationRuleStrategy;

public class CompositeQualityElement extends QualityElement {
    private ArrayList<QualityElement> children = new ArrayList<>();
    private AggregationStrategy aggrType;

    public AggregationStrategy getAggrType() {
        return aggrType;
    }

    public void setAggrType(AggregationStrategy aggrType) {
        this.aggrType = aggrType;
    }

    public CompositeQualityElement(String name, AggregationStrategy aggrType, EvaluationRuleStrategy evalRule) {
        // TODO replace with id from xmi
        this.id = getNextId().incrementAndGet();
        this.name = name;
        this.aggrType = aggrType;
        this.setEvalRule(evalRule);
    }

    public void print() {
        System.out.println(Demo.getSpaces() + this.name + "-AggregationType:" + this.getAggrType());
        Demo.getSpaces().append("   ");
        for (int i = 0; i < this.getChildren().size(); ++i) {
            QualityElement obj = (QualityElement) this.getChildren().get(i);
            obj.print();
        }
        Demo.getSpaces().setLength(Demo.getSpaces().length() - 3);
    }

    public double eval() {
        return this.getEvalRule().eval(this.getValue());
    }

    public ArrayList<QualityElement> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<QualityElement> children) {
        this.children = children;
    }
}
