/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.nn.penalty;

import net.sourceforge.cilib.nn.NeuralNetwork;

/**
 *
 * @author anna
 */
public class NoPenalty extends NNPenalty {

    @Override
    public double calculatePenalty(NeuralNetwork nn) {
        return 0; // no penalty!
    }

    @Override
    public double calculatePenaltyDerivative(double weight) {
        return 0;
    }
    
}
