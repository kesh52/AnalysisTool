package de.thm.icampus.AnalysisTool.io.output;

import de.thm.icampus.AnalysisTool.ast.QualityElement;

public interface Output {

    // Serializes a string and prints it in the console
    void print(String value);

    // Serializes an object and prints it in the console
    void print(QualityElement element);

    // SeSerializes an object and saves it to the file
    void write(QualityElement element, String path);
}
