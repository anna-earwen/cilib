/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */

package net.sourceforge.cilib.nn.penalty;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * 
 * @author anna
 */
public class WeightDecayPenalty extends NNPenalty {

    public WeightDecayPenalty() {
        lambda = ConstantControlParameter.of(1e-5);
    }
    
    @Override
    public double calculatePenalty(Type solution) {
        Vector weights = (Vector) solution;
        double weightSum = 0;
        for(Numeric weight : weights) {
            weightSum += Math.pow(weight.doubleValue(),2);
        }
        return lambda.getParameter() * weightSum / 2;
    }

    @Override
    public double calculatePenaltyDerivative(double weight) {
        return weight*lambda.getParameter();
    }
    
}
