/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.multiple;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.Vector.Builder;

/**
 * This measurement only works for PSO. Simply find the Entity with the Best person best fitness in the population without re-calculating the fitness.
 */
public class CooperativeAverageSpeed implements Measurement {

    private static final long serialVersionUID = 6502384299554109943L;

    /**
     * {@inheritDoc}
     */
    @Override
    public CooperativeAverageSpeed getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getValue(Algorithm algorithm) {
        Builder builder = Vector.newBuilder(); 
        MultiPopulationBasedAlgorithm ca = (MultiPopulationBasedAlgorithm) algorithm;
        for (SinglePopulationBasedAlgorithm<Entity> currentAlgorithm : ca.getPopulations()) {
            int dimension = currentAlgorithm.getTopology().last().getDimension();
            Vector averageVelocity = Vector.of();//new Numeric[dimension]);
            for (Entity e : currentAlgorithm.getTopology()) {
                Vector velocity = (Vector)e.getProperties().get(EntityType.Particle.VELOCITY);
                for(int i = 0; i < dimension; i++) {
                    if(averageVelocity.size() < dimension) {
                        averageVelocity.add(Real.valueOf(Math.abs(velocity.doubleValueOf(i))));
                    } else {
                    averageVelocity.set(i, Real.valueOf(averageVelocity.doubleValueOf(i) + Math.abs(velocity.doubleValueOf(i))));
                    }
                }
            }
            for(int i = 0; i < dimension; i++) {
                averageVelocity.set(i, Real.valueOf(averageVelocity.doubleValueOf(i)/(double)dimension));
            }
            builder.copyOf(averageVelocity);
        }
        return builder.build();
        
    }
    
}
