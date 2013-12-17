/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Bit;

/**
 * Calculates the average number of personal best positions in
 * the current swarm that violates boundary constraints.
 *
 */
public class ThresholdedStagnationFlag implements Measurement<Bit> {

    private static final long serialVersionUID = 7547646366505677446L;
    private BestSolutionImprovement bestSolutionImprovement;
    private int iterationThreshold = 5;
    private int iterationCount;
    private double epsilon = 0.00001;

    public ThresholdedStagnationFlag() {
        this.bestSolutionImprovement = new BestSolutionImprovement();
    }
    
    public ThresholdedStagnationFlag(int iterationThreshold, double epsilon) {
        this.iterationThreshold = iterationThreshold;
        this.epsilon = epsilon;
    }

    public ThresholdedStagnationFlag(ThresholdedStagnationFlag copy) {
        this.bestSolutionImprovement = copy.bestSolutionImprovement.getClone();
        this.iterationThreshold = copy.iterationThreshold;
        this.epsilon = copy.epsilon;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ThresholdedStagnationFlag getClone() {
        return new ThresholdedStagnationFlag(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bit getValue(Algorithm algorithm) {
        if(bestSolutionImprovement.getValue(algorithm).doubleValue() < epsilon) {
            iterationCount++;
        }
        else {
            iterationCount = 0;
        }
        if(iterationCount >= this.iterationThreshold) {
            iterationCount = 0;
            return Bit.valueOf(true);
        }
        else {
            return Bit.valueOf(false);
        }
    }

    public int getIterationThreshold() {
        return iterationThreshold;
    }

    public void setIterationThreshold(int iterationThreshold) {
        this.iterationThreshold = iterationThreshold;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }
}
