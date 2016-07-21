package de.thm.icampus.AnalysisTool.io.input;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class PHPLOCParser implements Parser {

    private static final Logger logger = LogManager.getLogger(PHPLOCParser.class);

    private Element rootNode = null;

    private static PHPLOCParser instance;

    public static PHPLOCParser getInstance() {
        if (PHPLOCParser.instance == null) {
            PHPLOCParser.instance = new PHPLOCParser();
        }
        return PHPLOCParser.instance;
    }

    private PHPLOCParser() {
    }

    public void parseFile(final String filename) {
        System.out.println(filename);
        SAXBuilder builder = new SAXBuilder();

        try {
            File xmlFile = new File(filename);
            boolean fileExists = xmlFile.exists();
            boolean isDirectory = xmlFile.isDirectory();
            if (!fileExists || isDirectory) {
                throw new IOException("File " + filename + " not found.");
            }

            Document document = (Document) builder.build(xmlFile);
            this.rootNode = document.getRootElement();
        } catch (IOException io) {
            logger.error(io.getMessage());
        } catch (JDOMException jdomex) {
            logger.error(jdomex.getMessage());
        }
    }

    public String getValue(String name) {
        Element metric = null;
        try {
            if (this.rootNode == null) {
                throw new Exception("You should first parse a PHPLOC report.");
            }

            metric = this.rootNode.getChild(name);
            if (metric == null) {
                throw new Exception(name + " - Metric not found.");
            }
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            return null;
        }

        return metric.getText();
    }
}
