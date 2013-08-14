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
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.Vector.Builder;

/**
 * This measurement only works for PSO. Simply find the Entity with the Best person best fitness in the population without re-calculating the fitness.
 */
public class CooperativeAverageDistanceFromOrigin implements Measurement {

    private static final long serialVersionUID = 6502384299554109943L;

    /**
     * {@inheritDoc}
     */
    @Override
    public CooperativeAverageDistanceFromOrigin getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getValue(Algorithm algorithm) {
        Builder builder = Vector.newBuilder(); 
        MultiPopulationBasedAlgorithm ca = (MultiPopulationBasedAlgorithm) algorithm;
        for (SinglePopulationBasedAlgorithm<Entity> currentAlgorithm : ca) {
            int dimension = currentAlgorithm.getTopology().last().getDimension();
            Vector averagePosition = Vector.of();//new Numeric[dimension]);
            for (Entity e : currentAlgorithm.getTopology()) {
                Vector position = (Vector)e.getCandidateSolution();
                for(int i = 0; i < dimension; i++) {
                    if(averagePosition.size() < dimension) {
                        averagePosition.add(Real.valueOf(Math.abs(position.doubleValueOf(i))));
                    } else {
                    averagePosition.set(i, Real.valueOf(averagePosition.doubleValueOf(i) + Math.abs(position.doubleValueOf(i))));
                    }
                }
            }
            for(int i = 0; i < dimension; i++) {
                averagePosition.set(i, Real.valueOf(averagePosition.doubleValueOf(i)/(double)dimension));
            }
            builder.copyOf(averagePosition);
        }
        return builder.build();
        
    }
    
}
