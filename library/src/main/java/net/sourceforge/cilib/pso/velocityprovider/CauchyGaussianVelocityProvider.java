/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.CauchyDistribution;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *  The <tt>VelocityProvider</tt> for the Bare Bones PSO as defined by Kennedy.
 *
 *  TODO: get the required references
 *
 */
public class CauchyGaussianVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = -823686042197742768L;
    
    private RandomProvider provider;
    private double p = 0.5;
    protected ProbabilityDistributionFunction cauchyDistribution;
    protected ProbabilityDistributionFunction gaussianDistribution;

    public CauchyGaussianVelocityProvider() {
        this.cauchyDistribution = new CauchyDistribution();
        this.gaussianDistribution = new GaussianDistribution();
        this.provider = new MersenneTwister();
    }

    public CauchyGaussianVelocityProvider(CauchyGaussianVelocityProvider copy) {
        this.cauchyDistribution = copy.cauchyDistribution;
        this.gaussianDistribution = copy.gaussianDistribution;
        this.provider = copy.provider;
        this.p = copy.p;
    }

    @Override
    public CauchyGaussianVelocityProvider getClone() {
        return new CauchyGaussianVelocityProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        Vector localGuide = (Vector) particle.getLocalGuide(); // personal best (yi)
        Vector globalGuide = (Vector) particle.getGlobalGuide(); // global best (y^i)

        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); ++i) {
            //double tmp1 = cognitive.getParameter();
            //double tmp2 = social.getParameter();

            double sigma = Math.abs(localGuide.doubleValueOf(i) - globalGuide.doubleValueOf(i));
            if(sigma == 0) { sigma = 1; }
            //System.out.println("Sigma: "+sigma);
            //according to Kennedy
            double mean = 1;//(localGuide.doubleValueOf(i) + globalGuide.doubleValueOf(i)) / 2;
            //andries proposal: double mean = (tmp1*personalBestPosition.getReal(i) + tmp2*nBestPosition.getReal(i)) / (tmp1+tmp2);
            if(provider.nextDouble() < p) {
                builder.add(localGuide.doubleValueOf(i) + this.cauchyDistribution.getRandomNumber(mean, sigma));
            } else {
                builder.add(globalGuide.doubleValueOf(i) + this.gaussianDistribution.getRandomNumber(mean, sigma));
            }
        }
        return builder.build();
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }
}
