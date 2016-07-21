package de.thm.icampus.AnalysisTool.io.input;

public interface Parser {
    public void parseFile(String filename);
    public String getValue(String name);
}
