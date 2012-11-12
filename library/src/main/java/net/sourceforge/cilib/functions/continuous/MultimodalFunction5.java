/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Minimum: 0.0
 * R(-6, 6)^2
 */
public class MultimodalFunction5 implements ContinuousFunction {

    private static final long serialVersionUID = -8704025552791904890L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double x = input.doubleValueOf(0);
        double y = input.doubleValueOf(1);
        return 200 - Math.pow((x*x + y - 11), 2) - Math.pow((x + y*y - 7), 2);
    }
}
