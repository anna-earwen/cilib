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
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Type;

/**
 * Calculates the average number of personal best positions in
 * the current swarm that violates boundary constraints.
 *
 */
public class GBestChange implements Measurement {

    private static final long serialVersionUID = 7547646366505677446L;
    private BestSolutionImprovement bestSolutionImprovement;
    private double epsilon = 0.000001;

    public GBestChange() {
        this.bestSolutionImprovement = new BestSolutionImprovement();
    }
    
    public GBestChange(int iterationThreshold, double epsilon) {
        this.epsilon = epsilon;
    }

    public GBestChange(GBestChange copy) {
        this.bestSolutionImprovement = copy.bestSolutionImprovement.getClone();
        this.epsilon = copy.epsilon;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public GBestChange getClone() {
        return new GBestChange(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getValue(Algorithm algorithm) {
        if(Math.abs(bestSolutionImprovement.getValue(algorithm).doubleValue()) > epsilon) {
            return Int.valueOf(1);
        }
        else {
            return Int.valueOf(0);
        }
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }
}
