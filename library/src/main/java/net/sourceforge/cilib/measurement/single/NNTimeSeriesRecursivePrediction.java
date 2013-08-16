/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.Vector.Builder;

/**
 */
public class NNTimeSeriesRecursivePrediction implements Measurement {

    private static final long serialVersionUID = -1014032196750640716L;
    private int n = 1; // specifies the extent of recursiveness. Can't be bigger than embedding.
    private int extraSteps = 0;
    /**
     * {@inheritDoc }
     */
    @Override
    public Measurement getClone() {
        return this;
    }

    /**
     * 
     */
    @Override
    public Type getValue(Algorithm algorithm) {
        Vector solution = (Vector) algorithm.getBestSolution().getPosition();
        NNTrainingProblem problem = (NNTrainingProblem) algorithm.getOptimisationProblem();
        StandardPatternDataTable dataSet = new StandardPatternDataTable();
        StandardPatternDataTable trainingSet = problem.getTrainingSet();
        StandardPatternDataTable generalisationSet = problem.getGeneralisationSet();
        StandardPatternDataTable validationSet = problem.getValidationSet();
        for (StandardPattern pattern : trainingSet) {
            dataSet.addRow(pattern);
        }
        for (StandardPattern pattern : generalisationSet) {
            dataSet.addRow(pattern);
        }
        for (StandardPattern pattern : validationSet) {
            dataSet.addRow(pattern);
        }

        NeuralNetwork neuralNetwork = problem.getNeuralNetwork();
        neuralNetwork.setWeights(solution);

        Builder builder = Vector.newBuilder(); // final solution vector
        Vector predicted = Vector.of(); // predicted stuff!
        // Now that n predictions are gathered, do the cycle
        for (StandardPattern pattern : dataSet) {
            // set the input vector
            Vector inputs = pattern.getVector();
            if(!predicted.isEmpty()) {
                for(int j = 0; j < n; j++ ) { // substitute predicted values into the pattern
                    inputs.setReal(j, predicted.get(j).doubleValue());
                }
            }
            // get the prediction
            //System.out.println("Inputs: "+inputs.toString());
            pattern.setVector(inputs);
            predicted = neuralNetwork.evaluatePattern(pattern);
            // add to the resulting vector
            for(Numeric element : predicted) {
                builder.add(element);
            }
        }
        for(int i = 0; i < extraSteps; i++) { // add extra steps - pure prediction, fully recurrent
            StandardPattern pattern = new StandardPattern();
            pattern.setVector(predicted);
            predicted = neuralNetwork.evaluatePattern(pattern);
            // add to the resulting vector
            for(Numeric element : predicted) {
                builder.add(element);
            }            
        }
        return builder.build();
    }

    public int getExtraSteps() {
        return extraSteps;
    }

    public void setExtraSteps(int extraSteps) {
        this.extraSteps = extraSteps;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

}
