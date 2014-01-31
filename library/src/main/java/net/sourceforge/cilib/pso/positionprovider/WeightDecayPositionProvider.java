/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.positionprovider;


import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.pso.particle.DecayingParticle;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

/** Implements weight decay for PSO NN training. To be used with DecayingParticle type particles only (they store personal lambda values)
 */
public class WeightDecayPositionProvider implements PositionProvider {

    private static final long serialVersionUID = -4052606351661988520L;
    private PositionProvider delegate;

    public WeightDecayPositionProvider() {
        this.delegate = new StandardPositionProvider() ;
    }

    public WeightDecayPositionProvider(WeightDecayPositionProvider copy) {
        this();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WeightDecayPositionProvider getClone() {
        return new WeightDecayPositionProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        DecayingParticle decayingParticle = (DecayingParticle)particle;

        if(((Fitness)particle.getProperties().get(EntityType.PREVIOUS_FITNESS)).getValue()
                .compareTo(((Fitness)particle.getProperties().get(EntityType.FITNESS)).getValue()) > 0) { // error decreases

            decayingParticle.setLambda(ConstantControlParameter.of(decayingParticle.getLambda().getParameter() + 1e-3));
        } else if(((Fitness)particle.getProperties().get(EntityType.PREVIOUS_FITNESS)).getValue()
               .compareTo(((Fitness)particle.getProperties().get(EntityType.FITNESS)).getValue()) < 0) { // error increases
            
            decayingParticle.setLambda(ConstantControlParameter.of(decayingParticle.getLambda().getParameter() - 1e-3));
        }
        Vector pos = delegate.get(particle);
        Vector decayedPos = Vector.copyOf(pos).multiply(decayingParticle.getLambda().getParameter());
        return pos.subtract(decayedPos);
    }

    public PositionProvider getDelegate() {
        return delegate;
    }

    public void setDelegate(PositionProvider delegate) {
        this.delegate = delegate;
    }
}
