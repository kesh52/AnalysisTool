package de.thm.icampus.AnalysisTool.ast;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import de.thm.icampus.AnalysisTool.evaluation.EvaluationRuleStrategy;
import de.thm.icampus.AnalysisTool.visitor.Visitor;

// General tree
public abstract class QualityElement {
    private static AtomicInteger nextId = new AtomicInteger();
    protected int id;
    protected String name;
    protected double value;
    protected double weight;

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    protected EvaluationRuleStrategy evalRule;
    protected int parentID = 0;

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void accept(Visitor v) {
        if (this.getChildren() != null) {
            v.visit(this.getChildren());
        }
    }

    public ArrayList<QualityElement> getChildren() {
        return null;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static AtomicInteger getNextId() {
        return nextId;
    }

    public static void setNextId(AtomicInteger nextId) {
        QualityElement.nextId = nextId;
    }

    public EvaluationRuleStrategy getEvalRule() {
        return evalRule;
    }

    public void setEvalRule(EvaluationRuleStrategy evalRule) {
        this.evalRule = evalRule;
    }

    public abstract void print();
    public abstract double eval();

    public void addElement(QualityElement element) {
        element.setParentID(this.getId());
        this.getChildren().add(element);
    }
}
