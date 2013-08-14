/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.iterationstrategies;

import java.util.Iterator;

import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.velocityprovider.WeightedInertiaVelocityProvider;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Implementation of the Hendtlass iteration strategy for PSO.
 *
 */
public class HendtlassSynchronousIterationStrategy extends AbstractIterationStrategy<PSO> {

    private static final long serialVersionUID = 6617737228912852220L;    
    protected Vector inertiaWeight;
    private Vector absoluteAverageVelocityVector;
    private Vector averageSpeedVector;
    private ControlParameter filter;
    private ControlParameter pwr;
    private ControlParameter initialInertiaWeight;

    public HendtlassSynchronousIterationStrategy() {
        this(ConstantControlParameter.of(0.729844),
            ConstantControlParameter.of(0.5),
            ConstantControlParameter.of(1));
    }
    
    public HendtlassSynchronousIterationStrategy(ControlParameter initialInertiaWeight, ControlParameter filter, ControlParameter pwr) {
        this.initialInertiaWeight = initialInertiaWeight;
        this.filter = filter;
        this.pwr = pwr;
        this.inertiaWeight = Vector.of();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public HendtlassSynchronousIterationStrategy getClone() {
        return this;
    }

    /**
     * @param pso The {@link PSO} to have an iteration applied.
     */
    @Override
    public void performIteration(PSO pso) {
        Topology<Particle> topology = pso.getTopology();
        
        this.calculateAbsoluteAverages(pso);
        this.updateInertia(pso);

        for (Particle current : topology) {// Update inertia!
            
            WeightedInertiaVelocityProvider wp = (WeightedInertiaVelocityProvider)current.getVelocityProvider(); // put 
            wp.setInertiaWeight(inertiaWeight);
            current.updateVelocity();
            current.updatePosition(); // TODO: replace with visitor (will simplify particle interface)

            boundaryConstraint.enforce(current);
        }

        for (Iterator<? extends Particle> i = topology.iterator(); i.hasNext();) {
            Particle current = i.next();
            current.calculateFitness();
            for (Iterator<? extends Particle> j = topology.neighbourhood(i); j.hasNext();) {
                Particle other = j.next();
                if (current.getSocialFitness().compareTo(other.getNeighbourhoodBest().getSocialFitness()) > 0) {

                    other.setNeighbourhoodBest(current); // TODO: neighbourhood visitor?

                } 
            }
        }
    }
    
    private void calculateAbsoluteAverages(PSO pso) {
        
        int dimension = pso.getTopology().get(0).getDimension();
        absoluteAverageVelocityVector = Vector.of();
        averageSpeedVector = Vector.of();
        
        for (Entity e : pso.getTopology()) {
            Vector velocity = (Vector)e.getProperties().get(EntityType.Particle.VELOCITY);
            for(int i = 0; i < dimension; i++) {
                if(absoluteAverageVelocityVector.size() < dimension) {
                    absoluteAverageVelocityVector.add(velocity.get(i));
                    averageSpeedVector.add(Real.valueOf(Math.abs(velocity.doubleValueOf(i))));
                } else {
                    absoluteAverageVelocityVector.setReal(i, absoluteAverageVelocityVector.doubleValueOf(i) + velocity.doubleValueOf(i));
                    averageSpeedVector.setReal(i, averageSpeedVector.doubleValueOf(i) + Math.abs(velocity.doubleValueOf(i)));
                }
            }
        }
     
        for(int i = 0; i < dimension; i++) {
            absoluteAverageVelocityVector.setReal(i, Math.abs(absoluteAverageVelocityVector.doubleValueOf(i)/(double)dimension));
            averageSpeedVector.setReal(i, averageSpeedVector.doubleValueOf(i)/(double)dimension);
        } 
        
    }
    
    private void updateInertia(PSO pso) {
        
        int dimension = pso.getTopology().get(0).getDimension();
        
        if(inertiaWeight.size() < dimension) {
            Vector.Builder builder = Vector.newBuilder();
            builder.repeat(dimension, Real.valueOf(initialInertiaWeight.getParameter()));
            inertiaWeight = builder.build();
        }
        Vector.Builder builder = Vector.newBuilder();
        for(int i = 0; i < dimension; i++) {
            builder.add(Math.sqrt(Math.pow(absoluteAverageVelocityVector.doubleValueOf(i),2)
                    +Math.pow(averageSpeedVector.doubleValueOf(i), 2)));
        }
        Vector d = builder.build(); // get the degree of convergence vector
        double max_d = 0;
        for(Numeric component : d) {
            if(component.doubleValue() > max_d) {
                max_d = component.doubleValue();
            }
        }
        if(max_d != 0) {
            Vector.Builder builder2 = Vector.newBuilder();
            for(Numeric component : d) {
                builder2.add(max_d/(max_d + component.doubleValue()));
            }        
            Vector w = builder2.build();
            
            /*double sum_w = 0;
            for(Numeric component : w) {
                sum_w += component.doubleValue();
            }
            
            /*
            Vector.Builder builder3 = Vector.newBuilder();
            for(Numeric component : w) {
                builder3.add(Math.pow(dimension * component.doubleValue() / sum_w, pwr.getParameter()));
            } */
            
            /*
            for(Numeric component : w) {
                //builder3.add(component.doubleValue() - w_mean / w_stdDiv);
                builder3.add(component.doubleValue() * initialInertiaWeight.getParameter());
            }
            for(int i = 0; i < inertiaWeight.size(); i++) {
                builder3.add(w.doubleValueOf(i) * inertiaWeight.doubleValueOf(i));
            }  
            */          
            /*
            Vector m = builder3.build();
            double sum_m = 0;
            for (Numeric num : m) {
                sum_m += num.doubleValue();
            }
            
            double m_mean = sum_m / (double) dimension;
            double sum_diff_squared = 0;
            for(Numeric component : m) {
                sum_diff_squared += Math.pow(component.doubleValue() - m_mean, 2);
            }
            double m_stdDiv = Math.sqrt(sum_diff_squared / (double) dimension);
            */
            //System.out.println("VEL: StdDiv of M: " + m_stdDiv + ", mean of M: " + m_mean);
            
            for(int i = 0; i < inertiaWeight.size(); i++) {
                inertiaWeight.setReal(i, (1 - filter.getParameter()) * w.doubleValueOf(i) 
                        + filter.getParameter() * inertiaWeight.doubleValueOf(i));//w.doubleValueOf(i));//;
            }
        }        
    }

    public ControlParameter getFilter() {
        return filter;
    }

    public void setFilter(ControlParameter filter) {
        this.filter = filter;
    }

    public ControlParameter getPwr() {
        return pwr;
    }

    public void setPwr(ControlParameter pwr) {
        this.pwr = pwr;
    }    

    public void setInitialInertiaWeight(ControlParameter initialInertiaWeight) {
        this.initialInertiaWeight = initialInertiaWeight;
    }
}
