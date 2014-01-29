/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.nn;

import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.nn.penalty.NNPenalty;
import net.sourceforge.cilib.nn.penalty.NonZeroWeightPenalty;
import net.sourceforge.cilib.problem.AbstractProblem;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;

/**
 * Abstract class represents an {@link net.sourceforge.cilib.problem.Problem} where the goal is to optimize
 * the set of weights of a neural network. Used as an interface to more specific training problems
 * such as training from a static dataset and sliding window training.
 */
public class NNRegularisationDecorator extends NNTrainingProblem {
    protected NNTrainingProblem neuralNetworkProblem;
    protected NNPenalty penalty;
    /**
     * Default constructor.
     */
    public NNRegularisationDecorator() {
        super();
        neuralNetworkProblem = new NNEmptyTrainingProblem();
        penalty = new NonZeroWeightPenalty();
    }

    @Override
    public void initialise() {
        neuralNetworkProblem.initialise();
        domainRegistry = neuralNetworkProblem.getDomain();
    }
    
    @Override
    protected Fitness calculateFitness(Type solution) {
        double nnFitness = neuralNetworkProblem.getFitness(solution).getValue().doubleValue();        
        return objective.evaluate(nnFitness + penalty.calculatePenalty(neuralNetworkProblem.getNeuralNetwork()));
    }
    
    public NNTrainingProblem getNeuralNetworkProblem() {
        return neuralNetworkProblem;
    }

    public void setNeuralNetworkProblem(NNTrainingProblem neuralNetworkProblem) {
        this.neuralNetworkProblem = neuralNetworkProblem;
    }

    public NNPenalty getPenalty() {
        return penalty;
    }

    public void setPenalty(NNPenalty penalty) {
        this.penalty = penalty;
    }

    @Override
    public void operateOnData() {
        neuralNetworkProblem.operateOnData();
    }

    @Override
    public AbstractProblem getClone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainRegistry getDomain() {
        return neuralNetworkProblem.getDomain();
    }    
    
    @Override
    public StandardPatternDataTable getGeneralisationSet() {
        return neuralNetworkProblem.getGeneralisationSet();
    }    
    
    @Override
    public StandardPatternDataTable getTrainingSet() {
        return neuralNetworkProblem.getTrainingSet();
    }    
    
    @Override
    public StandardPatternDataTable getValidationSet() {
        return neuralNetworkProblem.getValidationSet();
    }
    
    @Override
    public NeuralNetwork getNeuralNetwork() {
        return neuralNetworkProblem.getNeuralNetwork();
    }
}
