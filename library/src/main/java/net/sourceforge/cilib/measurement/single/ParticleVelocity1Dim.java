/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.Vector.Builder;

/**
 * This measurement only works for PSO. Simply find the Entity with the Best person best fitness in the population without re-calculating the fitness.
 */
public class ParticleVelocity1Dim implements Measurement {

    private static final long serialVersionUID = 6502384299554109943L;

    /**
     * {@inheritDoc}
     */
    @Override
    public ParticleVelocity1Dim getClone() {
        return this;
    }

    /**
     * Get particle properties - but for one dimension only, one algo, one particle!
     */
    @Override
    public Vector getValue(Algorithm algorithm) {
        Builder builder = Vector.newBuilder(); 
        
        for(Entity entity : ((PopulationBasedAlgorithm)algorithm).getTopology()) {
            Particle e = (Particle) entity;
            builder.add(((Vector)e.getLocalGuide()).get(0).doubleValue());
            builder.add(((Vector)e.getGlobalGuide()).get(0).doubleValue());
            builder.add(((Vector)e.getPosition()).get(0).doubleValue());               
            builder.add(((Vector)e.getVelocity()).get(0).doubleValue());
        }
        /*Particle e = (Particle)currentAlgorithm.getTopology().get(0);
        builder.add(((Vector)e.getLocalGuide()).get(0).doubleValue());
        builder.add(((Vector)e.getGlobalGuide()).get(0).doubleValue());               
        Vector velocity = (Vector)e.getProperties().get(EntityType.Particle.VELOCITY);
        builder.add(velocity.get(0));
        builder.add(((Vector)e.getPosition()).get(0).doubleValue());*/
        return builder.build();
    }
    
}
