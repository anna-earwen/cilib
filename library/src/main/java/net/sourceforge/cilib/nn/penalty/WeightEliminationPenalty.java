/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */

package net.sourceforge.cilib.nn.penalty;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * 
 * @author anna
 */
public class WeightEliminationPenalty extends NNPenalty {
    
    protected ControlParameter c;
    
    public WeightEliminationPenalty() {
        c = ConstantControlParameter.of(1);
        lambda = ConstantControlParameter.of(1e-4);
    }
    
    @Override
    public double calculatePenalty(Type solution) {
        Vector weights = (Vector) solution;
        double weightSum = 0;
        for(Numeric weight : weights) {
            double wSquared = Math.pow(weight.doubleValue(),2);
            double cSquared = c.getParameter() * c.getParameter();
            weightSum += wSquared / (wSquared + cSquared);
        }
        return lambda.getParameter() * weightSum;
    }

    public ControlParameter getC() {
        return c;
    }

    public void setC(ControlParameter c) {
        this.c = c;
    }

    @Override
    public double calculatePenaltyDerivative(double weight) {
        double cSquared = c.getParameter() * c.getParameter();
        return lambda.getParameter() * (weight * cSquared) / Math.pow((weight*weight + cSquared),2);
    }
    
}
