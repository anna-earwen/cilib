/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.activation;

import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Scaled Hyperbolic Tangent Function, from LeCun.
 *
 */
public class ScaledTanH implements ActivationFunction {

    private static final long serialVersionUID = -5843046986587459333L;

    @Override
    public Object getClone() {
        return this;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Real apply(Real input) {
        return Real.valueOf(apply(input.doubleValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double apply(double input) {
        return 1.7159 * Math.tanh(0.6666 * input);
    }

    @Override
    public Vector getGradient(Vector x) {
        return Vector.of(this.getGradient(x.doubleValueOf(0)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getGradient(double number) {
        return 1.14381894 - 0.50826231 * number * number;
    }

    /**
     * {@inheritDoc}
     * The active range is -Sqrt(3) - Sqrt(3), and Sqrt(3) = 1.732050808
     */
    @Override
    public double getLowerActiveRange() {
        return -1.732050808;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getUpperActiveRange() {
        return 1.732050808;
    }
}
