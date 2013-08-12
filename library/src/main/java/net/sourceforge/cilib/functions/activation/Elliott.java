/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.functions.activation;

import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Elliott Function. An efficient alternative to TanH, works in the [-1,1] domain, has [-5,5] active range.
 *
 */
public class Elliott implements ActivationFunction {

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
        return -5;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getUpperActiveRange() {
        return 5;
    }
}
