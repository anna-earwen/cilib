/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.measurement.single;

import java.util.ArrayList;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.problem.nn.NNDataTrainingProblem;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.Vector.Builder;

/**
 * Calculates the MSE generalization error of the best solution of an algorithm
 * optimizing a {@link NNDataTrainingProblem}.
 */
public class NNTimeSeriesMultiplePrediction implements Measurement {

    private static final long serialVersionUID = -1014032196750640716L;
     private int tau = 1; // specifies the time lag between observations
    private int step = 1; // specifies the distance between patterns
    private int n = 1; // specifies the extent of multivariate prediction. Can't be bigger than embedding.
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
        StandardPatternDataTable generalizationSet = problem.getGeneralizationSet();
        StandardPatternDataTable validationSet = problem.getValidationSet();
        for (StandardPattern pattern : trainingSet) {
            dataSet.addRow(pattern);
        }
        for (StandardPattern pattern : generalizationSet) {
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
            for(int j = 0; j <predicted.size(); j++ ) { // substitute predicted values into the pattern
                inputs.setReal(pattern.getVector().size() - predicted.size() + j, predicted.get(j).doubleValue());
            }
            //System.out.println("Original Inputs: "+inputs.toString());
            pattern.setVector(inputs);
            Vector prediction = neuralNetwork.evaluatePattern(pattern);
            for(Numeric value : prediction) {
                predicted.add(value);
                builder.add(value);
            }
        }
        // Now that n predictions are gathered, do the cycle
        for (StandardPattern pattern : dataSet) {
            // set the input vector
            Vector inputs = pattern.getVector();
            for(int j = 0; j < n; j++ ) { // substitute predicted values into the pattern
                inputs.setReal(pattern.getVector().size() - n + j, predicted.get(j).doubleValue());
            }
            // get the prediction
            //System.out.println("Inputs: "+inputs.toString());
            pattern.setVector(inputs);
            Real prediction = (Real)neuralNetwork.evaluatePattern(pattern).get(0);
            // update the "predicted" array
            for(int i = 0; i < n - 1; i++) {
                predicted.set(i, predicted.get(i+1));
            }
            predicted.set(n - 1, prediction);
            // add to the resulting vector
            builder.add(prediction);
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

}
