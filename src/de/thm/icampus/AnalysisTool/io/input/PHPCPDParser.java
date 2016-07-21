package de.thm.icampus.AnalysisTool.io.input;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class PHPCPDParser implements Parser {

    private static final Logger logger = LogManager.getLogger(PHPCPDParser.class);

    private Element rootNode = null;

    private static PHPCPDParser instance;

    public static PHPCPDParser getInstance() {
        if (PHPCPDParser.instance == null) {
            PHPCPDParser.instance = new PHPCPDParser();
        }
        return PHPCPDParser.instance;
    }

    private PHPCPDParser() {
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

    // TODO refactor
    public String getValue(String name) {
        boolean metricMatch = name.equals("duplicationRate");
        if (!metricMatch) {
            return null;
        }

        double loc = Double.parseDouble(PHPLOCParser.getInstance().getValue("loc"));

        int duplicatedLines = 0;
        int lines = 0;
        @SuppressWarnings("unchecked")
        List<Element> duplications = this.rootNode.getChildren("duplication");
        boolean duplicationsEmpty = duplications.isEmpty();
        if (duplicationsEmpty) {
            logger.error("There is no duplications or phpcpd file may be corrupt.");
        }
        for (Element duplication : duplications) {
            lines = Integer.parseInt(duplication.getAttributeValue("lines"));
            duplicatedLines += lines;
        }

        double duplicationRation = (duplicatedLines / loc) * 100;
        return String.valueOf(duplicationRation);
    }
}
