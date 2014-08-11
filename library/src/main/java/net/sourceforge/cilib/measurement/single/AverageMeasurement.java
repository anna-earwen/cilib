/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.Vector.Builder;

/**
 * Measurement to perform measurements on a set of contained {@code Algorithm}
 * instances, and return an average of the obtained values. 
 * This type of measurement is generally only defined for
 * {@link net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm}.
 */
public class AverageMeasurement implements Measurement {

    private static final long serialVersionUID = -7109719897119621328L;
    private List<Measurement<? extends Type>> measurements;

    /**
     * Create a new instance with zero measurements.
     */
    public AverageMeasurement() {
        this.measurements = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AverageMeasurement getClone() {
        AverageMeasurement newCM = new AverageMeasurement();

        for(Measurement<? extends Type> m : this.measurements) {
            newCM.addMeasurement(m.getClone());
        }

        return newCM;
    }

    /**
     * Get the measurement values for all sub-algorithms.
     * @param algorithm The top level algorithm
     * @return The values of measurements applied to all contained algorithms.
     */
    @Override
    public Type getValue(Algorithm algorithm) {
        
        Builder mBuilder = Vector.newBuilder();
        
        for (Measurement measurement : measurements) {
            mBuilder.addAll((Vector)measurement.getValue(algorithm));
        }
        
        Vector ms = mBuilder.build();
        
        double average = 0;
        for(Numeric element : ms) {
            average += element.doubleValue();
        }

        return Real.valueOf(average / ms.size());
    }

    /**
     * Add a measurement to the composite for evaluation on the sub-algorithms.
     * @param measurement The measurement to add.
     */
    public void addMeasurement(Measurement<? extends Type> measurement) {
        this.measurements.add(measurement);
    }
}
