package de.thm.icampus.AnalysisTool;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.thm.icampus.AnalysisTool.ast.QualityElement;
import de.thm.icampus.AnalysisTool.evaluation.Evaluator;
import de.thm.icampus.AnalysisTool.io.input.PHPCPDParser;
import de.thm.icampus.AnalysisTool.io.input.PHPLOCParser;
import de.thm.icampus.AnalysisTool.io.input.Parser;
import de.thm.icampus.AnalysisTool.io.input.QualityModelParser;
import de.thm.icampus.AnalysisTool.io.output.Output;
import de.thm.icampus.AnalysisTool.io.output.XMLOutput;
import de.thm.icampus.AnalysisTool.visitor.MetricsVisitor;
import de.thm.icampus.AnalysisTool.visitor.Visitor;

public class Demo {

    private static final Logger logger = LogManager.getLogger();

    private static final String QM = "qm";
    private static final String OUTPUT_FORMAT = "outputFormat";
    private static final String PHPMD = "phpmd";
    private static final String PHPLOC = "phploc";
    private static final String PHPCPD = "phpcpd";
    private static final String KLASSEN_NAME = "de.thm.icampus.AnalysisTool.Demo";

    private static List<Parser> parsers = new ArrayList<Parser>();

    /**
     * Variable for output spaces.
     */
    private static StringBuffer spaces = new StringBuffer();

    /**
     * Don't allow to create an instance of Demo.
     */
    protected Demo() {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) throws ParseException {
        logger.info("Start application");
        Config config = processArguments(args);
        QualityElement qualityModel = processQualityModel(config);
        processPHPLOCReport(config);
        processPHPCPDReport(config);

        Visitor v = new MetricsVisitor();
        for (Parser parser : parsers) {
            // in order to use duplicationRate metric, use "duplicationRate" abbreviation in editor
            ((MetricsVisitor) v).assignMetricsValues(qualityModel.getChildren(), parser);
        }

        Output outXML = new XMLOutput();
        outXML.print(qualityModel);

        try {
            logger.info("Start evaluate quality model");
            Evaluator.traverse(qualityModel.getChildren());
            logger.info("Stop evaluate quality model");
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("Stop application");

        // evaluate created tree

        /*
         * // parse xml to the ast // evaluate created tree
         *
         * // TODO set aggregation type from the quality model AggregationStrategy aggrType = new ArithmeticMeanAggregation(); EvaluationRuleStrategy
         * rule = new ThreeNotesEvaluationRule(10, 50, 100); //EvaluationRuleStrategy rule = new FiveNotesEvaluationRule(10, 20, 30, 40, 50);
         *
         * CompositeQualityElement root = new CompositeQualityElement("ROOT", aggrType, rule);
         *
         * CompositeQualityElement iso25010 = new CompositeQualityElement("ISO25010", aggrType, rule); root.addElement(iso25010);
         *
         * QualityAttribute maintainability = new QualityAttribute("Maintainability", aggrType, rule); iso25010.addElement(maintainability);
         *
         * QualityAttribute analysability = new QualityAttribute("Analysability", aggrType, rule); maintainability.addElement(analysability);
         *
         * QualityAttribute modularity = new QualityAttribute("Modularity", aggrType, rule); maintainability.addElement(modularity);
         *
         * QualityAttribute security = new QualityAttribute("Security", aggrType, rule); iso25010.addElement(security);
         *
         * Metric m3 = new Metric("M3", 9000, rule); modularity.addElement(m3);
         *
         * Metric m4 = new Metric("M4", 4800, rule); modularity.addElement(m4);
         *
         * Concept complexity = new Concept("Complexity", aggrType, rule); analysability.addElement(complexity);
         *
         * Metric m1 = new Metric("McCabe CC", 4200, rule); complexity.addElement(m1);
         *
         * Metric m2 = new Metric("ACD", 6000, rule); complexity.addElement(m2);
         *
         * //root.print();
         *
         * // Use here evaluator, get as parameter the root's children try { Evaluator.traverse(root.getChildren()); } catch (Exception e) {
         * e.printStackTrace(); }
         *
         * // Serialize to JSON using Gson //Output outJSON = new JSONOutput(); //Output outXML = new XMLOutput(); //outJSON.print(root);
         * //outXML.print(root);
         *
         * // Test Visitor //Visitor v = new EvaluatorVisitor(); //v.visit(root.getChildren());
         *
         * Visitor v = new MetricsVisitor(); v.visit(root.getChildren());
         */
    }

    /**
     * @param config
     */
    private static void processPHPCPDReport(Config config) {
        logger.info("Start parsing PHPCPD report.");
        String phpcpdPath = config.getPhpcpdPath();
        Parser phpcpdParser = null;
        boolean phpcpdPathNull = phpcpdPath == null;
        boolean phpcpdPathEmpty = phpcpdPath.trim().isEmpty();
        if (!phpcpdPathNull && !phpcpdPathEmpty) {
            phpcpdParser = PHPCPDParser.getInstance();
            phpcpdParser.parseFile(phpcpdPath);
            parsers.add(phpcpdParser);
            logger.info("DUPLICATION RATE: " + phpcpdParser.getValue("duplicationRate"));
        } else {
            logger.error("Path to PHPCPD report is empty or corrupt.");
        }
        logger.info("PHPCPD report successfully parsed.");
    }

    /**
     * @param config
     */
    private static void processPHPLOCReport(Config config) {
        logger.info("Start parsing PHPLOC report.");
        String phplocPath = config.getPhplocPath();
        Parser phplocParser = null;
        boolean phplocPathNull = phplocPath == null;
        boolean phplocPathEmpty = phplocPath.trim().isEmpty();
        if (!phplocPathNull && !phplocPathEmpty) {
            phplocParser = PHPLOCParser.getInstance();
            phplocParser.parseFile(phplocPath);
            parsers.add(phplocParser);
        } else {
            logger.error("Path to PHPLOC report is empty or corrupt.");
        }
        logger.info("PHPLOC report successfully parsed.");
    }

    /**
     * @param config
     * @return
     */
    private static QualityElement processQualityModel(Config config) {
        logger.info("Start parsing quality model");
        QualityElement qualityModel = null;
        String qmPath = config.getQmPath();
        boolean qmPathNull = qmPath == null;
        boolean qmPathEmpty = qmPath.trim().isEmpty();
        if (!qmPathNull && !qmPathEmpty) {
            QualityModelParser qmParser = new QualityModelParser();
            qmParser.parseFile(qmPath);
            qualityModel = qmParser.getQualityModel();
        } else {
            logger.error("Path to quality model is empty or corrupt.");
        }
        logger.info("Quality model successfully parsed.");
        return qualityModel;
    }

    private static Config processArguments(String[] args) {
        HelpFormatter helpFormater = new HelpFormatter();
        Options options = new Options();
        Option qualityModelFile = Option.builder().longOpt(QM).hasArg().valueSeparator('=').desc("Quality model").build();
        options.addOption(qualityModelFile);
        Option outputFormat = Option.builder().longOpt(OUTPUT_FORMAT).hasArg().valueSeparator('=').desc("Output format: xml or json").build();
        options.addOption(outputFormat);
        Option phploc = Option.builder().longOpt(PHPLOC).hasArg().valueSeparator('=').desc("Path to phploc report").build();
        options.addOption(phploc);
        Option phpmd = Option.builder().longOpt(PHPMD).hasArg().valueSeparator('=').desc("Path to phpmd report").build();
        options.addOption(phpmd);
        Option phpcpd = Option.builder().longOpt(PHPCPD).hasArg().valueSeparator('=').desc("Path to phpcpd report").build();
        options.addOption(phpcpd);

        options.addOption("h", false, "display help");

        CommandLineParser parser = new DefaultParser();
        Config config = new Config();
        try {
            CommandLine line = parser.parse(options, args);

            if (line.hasOption("h")) {
                helpFormater.printHelp(KLASSEN_NAME, options);
            }
            if (line.hasOption(PHPCPD)) {
                config.setPhpcpdPath(line.getOptionValue(PHPCPD));
            }
            if (line.hasOption(PHPLOC)) {
                config.setPhplocPath(line.getOptionValue(PHPLOC));
            }
            if (line.hasOption(PHPMD)) {
                config.setPhpmdPath(line.getOptionValue(PHPMD));
            }
            if (line.hasOption(QM)) {
                config.setQmPath(line.getOptionValue(QM));
            }
            if (line.hasOption(OUTPUT_FORMAT)) {
                config.setOutputFormatValue(line.getOptionValue(OUTPUT_FORMAT));
            }
        } catch (ParseException exp) {
            // oops, something went wrong
            logger.error("Parsing failed.  Reason: " + exp.getMessage());
        }
        return config;
    }

    public static StringBuffer getSpaces() {
        return spaces;
    }

    public static void setSpaces(StringBuffer spaces) {
        Demo.spaces = spaces;
    }

}
