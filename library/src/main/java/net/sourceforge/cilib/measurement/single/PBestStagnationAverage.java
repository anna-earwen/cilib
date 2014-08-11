/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;

/**
 * This measurement only works for PSO. Simply find the Entity with the Best person best fitness in the population without re-calculating the fitness.
 */
public class PBestStagnationAverage implements Measurement {

    private static final long serialVersionUID = 6502384299554109943L;

    /**
     * {@inheritDoc}
     */
    @Override
    public PBestStagnationAverage getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getValue(Algorithm algorithm) {
        double counterSum = 0;
        SinglePopulationBasedAlgorithm<Entity> ca = (SinglePopulationBasedAlgorithm) algorithm;
        for (Entity e : ca.getTopology()) {
            Int counter = (Int)e.getProperties().get(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER);
            counterSum += counter.intValue();
        }
        return Real.valueOf(counterSum / ca.getTopology().length());
    }
    
}
