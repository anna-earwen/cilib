/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.activation;

import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Hyperbolic Tangent Function.
 *
 */
public class Exponent extends ActivationFunction {

    private static final long serialVersionUID = -5843046986587459333L;

    @Override
    public Exponent getClone() {
        return this;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Real f(Real input) {
        return Real.valueOf(f(input.doubleValue()));
    }

    /**
     * {@inheritDoc}
     */

    public double f(double input) {
        return Math.exp(input);
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
        return Math.exp(number);
    }

    /**
     * {@inheritDoc}
     * The active range is -Sqrt(3) - Sqrt(3), and Sqrt(3) = 1.732050808
     */
    @Override
    public double getLowerActiveRange() {
        return Double.MIN_VALUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getUpperActiveRange() {
        return Double.MAX_VALUE;
    }
    
    @Override
    public Bounds getBounds() {
        return new Bounds(0,Double.MAX_VALUE);
    }   
}
