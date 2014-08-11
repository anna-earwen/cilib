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
 * Logarithmic Function. Supposed to aid with hidden unit saturation.
 *
 */
public class Logarithmic extends ActivationFunction {

    private static final long serialVersionUID = -5843046986587459333L;

    @Override
    public Logarithmic getClone() {
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
    
    @Override
    public double f(double input) {
        if(input >=0 ) {
            return Math.log(1 + input);
        } else {
            return -Math.log(1 - input);
        }
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
    	if (number >= 0) {
            return 1 / (1 + number);
        } else {
            return 1 / (1 - number);
        }
    }

    /**
     * {@inheritDoc}
     * The active range is -5 - 5
     */
    @Override
    public double getLowerActiveRange() {
        return -2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getUpperActiveRange() {
        return 2;
    }
    
    @Override
    public Bounds getBounds() {
        return new Bounds(-1,1);
    }   
}
