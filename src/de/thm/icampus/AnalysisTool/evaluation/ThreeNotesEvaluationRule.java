package de.thm.icampus.AnalysisTool.evaluation;

public class ThreeNotesEvaluationRule implements EvaluationRuleStrategy {
    private int p1;
    private int p2;
    private int p3;

    public ThreeNotesEvaluationRule(int p1, int p2, int p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }
    public ThreeNotesEvaluationRule(String[] rules) {
        // TODO Auto-generated constructor stub
    }
    public int getP1() {
        return p1;
    }
    public void setP1(int p1) {
        this.p1 = p1;
    }
    public int getP2() {
        return p2;
    }
    public void setP2(int p2) {
        this.p2 = p2;
    }
    public int getP3() {
        return p3;
    }
    public void setP3(int p3) {
        this.p3 = p3;
    }

    public double eval(double value) {
        if (value <= this.getP1()) {
            return 1;
        } else if ((value > this.getP1()) && (value <= this.getP2())) {
            return 2;
        } else if ((value > this.getP2()) && (value <= this.getP3())) {
            return 3;
        }

        return 0;
    }
}
