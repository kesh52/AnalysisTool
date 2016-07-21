package de.thm.icampus.AnalysisTool.evaluation;

public class FiveNotesEvaluationRule implements EvaluationRuleStrategy {
    private int p1;
    private int p2;
    private int p3;
    private int p4;
    private int p5;

    public FiveNotesEvaluationRule(int p1, int p2, int p3, int p4, int p5) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.p5 = p5;
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

    public int getP4() {
        return p4;
    }
    public void setP4(int p4) {
        this.p4 = p4;
    }
    public int getP5() {
        return p5;
    }
    public void setP5(int p5) {
        this.p5 = p5;
    }
    public double eval(double value) {
        if (value < this.getP1()) {
            return 1;
        } else if ((value > this.getP1()) && (value < this.getP2())) {
            return 2;
        } else if ((value > this.getP2()) && (value < this.getP3())) {
            return 3;
        } else if ((value > this.getP3()) && (value < this.getP4())) {
            return 4;
        } else if ((value > this.getP4()) && (value < this.getP5())) {
            return 5;
        }

        return 0;
    }
}
