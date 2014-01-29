/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */

package net.sourceforge.cilib.nn.penalty;

import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * 
 * @author anna
 */
public class NonZeroWeightPenalty extends NNPenalty {

    @Override
    public double calculatePenalty(NeuralNetwork neuralNetwork) {
        Vector weights = neuralNetwork.getWeights();
        int numNonZeroWeights = 0;
        for(Numeric weight : weights) {
            if(Math.abs(weight.doubleValue()) > Maths.EPSILON) {
                numNonZeroWeights++;
            }
        }
        double weightRatio = numNonZeroWeights / (double)weights.size();
        return lambda.getParameter() * weightRatio;
    }

    @Override
    public double calculatePenaltyDerivative(double weight) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
