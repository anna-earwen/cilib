/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */

package net.sourceforge.cilib.nn.penalty;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * 
 * @author anna
 */
public class NonZeroWeightPenalty extends NNPenalty {

    public NonZeroWeightPenalty() {
        lambda = ConstantControlParameter.of(1e-5);
    }
    
    @Override
    public double calculatePenalty(Type solution) {
        Vector weights = (Vector) solution;
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
