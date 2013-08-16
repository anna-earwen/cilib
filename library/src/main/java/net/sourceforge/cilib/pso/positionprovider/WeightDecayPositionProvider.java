/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.positionprovider;


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

        //System.out.println("Previous: "+((Fitness)particle.getProperties().get(EntityType.PREVIOUS_FITNESS)).getValue());
        //System.out.println("Current: "+((Fitness)particle.getProperties().get(EntityType.FITNESS)).getValue());
        if(((Fitness)particle.getProperties().get(EntityType.PREVIOUS_FITNESS)).getValue()
                .compareTo(((Fitness)particle.getProperties().get(EntityType.FITNESS)).getValue()) > 0) { // error decreases
            
          //   System.out.println("Error decrease!");
        //System.out.println("New position: " + delegate.get(particle));
        return delegate.get(particle);
             /*System.out.println("Error decrease! New lambda:" + (decayingParticle.getLambda().getParameter() + 1e-3));
            decayingParticle.setLambda(ConstantControlParameter.of(decayingParticle.getLambda().getParameter() + 1e-3));
        System.out.println("New position: " + delegate.get(particle).multiply(decayingParticle.getLambda().getParameter()).toString());
            return delegate.get(particle).multiply(decayingParticle.getLambda().getParameter());*/
        }
        else //if(((Fitness)particle.getProperties().get(EntityType.PREVIOUS_FITNESS)).getValue()
               // .compareTo(((Fitness)particle.getProperties().get(EntityType.FITNESS)).getValue()) <= 0) { // error increases
          //   System.out.println("Error increase... New lambda:" + (decayingParticle.getLambda().getParameter() * 1.3));
            //decayingParticle.setLambda(ConstantControlParameter.of(decayingParticle.getLambda().getParameter() * 1.3));
        {
            return delegate.get(particle).multiply(decayingParticle.getLambda().getParameter());

        }
        //System.out.println("Stagnation! New lambda:" + (Math.abs(decayingParticle.getLambda().getParameter()) * 1.3));
        //System.out.println("New position: " + delegate.get(particle).multiply(decayingParticle.getLambda().getParameter()).toString());
            //return delegate.get(particle).multiply(decayingParticle.getLambda().getParameter());
    }

    public PositionProvider getDelegate() {
        return delegate;
    }

    public void setDelegate(PositionProvider delegate) {
        this.delegate = delegate;
    }
}
