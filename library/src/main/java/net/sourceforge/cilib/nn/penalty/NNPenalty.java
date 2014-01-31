/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */

package net.sourceforge.cilib.nn.penalty;

import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.nn.NeuralNetwork;

/**
 *
 * @author anna
 */
public abstract class NNPenalty {
    /**
     * Calculate penalty function according to concrete penalty strategy
     * @param nn
     * @return
     */
    protected ControlParameter lambda;
    
    public abstract double calculatePenalty(NeuralNetwork nn);
    public abstract double calculatePenaltyDerivative(double weight);    

    public ControlParameter getLambda() {
        return lambda;
    }

    public void setLambda(ControlParameter lambda) {
        this.lambda = lambda;
    }
}
