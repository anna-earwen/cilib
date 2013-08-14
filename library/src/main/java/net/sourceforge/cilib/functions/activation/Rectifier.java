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
 * The rectifier activation function, f(x) = max(0, x); f '(x) = 1 if x > 0, 0 otherwise; 
 * Linear after 0, flat before 0. Since it is unbounded, the linear function has 
 * no active range, and these values are set to positive and negative max double.
 */
public class Rectifier implements ActivationFunction {

    private static final long serialVersionUID = -6826800182176063079L;

    @Override
    public Object getClone() {
        return this;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Real apply(Real input) {
        return input;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double apply(double input) {
        return Math.max(0, input);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getGradient(Vector x) {
        return Vector.of(getGradient(x.get(0).doubleValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getGradient(double number) {
        if(number > 0) {
            return 1.0;
        }
        else {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getLowerActiveRange() {
        return -Double.MAX_VALUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getUpperActiveRange() {
        return Double.MAX_VALUE;
    }
}
