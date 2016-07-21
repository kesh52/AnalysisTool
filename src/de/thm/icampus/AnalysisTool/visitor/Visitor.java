package de.thm.icampus.AnalysisTool.visitor;

import java.util.ArrayList;

import de.thm.icampus.AnalysisTool.ast.QualityElement;

public interface Visitor {
    void visit(ArrayList<QualityElement> compositeQualityElement);
}
