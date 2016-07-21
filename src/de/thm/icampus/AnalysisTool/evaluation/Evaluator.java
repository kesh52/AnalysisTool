package de.thm.icampus.AnalysisTool.evaluation;

import java.util.ArrayList;
import java.util.List;

import de.thm.icampus.AnalysisTool.Demo;
import de.thm.icampus.AnalysisTool.aggregation.AggregationStrategy;
import de.thm.icampus.AnalysisTool.ast.AtomicQualityElement;
import de.thm.icampus.AnalysisTool.ast.CompositeQualityElement;
import de.thm.icampus.AnalysisTool.ast.QualityElement;

public class Evaluator {

    private static List<Double> result = new ArrayList<Double>();

    protected Evaluator() {
        throw new UnsupportedOperationException(); // prevents calls from subclass
    }

    /**
     * Traverses through the tree and computes the result based on the selected
     * evaluation and aggregation.
     *
     * @param   tree  List with all children of an Quality Element
     * @throws  Exception
     */
    public static void traverse(final ArrayList<QualityElement> tree) throws Exception {
        double temp = 0;
        double evaluatedCompositeValue = 0;

        // TODO check all possible states of the tree
        if (tree == null) {
            throw new Exception("Subtree is empty");
        }

        for (QualityElement obj : tree) {
            System.out.println(Demo.getSpaces() + obj.getName());

            if (obj instanceof CompositeQualityElement) {

                // Don't go into quality elements which don't have children
                if (((QualityElement) obj).getChildren().size() != 0) {
                    Demo.getSpaces().append("   ");

                    // Scan children
                    traverse(((QualityElement) obj).getChildren());

                    Demo.getSpaces().setLength(Demo.getSpaces().length() - 3);


                    /* This variant allows to use different strategies for different composition elements,
                     * like Maintainability -> weightedMean, Security -> arithmeticMean
                     */
                    AggregationStrategy aggrStrategy = ((CompositeQualityElement) obj).getAggrType();

                    /*
                     * Temp is a not evaluated variable Type of operation to
                     * perform on children elements should be configurable, see
                     * ENUM in helpers "AggregationType"
                     *
                     * Default is the arithmetic mean of all normalized and
                     * evaluated values from children
                     */
                    temp = aggrStrategy.compute(result, obj);

                    ((QualityElement) obj).setValue(temp);

                    evaluatedCompositeValue = obj.eval();

                    // Deletes previously saved values to go lvl-up
                    result.clear();
                    // Saves computed together values from the last lvl
                    result.add(evaluatedCompositeValue);

                    System.out.println(
                            Demo.getSpaces() + "" + evaluatedCompositeValue + "(Evaluated value); Computed from: " + temp);
                } else {
                    System.out.println(Demo.getSpaces() + "This element don't participate in the analysis, because it has no children elements");
                }
            }

            // TODO check state -> metric consists of metrics
            if (obj instanceof AtomicQualityElement) {
                temp = obj.eval();
                System.out.println(Demo.getSpaces() + "" + temp + "; Computed from: " + obj.getValue());
                result.add(temp);
            }
        }
    }
}
