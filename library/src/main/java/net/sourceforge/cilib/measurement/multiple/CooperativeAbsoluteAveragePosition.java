/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.multiple;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.Vector.Builder;

/**
 * This measurement only works for PSO. Simply find the Entity with the Best person best fitness in the population without re-calculating the fitness.
 */
public class CooperativeAbsoluteAveragePosition implements Measurement {

    private static final long serialVersionUID = 6502384299554109943L;

    /**
     * {@inheritDoc}
     */
    @Override
    public CooperativeAbsoluteAveragePosition getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getValue(Algorithm algorithm) {
        Builder builder = Vector.newBuilder(); 
        MultiPopulationBasedAlgorithm ca = (MultiPopulationBasedAlgorithm) algorithm;
        for (PopulationBasedAlgorithm currentAlgorithm : ca.getPopulations()) {
            int dimension = currentAlgorithm.getTopology().get(0).getDimension();
            Vector averagePosition = Vector.of();//new Numeric[dimension]);
            for (Entity e : currentAlgorithm.getTopology()) {
                Vector position = (Vector) e.getCandidateSolution();
                for(int i = 0; i < dimension; i++) {
                    if(averagePosition.size() < dimension) {
                        averagePosition.add(position.get(i));
                    } else {
                    averagePosition.set(i, Real.valueOf(averagePosition.doubleValueOf(i) + position.doubleValueOf(i)));
                    }
                }
            }
            for(int i = 0; i < dimension; i++) {
                averagePosition.set(i, Real.valueOf(Math.abs(averagePosition.doubleValueOf(i)/(double)dimension)));
            }
            builder.copyOf(averagePosition);
        }
        return builder.build();
        
        /*
         * PopulationBasedAlgorithm populationBasedAlgorithm = (PopulationBasedAlgorithm) algorithm;
        int populationSize = populationBasedAlgorithm.getTopology().size();

        int dimensions = 0;
        double sumOfAverageConvergedDimensions = 0.0;

        for (Entity populationEntity : populationBasedAlgorithm.getTopology()) {
            dimensions = populationEntity.getDimension();

            int dimension = 0;
            int numberConvergedDimensions = 0;
            for (Numeric position : (Vector) populationEntity.getCandidateSolution()) {
                double lowerBound = targetSolution.doubleValueOf(dimension) - this.errorThreshold.getParameter();
                double upperBound = targetSolution.doubleValueOf(dimension) + this.errorThreshold.getParameter();
                double value = position.doubleValue();

                if ((value >= lowerBound) && (value <= upperBound)) {
                    numberConvergedDimensions++;
                }
                dimension++;
            }
            sumOfAverageConvergedDimensions += (double) numberConvergedDimensions / (double) dimensions;
        }

        return Real.valueOf(sumOfAverageConvergedDimensions / (double) populationSize * (double) dimensions);
         */
    }
    
}
