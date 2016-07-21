package de.thm.icampus.AnalysisTool.io.output;

import com.google.gson.Gson;

import de.thm.icampus.AnalysisTool.ast.QualityElement;

public class JSONOutput implements Output {

    public void print(String value) {
        System.out.println(new Gson().toJson(value));
    }

    public void print(QualityElement element) {
        System.out.println(new Gson().toJson(element));

    }

    @Override
    public void write(QualityElement element, String path) {
        // TODO Auto-generated method stub
    }
}
