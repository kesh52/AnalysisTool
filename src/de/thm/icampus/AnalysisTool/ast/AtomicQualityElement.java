package de.thm.icampus.AnalysisTool.ast;

import de.thm.icampus.AnalysisTool.Demo;

public abstract class AtomicQualityElement extends QualityElement {

    public void print() {
        System.out.println(Demo.getSpaces() + this.getName() + "(Value:" + this.getValue() + "; Weight:" + this.getWeight());
    }

    public double eval() {
        //double normalizedValue = normalize(100);
        return this.getEvalRule().eval(this.getValue());
    }

    public abstract double normalize(int normalizationValue);
}
