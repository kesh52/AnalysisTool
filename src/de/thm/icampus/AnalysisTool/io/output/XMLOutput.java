package de.thm.icampus.AnalysisTool.io.output;

import com.thoughtworks.xstream.XStream;

import de.thm.icampus.AnalysisTool.ast.QualityElement;

public class XMLOutput implements Output {

    public void print(String value) {
        System.out.println(new XStream().toXML(value));
    }

    public void print(QualityElement element) {
        System.out.println(new XStream().toXML(element));
    }

    @Override
    public void write(QualityElement element, String path) {
        // TODO Auto-generated method stub

    }

}
