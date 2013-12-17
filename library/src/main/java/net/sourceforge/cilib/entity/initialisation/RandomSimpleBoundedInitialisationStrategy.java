/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.initialisation;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @param <E> The entity type.
 */
public class RandomSimpleBoundedInitializationStrategy<E extends Entity> implements
        InitialisationStrategy<E> {

    private static final long serialVersionUID = -7926839076670354209L;
    protected ControlParameter lowerBound;
    protected ControlParameter upperBound;
    private ProbabilityDistributionFunction random;

    public RandomSimpleBoundedInitializationStrategy() {
        this.lowerBound = ConstantControlParameter.of(0.1);
        this.upperBound = ConstantControlParameter.of(0.1);
        this.random = new UniformDistribution();
    }

    public RandomSimpleBoundedInitializationStrategy(RandomSimpleBoundedInitializationStrategy copy) {
        this.lowerBound = copy.lowerBound;
        this.upperBound = copy.upperBound;
        this.random = copy.random;
    }

    @Override
    public RandomSimpleBoundedInitializationStrategy getClone() {
        return new RandomSimpleBoundedInitializationStrategy(this);
    }

    @Override
    public void initialise(Enum<?> key, E entity) {
        Type type = entity.getProperties().get(key);
        Vector entityVector = (Vector) type;
        
        for (int i = 0; i < entityVector.size(); i++) {
            entityVector.setReal(i, random.getRandomNumber(lowerBound.getParameter(), upperBound.getParameter()));
        }
    }

    
    public ControlParameter getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(ControlParameter lowerBound) {
        this.lowerBound = lowerBound;
    }

    public ControlParameter getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(ControlParameter upperBound) {
        this.upperBound = upperBound;
    }

    public ProbabilityDistributionFunction getRandom() {
        return random;
    }

    public void setRandom(ProbabilityDistributionFunction random) {
        this.random = random;
    }
    
}
