/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.nn.NNRegularisationDecorator;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 */
public class NNPenaltyValue implements Measurement {

    private static final long serialVersionUID = -1014032196750640716L;

    protected double outputSensitivityThreshold = 0.2;

    /**
     * {@inheritDoc }
     */
    @Override
    public Measurement getClone() {
        return this;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Type getValue(Algorithm algorithm) {
        Vector solution = (Vector) algorithm.getBestSolution().getPosition();
        NNRegularisationDecorator problem = (NNRegularisationDecorator) algorithm.getOptimisationProblem();
        return Real.valueOf(problem.calculatePenalty(solution));
    }

    public void setOutputSensitivityThreshold(double outputSensitivityThreshold) {
        this.outputSensitivityThreshold = outputSensitivityThreshold;
    }
}
