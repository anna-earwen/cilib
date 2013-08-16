/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import java.util.ArrayList;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.Vector.Builder;

/**
 * Generates NN time series prediction data for networks with one-step-ahead prediction
 */
public class NNTimeSeriesMultiplePredictionInput implements Measurement {

    private static final long serialVersionUID = -1014032196750640716L;
    private int n = 1; // specifies the extent of multivariate prediction. Can't be bigger than embedding.
    private int m = 0; // number of output (starts from 0)
    private int extraSteps = 0;
    private ProbabilityDistributionFunction randomNumber;
    private double variance;
    private Boolean noisy = false;
    
    public NNTimeSeriesMultiplePredictionInput() {        
        randomNumber = new GaussianDistribution();
        variance = 0.5;
    }
    
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
        ArrayList<Numeric> predicted = new ArrayList<Numeric>();//Vector.newBuilder();
        for(int i = 0; i < n; i++) { // predict n values
            StandardPattern pattern = dataSet.getRow(i);
            Vector inputs = pattern.getVector();
            //System.out.println("Original Inputs: "+inputs.toString());
            for(int j = 0; j <predicted.size(); j++ ) { // substitute predicted values into the pattern
                double predVal = predicted.get(j).doubleValue();
                if(noisy) {
                    predVal += randomNumber.getRandomNumber(0.0, variance);
                }
                inputs.setReal(pattern.getVector().size() - predicted.size() + j, predVal);
            }
            //System.out.println("Inputs with replaced items: "+inputs.toString());
            pattern.setVector(inputs);
            builder.add(inputs.get(m));         
            Vector prediction = neuralNetwork.evaluatePattern(pattern);
            for(Numeric value : prediction) {
                predicted.add(value);
            }
        }
        // Now that n predictions are gathered, do the cycle
        for (StandardPattern pattern : dataSet) {
            // set the input vector
            Vector inputs = pattern.getVector();
            //System.out.println("Original Inputs: "+inputs.toString());
            for(int j = 0; j < n; j++ ) { // substitute predicted values into the pattern
                double predVal = predicted.get(j).doubleValue();
                if(noisy) {
                    predVal += randomNumber.getRandomNumber(0.0, variance);
                }
                inputs.setReal(pattern.getVector().size() - n + j, predVal);
            }
            //System.out.println("Inputs with replaced items: "+inputs.toString());
            // get the prediction
            //System.out.println("Inputs: "+inputs.toString());
            pattern.setVector(inputs);
            builder.add(inputs.get(m));  
            Real prediction = (Real)neuralNetwork.evaluatePattern(pattern).get(0);
            // update the "predicted" array
            for(int i = 0; i < n - 1; i++) {
                predicted.set(i, predicted.get(i+1));
            }
            predicted.set(n - 1, prediction);
        }
        // TODO: add the "extra steps" code!
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

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public ProbabilityDistributionFunction getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(ProbabilityDistributionFunction randomNumber) {
        this.randomNumber = randomNumber;
    }

    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }

    public Boolean getNoisy() {
        return noisy;
    }

    public void setNoisy(Boolean noisy) {
        this.noisy = noisy;
    }

}
