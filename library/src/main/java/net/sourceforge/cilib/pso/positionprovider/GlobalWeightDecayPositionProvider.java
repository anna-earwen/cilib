/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.positionprovider;


import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.container.Vector;

/** Implementation of weight decay for PSO NN training, single lambda coefficient for entire swarm.
 */
public class GlobalWeightDecayPositionProvider implements PositionProvider {

    private static final long serialVersionUID = -4052606351661988520L;
    private ControlParameter lambda;
    private Fitness previousFitness;
    private PositionProvider delegate;
    private int previousIteration;

    public GlobalWeightDecayPositionProvider() {
        this.delegate = new StandardPositionProvider() ;
        this.lambda = ConstantControlParameter.of(5e-6);
        this.previousFitness = InferiorFitness.instance();
        this.previousIteration = -1;
    }

    public GlobalWeightDecayPositionProvider(GlobalWeightDecayPositionProvider copy) {
        this();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GlobalWeightDecayPositionProvider getClone() {
        return new GlobalWeightDecayPositionProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        int currentIteration = AbstractAlgorithm.get().getIterations();
        if(previousIteration != currentIteration) {
            previousIteration = currentIteration;
            PSO pso = (PSO) AbstractAlgorithm.get();

            if(this.previousFitness.compareTo(InferiorFitness.instance()) == 0) {
                previousFitness = pso.getBestSolution().getFitness();
            }
            else if(this.previousFitness.compareTo(pso.getBestSolution().getFitness()) > 0) { // error increases
                this.lambda = ConstantControlParameter.of(this.lambda.getParameter() - 1e-3);
            }
            else if (this.previousFitness.compareTo(pso.getBestSolution().getFitness()) < 0) { // error decreases
                this.lambda = ConstantControlParameter.of(this.lambda.getParameter() + 1e-3);
            }
        }
        return delegate.get(particle).multiply(this.lambda.getParameter());
    }

    public PositionProvider getDelegate() {
        return delegate;
    }

    public void setDelegate(PositionProvider delegate) {
        this.delegate = delegate;
    }

    public ControlParameter getLambda() {
        return lambda;
    }

    public void setLambda(ControlParameter lambda) {
        this.lambda = lambda;
    }
}
