/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.particle;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.pso.dynamic.ChargedParticle;
import net.sourceforge.cilib.pso.positionprovider.WeightDecayPositionProvider;

public class DecayingParticle extends ChargedParticle {
    private ControlParameter lambda;

    public DecayingParticle() {
        this.behavior.setPositionProvider(new WeightDecayPositionProvider());
        this.lambda = ConstantControlParameter.of(5e-6);
    }

     public DecayingParticle(DecayingParticle copy) {
        super(copy);
        this.lambda = copy.getLambda().getClone();
    }

    @Override
    public DecayingParticle getClone() {
        return new DecayingParticle(this);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if ((object == null) || (this.getClass() != object.getClass())) {
            return false;
        }

        DecayingParticle other = (DecayingParticle) object;
        return super.equals(object)
                && (Double.valueOf(this.lambda.getParameter()).equals(Double.valueOf(other.lambda.getParameter())));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + super.hashCode();
        hash = 31 * hash + Double.valueOf(lambda.getParameter()).hashCode();
        return hash;
    }

    public ControlParameter getLambda() {
        return lambda;
    }

    public void setLambda(ControlParameter lambda) {
        this.lambda = lambda;
    }
}
