package de.thm.icampus.AnalysisTool.visitor;

import java.util.ArrayList;

import com.google.gson.Gson;

import de.thm.icampus.AnalysisTool.ast.QualityElement;

public class EvaluatorVisitor implements Visitor {

    @Override
    public void visit(ArrayList<QualityElement> compositeQualityElement) {

        for (int i = 0; i < compositeQualityElement.size(); ++i) {
            QualityElement obj = (QualityElement) compositeQualityElement.get(i);
            System.out.println(new Gson().toJson(obj));
            if (obj.getChildren() != null) {
                visit(obj.getChildren());
            }
        }
    }
}
