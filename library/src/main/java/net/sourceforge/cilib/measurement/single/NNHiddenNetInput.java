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
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.problem.nn.NNDataTrainingProblem;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.Vector.Builder;

/**
 * Calculates the NN output of the best solution of an algorithm
 * optimizing a {@link NNDataTrainingProblem}, for the entire data set.
 */
public class NNHiddenNetInput implements Measurement {

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
        StandardPatternDataTable dataSet = new StandardPatternDataTable();
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

        Builder builder = Vector.newBuilder();
        for (StandardPattern pattern : dataSet) {
            neuralNetwork.evaluatePattern(pattern);
            Layer layer = neuralNetwork.getArchitecture().getLayers().get(hiddenLayerNumber);
            Layer prevLayer = neuralNetwork.getArchitecture().getLayers().get(hiddenLayerNumber - 1);
            for(Neuron neuron : layer) {
                if(neuron.isBias()) continue;
                else {
                    Vector weights = neuron.getWeights();
                    double netInput = 0;
                    for (int i = 0; i < neuron.getNumWeights(); i++) {
                        netInput += weights.doubleValueOf(i) * prevLayer.getNeuralInput(i);
                    }
                    builder.add(Real.valueOf(netInput));
                }
            }
        }
            
        return builder.build();
    }

    public void setHiddenLayerNumber(int hiddenLayerNumber) {
        this.hiddenLayerNumber = hiddenLayerNumber;
    }
    
}
