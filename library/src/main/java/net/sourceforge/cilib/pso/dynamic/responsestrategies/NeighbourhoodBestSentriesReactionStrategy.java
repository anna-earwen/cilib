/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;

public class NeighbourhoodBestSentriesReactionStrategy<E extends PopulationBasedAlgorithm> extends EnvironmentChangeResponseStrategy<E> {
    private static final long serialVersionUID = -2142727048293776335L;

    public NeighbourhoodBestSentriesReactionStrategy() {
        // super() is called automatically
    }

    public NeighbourhoodBestSentriesReactionStrategy(NeighbourhoodBestSentriesReactionStrategy<E> rhs) {
        super(rhs);
    }

    @Override
    public NeighbourhoodBestSentriesReactionStrategy<E> getClone() {
        return new NeighbourhoodBestSentriesReactionStrategy<E>(this);
    }

    @Override
    public void performReaction(PopulationBasedAlgorithm algorithm) {
        RandomProvider random = new MersenneTwister();

        for (Entity entity : Topologies.getNeighbourhoodBestEntities(algorithm.getTopology())) {
            entity.getCandidateSolution().randomize(random);
            // TODO: What is the influence of reevaluation?
//            entity.calculateFitness(false);
        }
    }
}