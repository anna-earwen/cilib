/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.coevolution.cooperative.problemdistribution;

import com.google.common.base.Preconditions;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.CooperativeCoevolutionAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.problem.CooperativeCoevolutionProblemAdapter;
import net.sourceforge.cilib.coevolution.cooperative.problem.DimensionAllocation;
import net.sourceforge.cilib.coevolution.cooperative.problem.SequentialDimensionAllocation;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This {@linkplain ProblemDistributionStrategy} performs a split by assigning a
 * sequential portion equal to the number of weights between consequent NN layers
 * to each participating {@linkplain PopulationBasedAlgorithm}. 
 * 
 */
public class NeuralNetworkDistributionStrategy implements ProblemDistributionStrategy {

    /**
     * Splits up the given {@link OptimisationProblem} into sub-problems, where each sub problem corresponds to a single layer of NN weights.
     * @param populations The list of participating {@linkplain PopulationBasedAlgorithm}s.
     * @param problem The problem that needs to be re-distributed.
     * @param context The context vector maintained by the {@linkplain CooperativeCoevolutionAlgorithm}.
     */
    @Override
    public void performDistribution(List<SinglePopulationBasedAlgorithm> populations,
            Problem p, Vector context) {
        Preconditions.checkArgument(populations.size() >= 2, "There should at least be two Cooperating populations in a Cooperative Algorithm");
        NNTrainingProblem problem = (NNTrainingProblem)p; // convert optimisation problem to NNTrainingProblem
        NeuralNetwork nn = problem.getNeuralNetwork();
        int numLayers = nn.getArchitecture().getNumLayers();
        Preconditions.checkArgument(numLayers - 1 == populations.size(), "The number of populations should be equal to the number of NN layers minus one");

        int offset = 0;
        for(int l = 1; l < nn.getArchitecture().getNumLayers(); l++) { // go through the NN layer by layer, assigning weights to subpopulations
            int previousLayerSize = nn.getArchitecture().getLayers().get(l - 1).size();
            int thisLayerSize = nn.getArchitecture().getLayers().get(l).size(); 
            if(l != nn.getArchitecture().getNumLayers() - 1) {
                thisLayerSize -=1;
            } // subtract bias, unless we're dealing with output layers
            int subpopulationSize = previousLayerSize * thisLayerSize;
            DimensionAllocation allocation = new SequentialDimensionAllocation(offset, subpopulationSize);
            populations.get(l - 1).setOptimisationProblem(new CooperativeCoevolutionProblemAdapter(problem, allocation, context));
            offset += subpopulationSize;
        }
    }
}
