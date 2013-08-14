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
import net.sourceforge.cilib.problem.nn.NNDataTrainingProblem;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.Vector.Builder;

/**
 * Calculates the NN output of the best solution of an algorithm
 * optimizing a {@link NNDataTrainingProblem}, for the entire data set.
 */
public class NNOutput implements Measurement {

    private static final long serialVersionUID = -1014032196750640716L;
    /**
     * {@inheritDoc }
     */
    @Override
    public Measurement getClone() {
        return this;
    }

    /**
     * Return NN outputs for the entire dataset, using the current best solution to set up the weights.
     */
    @Override
    public Type getValue(Algorithm algorithm) {
        Vector solution = (Vector) algorithm.getBestSolution().getPosition();
        NNTrainingProblem problem = (NNTrainingProblem) algorithm.getOptimisationProblem();
        StandardPatternDataTable trainingSet = problem.getTrainingSet();
        StandardPatternDataTable generalizationSet = problem.getGeneralisationSet();
         StandardPatternDataTable validationSet = problem.getValidationSet();
        NeuralNetwork neuralNetwork = problem.getNeuralNetwork();
        neuralNetwork.setWeights(solution);

        Builder builder = Vector.newBuilder();
        for (StandardPattern pattern : trainingSet) {
            Vector outs = neuralNetwork.evaluatePattern(pattern);
            for(Numeric out : outs) {
                builder.add(out);
            }
        }
        for (StandardPattern pattern : generalizationSet) {
            Vector outs = neuralNetwork.evaluatePattern(pattern);
            for(Numeric out : outs) {
                builder.add(out);
            }
        }
        for (StandardPattern pattern : validationSet) {
            Vector outs = neuralNetwork.evaluatePattern(pattern);
            for(Numeric out : outs) {
                builder.add(out);
            }
        }
        return builder.build();
    }
}
