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
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.Vector.Builder;

/**
 * Calculates the NN output of the best solution of an algorithm
 * optimizing a {@link NNDataTrainingProblem}, for the entire data set.
 */
public class NNActivationDifference implements Measurement {

    private static final long serialVersionUID = -1014032196750640716L;
    private int layerNumber = 1;
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
        Layer layer = neuralNetwork.getArchitecture().getLayers().get(layerNumber);
        int numNeurons = layer.size();//getActivations();
        
        if(layer.isBias()) {
            numNeurons--;
        }// exclude bias
        
        Vector diffSums = Vector.of();
        Vector prevActivations = Vector.of();
        
        for (StandardPattern pattern : dataSet) 
        {
            neuralNetwork.evaluatePattern(pattern);
            Vector outs = layer.getActivations();
            if(prevActivations.isEmpty()) 
            {
                Builder prevActivationsBuilder = Vector.newBuilder();
                for(int j = 0; j < numNeurons; j++) //for(Numeric out : outs) 
                { // exclude bias
                    prevActivationsBuilder.add(outs.doubleValueOf(j)); // store diff
                }
                prevActivations = prevActivationsBuilder.build();
            }
            else // previous values exist
            {
                if(diffSums.isEmpty())
                {                        
                    Builder diffSumsBuilder = Vector.newBuilder();
                    for(int j = 0; j < numNeurons; j++) //for(Numeric out : outs) 
                    { 
                        double diff = prevActivations.get(j).doubleValue() - outs.doubleValueOf(j);
                        diffSumsBuilder.add(diff * diff);
                    }
                    diffSums = diffSumsBuilder.build();
                }
                else // previous diffs exist
                {
                    for(int j = 0; j < numNeurons; j++) //for(Numeric out : outs) 
                    { 
                        double diff = prevActivations.get(j).doubleValue() - outs.doubleValueOf(j);
                        diffSums.setReal(j, diffSums.doubleValueOf(j) + (diff * diff));
                        prevActivations.setReal(j,outs.doubleValueOf(j));
                    }
                }
            }
        }
            
        for(int i = 0; i < numNeurons; i++)
        {
            diffSums.setReal(i, diffSums.doubleValueOf(i) / dataSet.size());
        }
        
        return diffSums;
    }

    public void setLayerNumber(int layerNumber) {
        this.layerNumber = layerNumber;
    }
    
}
