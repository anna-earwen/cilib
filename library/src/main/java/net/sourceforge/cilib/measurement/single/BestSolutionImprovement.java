/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;

/**
 * Calculates the average number of personal best positions in
 * the current swarm that violates boundary constraints.
 *
 */
public class BestSolutionImprovement implements Measurement<Real> {

    private static final long serialVersionUID = 7547646366505677446L;
    private Double previousBestSolutionFitness;

    public BestSolutionImprovement() {
        this.previousBestSolutionFitness = Double.NaN;
    }

    public BestSolutionImprovement(BestSolutionImprovement copy) {
        this.previousBestSolutionFitness = copy.previousBestSolutionFitness;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public BestSolutionImprovement getClone() {
        return new BestSolutionImprovement();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        double currentBestSolutionFitness = algorithm.getBestSolution().getFitness().getValue();
        double difference = currentBestSolutionFitness;
        if(!previousBestSolutionFitness.isNaN()) {
            difference = previousBestSolutionFitness - currentBestSolutionFitness; // signed on purpose! negative sign indicates worsened fitness
        }
        //System.out.println("Curr fitness: "+currentBestSolutionFitness+", Prev fitness: "+previousBestSolutionFitness);
        previousBestSolutionFitness = currentBestSolutionFitness;
        return Real.valueOf(difference);
    }
}
