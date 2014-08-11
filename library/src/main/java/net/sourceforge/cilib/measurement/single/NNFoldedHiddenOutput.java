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
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.problem.nn.NNDataTrainingProblem;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.Vector.Builder;

/**
 * Calculates the NN output of the best solution of an algorithm
 * optimizing a {@link NNDataTrainingProblem}, for the entire data set.
 */
public class NNFoldedHiddenOutput implements Measurement {

    private static final long serialVersionUID = -1014032196750640716L;
    private int hiddenLayerNumber = 1;
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
        StandardPatternDataTable generalisationSet = problem.getGeneralisationSet();
        StandardPatternDataTable validationSet = problem.getValidationSet();
        NeuralNetwork neuralNetwork = problem.getNeuralNetwork();
        neuralNetwork.setWeights(solution);

        Layer layer = neuralNetwork.getArchitecture().getLayers().get(hiddenLayerNumber);
        int numNeurons = layer.size();
        
        if(layer.isBias()) {
            numNeurons--;
        }// exclude bias
        
        Builder builder = Vector.newBuilder();
        for (StandardPattern pattern : trainingSet) {
            neuralNetwork.evaluatePattern(pattern);
            Vector outs = layer.getActivations();
            for(int i = 0; i < numNeurons; i++) //for(Numeric out : outs) 
            {
                Bounds bounds = layer.getNeuron(i).getActivationFunction().getBounds();
                double activationMiddleRange = bounds.getLowerBound() + bounds.getRange()/2;
                if(outs.doubleValueOf(i) <= activationMiddleRange) {
                    builder.add(outs.doubleValueOf(i));
                } else {
                    builder.add(outs.doubleValueOf(i) - 2 * (outs.doubleValueOf(i) - activationMiddleRange));
                }
            }
        }
        for (StandardPattern pattern : generalisationSet) {
            neuralNetwork.evaluatePattern(pattern);
            Vector outs = layer.getActivations();
            for(int i = 0; i < numNeurons; i++) //for(Numeric out : outs) 
            {
                Bounds bounds = layer.getNeuron(i).getActivationFunction().getBounds();
                double activationMiddleRange = bounds.getLowerBound() + bounds.getRange()/2;
                if(outs.doubleValueOf(i) <= activationMiddleRange) {
                    builder.add(outs.doubleValueOf(i));
                } else {
                    builder.add(outs.doubleValueOf(i) - 2 * (outs.doubleValueOf(i) - activationMiddleRange));
                }
            }
        }
        for (StandardPattern pattern : validationSet) {
            neuralNetwork.evaluatePattern(pattern);
            Vector outs = layer.getActivations();
            for(int i = 0; i < numNeurons; i++) //for(Numeric out : outs) 
            {
                Bounds bounds = layer.getNeuron(i).getActivationFunction().getBounds();
                double activationMiddleRange = bounds.getLowerBound() + bounds.getRange()/2;
                if(outs.doubleValueOf(i) <= activationMiddleRange) {
                    builder.add(outs.doubleValueOf(i));
                } else {
                    builder.add(outs.doubleValueOf(i) - 2 * (outs.doubleValueOf(i) - activationMiddleRange));
                }
            }
        }
        return builder.build();
    }

    public void setHiddenLayerNumber(int hiddenLayerNumber) {
        this.hiddenLayerNumber = hiddenLayerNumber;
    }
    
}
