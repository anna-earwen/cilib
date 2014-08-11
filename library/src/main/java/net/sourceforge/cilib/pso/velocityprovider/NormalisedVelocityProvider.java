/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 */
public class NormalisedVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = -5995116445841750100L;

    private VelocityProvider delegate;

    public NormalisedVelocityProvider() {
        this(new StandardVelocityProvider());
    }

    public NormalisedVelocityProvider(VelocityProvider delegate) {
        this.delegate = delegate;
    }

    public NormalisedVelocityProvider(NormalisedVelocityProvider copy) {
        this.delegate = copy.delegate.getClone();
    }

    @Override
    public NormalisedVelocityProvider getClone() {
        return new NormalisedVelocityProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        Vector velocity = this.delegate.get(particle);
        return velocity.normalize();
    }

    public void setDelegate(VelocityProvider delegate) {
        this.delegate = delegate;
    }

    public VelocityProvider getDelegate() {
        return this.delegate;
    }
}
