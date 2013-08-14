/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.coevolution.cooperative;

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.contextupdate.ContextUpdateStrategy;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ContributionSelectionStrategy;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ZeroContributionSelectionStrategy;
import net.sourceforge.cilib.coevolution.cooperative.problem.CooperativeCoevolutionProblemAdapter;
import net.sourceforge.cilib.coevolution.cooperative.problemdistribution.ProblemDistributionStrategy;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;

/**
 * This class implements a greedy thresholded approach to cooperative optimization.
 * Only one subpopulation is worked on at every iteration, moving to the next 
 * subpopulation when the threshold number of iterations has been reached.
 * 
 */
public class GreedyTemperedCooperativeCoevolutionAlgorithm extends CooperativeCoevolutionAlgorithm {

    private static final long serialVersionUID = 3351497412601778L;
    private int greedyIterations = 10;
    private int greedyIterationsCounter = 0;
    /**
     * Constructor
     */
    public GreedyTemperedCooperativeCoevolutionAlgorithm() {
        super();
    }

    /**
     * Copy constructor
     * @param copy The {@linkplain CooperativeCoevolutionAlgorithm} to make a copy of.
     */
    public GreedyTemperedCooperativeCoevolutionAlgorithm(GreedyTemperedCooperativeCoevolutionAlgorithm copy) {
        super(copy);
    }

    @Override
    public void algorithmInitialisation() {
        super.algorithmInitialisation();
        algorithmIterator.setAlgorithms(subPopulationsAlgorithms); // set the iterator right away!
        algorithmIterator.next(); // set iterator index to something sensible
    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected void algorithmIteration() {
        CooperativeCoevolutionProblemAdapter problem = (CooperativeCoevolutionProblemAdapter) algorithmIterator.current().getOptimisationProblem();
        //update the context solution to point to the current context
        problem.updateContext(context.getCandidateSolution());
        //perform an iteration of the sub population algorithm
        algorithmIterator.current().performIteration();
        greedyIterationsCounter++;
        //select the contribution from the population
        contextUpdate.updateContext(context, ((ParticipatingAlgorithm) algorithmIterator.current()).getContributionSelectionStrategy().getContribution(algorithmIterator.current()), ((CooperativeCoevolutionProblemAdapter) algorithmIterator.current().getOptimisationProblem()).getProblemAllocation());
        if(greedyIterationsCounter == greedyIterations) { // Reached pre-specified number of iterations. Therefore, go to the next subpopulation
            greedyIterationsCounter = 0;
            if(algorithmIterator.hasNext()) {
                algorithmIterator.next();
            }
            else {
                algorithmIterator.setAlgorithms(subPopulationsAlgorithms); // set the iterator right away!
                algorithmIterator.next(); // set iterator index to something sensible
            }
        }
    }

    public int getGreedyIterations() {
        return greedyIterations;
    }

    public void setGreedyIterations(int greedyIterations) {
        this.greedyIterations = greedyIterations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OptimisationSolution getBestSolution() {
        return new OptimisationSolution(context.getCandidateSolution().getClone(), context.getFitness());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        return Arrays.asList(getBestSolution());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPopulationBasedAlgorithm(SinglePopulationBasedAlgorithm algorithm) {
        // TODO: There should be a better way to perfrom this test, rather than using an instanceof.
        if (((ParticipatingAlgorithm) algorithm).getContributionSelectionStrategy() instanceof ZeroContributionSelectionStrategy) {
            ((ParticipatingAlgorithm) algorithm).setContributionSelectionStrategy(contributionSelection);
        }

        super.addPopulationBasedAlgorithm(algorithm);
    }

    @Override
    public ContributionSelectionStrategy getContributionSelectionStrategy() {
        return contributionSelection;
    }

    @Override
    public void setContributionSelectionStrategy(ContributionSelectionStrategy strategy) {
        contributionSelection = strategy;
    }

    @Override
    public void setContextUpdate(ContextUpdateStrategy contextUpdate) {
        this.contextUpdate = contextUpdate;
    }

    @Override
    public void setProblemDistribution(ProblemDistributionStrategy problemDistribution) {
        this.problemDistribution = problemDistribution;
    }

    @Override
    public ContextEntity getContext() {
        return context;
    }
}
