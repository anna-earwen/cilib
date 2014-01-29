/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */

package net.sourceforge.cilib.nn.penalty;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * 
 * @author anna
 */
public class WeightEliminationPenalty extends NNPenalty {
    
    protected ControlParameter c;
    
    public WeightEliminationPenalty() {
        c = ConstantControlParameter.of(0.2);
        lambda = ConstantControlParameter.of(1e-2);
    }
    
    @Override
    public double calculatePenalty(NeuralNetwork neuralNetwork) {
        Vector weights = neuralNetwork.getWeights();
        double weightSum = 0;
        for(Numeric weight : weights) {
            double wSquared = Math.pow(weight.doubleValue(),2);
            weightSum += wSquared / (wSquared + c.getParameter() * c.getParameter());
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
