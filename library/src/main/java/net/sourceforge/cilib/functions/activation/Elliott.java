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
 * Elliott Function. An efficient alternative to TanH, works in the [-1,1] domain, has [-2,2] active range.
 *
 */
public class Elliott extends ActivationFunction {

    private static final long serialVersionUID = -5843046986587459333L;

    @Override
    public Elliott getClone() {
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
        return input / (1.0 + (Math.abs(input)));
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
    	double d = 1.0 + Math.abs(number);//Derivative is: 1/((1+|x|)*(1+|x|)).
    	return  1.0/(d * d);
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
