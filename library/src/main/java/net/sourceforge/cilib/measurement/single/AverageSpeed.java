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
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.Vector.Builder;

/**
 * This measurement only works for PSO. Simply find the Entity with the Best person best fitness in the population without re-calculating the fitness.
 */
public class AverageSpeed implements Measurement {

    private static final long serialVersionUID = 6502384299554109943L;

    /**
     * {@inheritDoc}
     */
    @Override
    public AverageSpeed getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getValue(Algorithm algorithm) {
        Builder builder = Vector.newBuilder(); 
        SinglePopulationBasedAlgorithm<Entity> currentAlgorithm = (SinglePopulationBasedAlgorithm) algorithm;
        int dimension = currentAlgorithm.getTopology().last().getDimension();
        Vector averageSpeed = Vector.of();//new Numeric[dimension]);
        for (Entity e : currentAlgorithm.getTopology()) {
            Vector velocity = (Vector)e.getProperties().get(EntityType.Particle.VELOCITY);
            for(int i = 0; i < dimension; i++) {
                if(averageSpeed.size() < dimension) {
                    averageSpeed.add(Real.valueOf(Math.abs(velocity.doubleValueOf(i))));
                } else {
                averageSpeed.set(i, Real.valueOf(averageSpeed.doubleValueOf(i) + Math.abs(velocity.doubleValueOf(i))));
                }
            }
        }
        for(int i = 0; i < dimension; i++) {
            averageSpeed.set(i, Real.valueOf(averageSpeed.doubleValueOf(i)/(double)dimension));
        }
        builder.copyOf(averageSpeed);
        
        return builder.build();
        
    }
    
}
