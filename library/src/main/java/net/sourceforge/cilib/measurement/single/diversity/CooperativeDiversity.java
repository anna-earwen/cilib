/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single.diversity;

import java.util.Iterator;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.iterator.AlgorithmIterator;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.ContextEntity;
import net.sourceforge.cilib.coevolution.cooperative.CooperativeCoevolutionAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.contextupdate.ContextUpdateStrategy;
import net.sourceforge.cilib.coevolution.cooperative.contextupdate.PrimitiveContextUpdateStrategy;
import net.sourceforge.cilib.coevolution.cooperative.problem.CooperativeCoevolutionProblemAdapter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.measurement.single.diversity.centerinitialisationstrategies.CenterInitialisationStrategy;
import net.sourceforge.cilib.measurement.single.diversity.centerinitialisationstrategies.SpatialCenterInitialisationStrategy;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;

/**
 * TODO: Add JavaDoc.
 *
 */
public class CooperativeDiversity implements Measurement<Real> {

    private static final long serialVersionUID = 7417526206433000209L;
    protected DistanceMeasure distanceMeasure;
    protected CenterInitialisationStrategy populationCenter;
    //protected DiversityNormalisation normalisationParameter;

    public CooperativeDiversity() {
        distanceMeasure = new EuclideanDistanceMeasure();
        populationCenter = new SpatialCenterInitialisationStrategy();
        //normalisationParameter = new NormalisationParameter();
    }

    public CooperativeDiversity(CooperativeDiversity other) {
        this.distanceMeasure = other.distanceMeasure;
        this.populationCenter = other.populationCenter;
        //this.normalisationParameter = other.normalisationParameter;
    }

    @Override
    public CooperativeDiversity getClone() {
        return new CooperativeDiversity(this);
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        CooperativeCoevolutionAlgorithm multiPopulationBasedAlgorithm = (CooperativeCoevolutionAlgorithm) algorithm;
        
        ContextEntity contextEntity = multiPopulationBasedAlgorithm.getContext().getClone();
        ContextUpdateStrategy contextUpdate = new PrimitiveContextUpdateStrategy();
        
        //iterate through each algorithm
        AlgorithmIterator<SinglePopulationBasedAlgorithm> iter = multiPopulationBasedAlgorithm.getAlgorithmIterator().getClone();
        iter.setAlgorithms(multiPopulationBasedAlgorithm.getPopulations());
        
        fj.data.List<Particle> pseudoTopology = fj.data.List.list();//new fj.data.Array<Particle>();
        //System.out.println("%%%%% Context before interfering: "+multiPopulationBasedAlgorithm.getContext().getCandidateSolution().toString());
        while (iter.hasNext()) { // go through every sub-population
            Iterator<? extends Entity> populationIterator = iter.next().getTopology().iterator();
            CooperativeCoevolutionProblemAdapter problem = (CooperativeCoevolutionProblemAdapter) iter.current().getOptimisationProblem();
            //System.out.println("---------------------------------------------------");
            while (populationIterator.hasNext()) { // go through every particle of the sub-population
                //System.out.println("Pre-Contextualised entity: "+contextEntity.getCandidateSolution().toString());
                //System.out.println("Context size: "+contextEntity.getDimension());
                //System.out.println("Problem allocation: "+problem.getProblemAllocation().getSize());
                Vector cs = (Vector) populationIterator.next().getCandidateSolution();
                //System.out.println("Candidate solution: "+cs.toString());
                contextUpdate.updateContext(contextEntity, cs, problem.getProblemAllocation());
                Particle pseudoParticle = new StandardParticle();
                pseudoParticle.getProperties().put(EntityType.CANDIDATE_SOLUTION, contextEntity.getProperties().get(EntityType.CANDIDATE_SOLUTION));
                pseudoParticle.getProperties().put(EntityType.FITNESS, contextEntity.getProperties().get(EntityType.FITNESS));
                //ystem.out.println("Context size: "+contextEntity.getDimension());
                pseudoTopology = pseudoTopology.cons(pseudoParticle);
                //System.out.println("Contextualised entity: "+contextEntity.getCandidateSolution().toString());
                //System.out.println();
                contextEntity = multiPopulationBasedAlgorithm.getContext().getClone();
            }
        }
        
        //System.out.println("%%%%% Context after interfering: "+multiPopulationBasedAlgorithm.getContext().getCandidateSolution().toString());
        SinglePopulationBasedAlgorithm<Particle> pseudoAlgorithm = new PSO();
        pseudoAlgorithm.setTopology(pseudoTopology);
        Diversity diversityCalculator = new Diversity();
        return diversityCalculator.getValue(pseudoAlgorithm);
    }

    /**
     * @return the distanceMeasure
     */
    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }

    /**
     * @param distanceMeasure the distanceMeasure to set
     */
    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    /**
     * @return the populationCenter
     */
    public CenterInitialisationStrategy getPopulationCenter() {
        return populationCenter;
    }

    /**
     * @param populationCenter the populationCenter to set
     */
    public void setPopulationCenter(CenterInitialisationStrategy populationCenter) {
        this.populationCenter = populationCenter;
    }
}
