package de.thm.icampus.AnalysisTool.io.input;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import de.thm.icampus.AnalysisTool.aggregation.AggregationStrategy;
import de.thm.icampus.AnalysisTool.aggregation.ArithmeticMeanAggregation;
import de.thm.icampus.AnalysisTool.aggregation.WeightedMeanAggregation;
import de.thm.icampus.AnalysisTool.ast.CompositeQualityElement;
import de.thm.icampus.AnalysisTool.ast.Concept;
import de.thm.icampus.AnalysisTool.ast.Metric;
import de.thm.icampus.AnalysisTool.ast.QualityAttribute;
import de.thm.icampus.AnalysisTool.ast.QualityElement;
import de.thm.icampus.AnalysisTool.evaluation.EvaluationRuleStrategy;
import de.thm.icampus.AnalysisTool.evaluation.ThreeNotesEvaluationRule;
import de.thm.icampus.AnalysisTool.io.output.Output;
import de.thm.icampus.AnalysisTool.io.output.XMLOutput;

public class QualityModelParser implements Parser{

    // XML root node
    private Element rootNode = null;

    // Quality model
    private QualityElement qualityModel = null;

    public QualityElement getQualityModel() {
        return qualityModel;
    }

    public void setQualityModel(QualityElement qualityModel) {
        this.qualityModel = qualityModel;
    }

    private final void assignEvaluationModel(Element root, QualityElement element) {
        Element evaluationModel = this.getEvaluationModel(root);
        Element evaluationsNode = this.getEvaluationsNode(evaluationModel);
        List<Element> evaluations = this.getEvaluations(evaluationsNode);

        for (Element evaluation : evaluations) {
            String metric = evaluation.getAttributeValue("metric");
            boolean isMetricNull = metric == null;
            String qualityAttribte = evaluation.getAttributeValue("qualityattribute");
            boolean isQualityAttributeNull = qualityAttribte == null;
            String concept = evaluation.getAttributeValue("concept");
            boolean isConceptNull = concept == null;

            String elementName = element.getName();
            if (!isMetricNull) {
                boolean isMetricEmpty = metric.isEmpty();
                boolean metricMatch = metric.equals(elementName);
                if (!isMetricEmpty && metricMatch) {
                    element.setWeight(Double.parseDouble(evaluation.getAttributeValue("weight")));

                    String[] rules = getEvaluationRules(evaluation);
                    EvaluationRuleStrategy eval = new ThreeNotesEvaluationRule(rules);
                    ((Metric) element).setEvalRule(eval);
                }
            } else if (!isQualityAttributeNull) {
                boolean isQualityAttributeEmpty = qualityAttribte.isEmpty();
                boolean qualityAttributeMatch = qualityAttribte.equals(elementName);
                if (!isQualityAttributeEmpty && qualityAttributeMatch) {
                    Double weightValue = getWeightFromEvaluationModel(evaluation);
                    element.setWeight(weightValue);

                    String aggregationStrategyValue = evaluation.getAttributeValue("aggregationType");
                    AggregationStrategy aggregationStrategyInstance = getStrategyInstance(aggregationStrategyValue);
                    ((QualityAttribute) element).setAggrType(aggregationStrategyInstance);

                    String[] rules = getEvaluationRules(evaluation);
                    EvaluationRuleStrategy eval = new ThreeNotesEvaluationRule(rules);
                    ((QualityAttribute) element).setEvalRule(eval);
                }
            } else if (!isConceptNull) {
                boolean isConceptEmpty = concept.isEmpty();
                boolean conceptMatch = concept.equals(elementName);
                if (!isConceptEmpty && conceptMatch) {
                    Double weightValue = getWeightFromEvaluationModel(evaluation);
                    element.setWeight(weightValue);

                    String aggregationStrategyValue = evaluation.getAttributeValue("aggregationType");
                    AggregationStrategy aggregationStrategyInstance = getStrategyInstance(aggregationStrategyValue);
                    ((Concept) element).setAggrType(aggregationStrategyInstance);

                    String[] rules = getEvaluationRules(evaluation);
                    EvaluationRuleStrategy eval = new ThreeNotesEvaluationRule(rules);
                    ((Concept) element).setEvalRule(eval);
                }
            }
        }
    }

    /**
     * @param evaluation
     */
    private String[] getEvaluationRules(Element evaluation) {
        String evalRule = evaluation.getAttributeValue("evalRule");
        evalRule = evalRule == null ? "" : evalRule;

        String[] rules = null;
        boolean isEvalRuleEmpty = evalRule.isEmpty();
        if (!isEvalRuleEmpty) {
            rules = evalRule.trim().replaceAll("\\s+","").split(";");
        }

        return rules;
    }

    /**
     * Checks weight value and returns a double value
     *
     * @param evaluation
     */
    private Double getWeightFromEvaluationModel(Element evaluation) {
        String weight = evaluation.getAttributeValue("weight");
        Double weightValue = weight == null ? Double.parseDouble("0.0") : Double.parseDouble(weight);
        return weightValue;
    }

    /**
     * Builds quality model from xml file
     *
     * @param   QualityElement  qmNode  Quality model nodes
     * @param   List<Element>   nodes   List of nodes of XML file
     */
    private final void buildModelFromXML(QualityElement qmNode, List<Element> nodes) {
        boolean areNodesEmpty = nodes.isEmpty();
        if (!areNodesEmpty) {
            for (int i = 0; i < nodes.size(); i++) {
                Element node = (Element) nodes.get(i);
                QualityElement newNode = createNewNode(qmNode, node);

                // Go deeper
                if (hasChildren(node)) {
                    List<Element> qAttributes = this.getQualityModelAttributes(node);
                    this.buildModelFromXML(newNode, qAttributes);

                    List<Element> qConcepts = this.getQualityModelConcepts(node);
                    this.buildModelFromXML(newNode, qConcepts);

                    List<Element> qMetrics = this.getQualityModelMetrics(node);
                    this.buildModelFromXML(newNode, qMetrics);
                }
            }
        }
    }

    /**
     * Returns new node with parameters
     *
     * @param qmNode
     * @param node
     */
    private QualityElement createNewNode(QualityElement qmNode, Element node) {
        QualityElement newNode = null;
        String nodeName = node.getName();
        String newNodeName = node.getAttributeValue("name");

        if (nodeName.equals("qualityattribute")) {
            newNode = new QualityAttribute(newNodeName, null, null);
        } else if (nodeName.equals("concept")) {
            newNode = new Concept(newNodeName, null, null);
        } else if (nodeName.equals("metric")) {
            newNode = new Metric(newNodeName, 0.0, null);

            Map<String, String> toolMetric = this.findToolMetricByMetricFromCollection(this.rootNode, node.getAttributeValue("cmetric"));
            String abbreviation = toolMetric.get("metricAbbreviation");
            abbreviation = abbreviation == null ? "" : abbreviation;
            ((Metric) newNode).setAbbreviation(abbreviation);

            String toolName = toolMetric.get("toolName");
            toolName = toolName == null ? "" : toolName;
            ((Metric) newNode).setTool(toolName);
        }
        qmNode.addElement(newNode);
        return newNode;
    }

    @SuppressWarnings("unused")
    private final Element findMetricFromMetricsCollections(final Element rootNode, final String name) {
        Element collections = this.getCollections(rootNode);
        Element metricsCollection = this.getMetricsCollection(collections);
        @SuppressWarnings("unchecked")
        List<Element> metrics = metricsCollection.getChildren();
        for (Element metric : metrics) {
            if (metric.getAttributeValue("name").equals(name)) {
                return metric;
            }
        }

        return null;
    }

    private final Map<String, String> findToolMetricByMetricFromCollection(Element rootNode, String collectionMetricName) {

        Element toolsCollection = this.getToolsCollection(rootNode);
        @SuppressWarnings("unchecked")
        List<Element> tools = toolsCollection.getChildren("ctool");

        for (Element tool : tools) {

            List<Element> metrics = this.getToolMetrics(tool);

            for (Element metric : metrics) {

                Boolean nameMatch = metric.getAttributeValue("metric").equals(collectionMetricName);
                Boolean isActive = Boolean.valueOf(metric.getAttributeValue("active"));
                if (nameMatch && isActive) {
                    Map<String, String> toolMetric = new HashMap<String, String>();
                    toolMetric.put("toolName", tool.getAttributeValue("name"));
                    toolMetric.put("metricAbbreviation", metric.getAttributeValue("abbreviation"));
                    return toolMetric;
                }
            }
        }

        return null;
    }

    /**
     * Returns the element <Collections> which contains qualityattributes, concepts and metrics
     *
     * @param rootNode
     *            Is a <QualityFramework> node
     * @return Element
     */
    private final Element getCollections(Element rootNode) {
        return rootNode.getChild("Collections");
    }

    private final Element getEvaluationModel(Element root) {
        return root.getChild("EvaluationModel");
    }

    @SuppressWarnings("unchecked")
    private final List<Element> getEvaluations(Element evaluationsNode) {
        return evaluationsNode.getChildren("evaluation");
    }

    private final Element getEvaluationsNode(Element evaluationModelNode) {
        return evaluationModelNode.getChild("evaluations");
    }

    @SuppressWarnings("unchecked")
    private final List<Element> getToolMetrics(final Element tool) {
        return tool.getChildren();
    }

    private final Element getToolsCollection(final Element rootNode) {
        Element collections = this.getCollections(rootNode);
        return collections.getChild("tools");
    }

    private final Element getQualityModel(Element rootNode) {
        return rootNode.getChild("QualityModel");
    }

    private final Element getMetricsCollection(Element collectionsNode) {
        return collectionsNode.getChild("metrics");
    }

    private final AggregationStrategy getStrategyInstance(String aggregationStrategy) {
        if (aggregationStrategy.equals("ArithmeticMeanAggregation")) {
            return new ArithmeticMeanAggregation();
        } else if (aggregationStrategy.equals("WeightedArithmeticMeanAggregation")) {
            return new WeightedMeanAggregation();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private final List<Element> getQualityModelAttributes(Element node) {
        return node.getChildren("qualityattribute");
    }

    @SuppressWarnings("unchecked")
    private final List<Element> getQualityModelMetrics(Element node) {
        return node.getChildren("metric");
    }

    @SuppressWarnings("unchecked")
    private final List<Element> getQualityModelConcepts(Element node) {
        return node.getChildren("concept");
    }

    /**
     * Returns if a node has children
     *
     * @param node
     * @return
     */
    private final boolean hasChildren(Element node) {
        return node.getChildren().isEmpty() ? false : true;
    }

    /**
     * Parses a given quality model.
     *
     * @param filename
     *            Path to the filename
     *
     * @return CompositeQualityElement
     */
    public final void parseFile(final String filename) {
        SAXBuilder builder = new SAXBuilder();

        // TODO check if file exists and has right xml structure
        File xmlFile = new File(filename);

        try {
            Document document = (Document) builder.build(xmlFile);
            this.rootNode = document.getRootElement();
            Element qualityModelNode = getQualityModel(this.rootNode);

            QualityElement qualityModel = new CompositeQualityElement(qualityModelNode.getName(), null, null);

            // First level
            List<Element> qualityAttributes = this.getQualityModelAttributes(qualityModelNode);
            buildModelFromXML(qualityModel, qualityAttributes);
            // root.print();

            // Assign parameters to quality model from evaluation model
            traverseEvaluationModel(this.rootNode, qualityModel);
            qualityModel.print();

            this.qualityModel = qualityModel;
            Output outXML = new XMLOutput();
            outXML.print(qualityModel);

        } catch (IOException io) {
            System.out.println(io.getMessage());
        } catch (JDOMException jdomex) {
            System.out.println(jdomex.getMessage());
        }
//        return null;
    }

    private final void traverseEvaluationModel(Element root, QualityElement qualityModel) {
        if (qualityModel == null || qualityModel.getChildren() == null) {
            return;
        }

        for (int i = 0; i < qualityModel.getChildren().size(); ++i) {
            QualityElement obj = (QualityElement) qualityModel.getChildren().get(i);
            this.assignEvaluationModel(root, obj);
            this.traverseEvaluationModel(root, obj);
        }
    }

    @Override
    public String getValue(String name) {
        // TODO Auto-generated method stub
        return null;
    }

}
