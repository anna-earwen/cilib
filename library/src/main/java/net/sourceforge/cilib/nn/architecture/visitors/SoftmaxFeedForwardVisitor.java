/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture.visitors;

import java.util.List;
import net.sourceforge.cilib.nn.architecture.Architecture;
import net.sourceforge.cilib.nn.architecture.ForwardingLayer;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.PatternInputSource;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.Vector.Builder;

/**
 * Class implements an {@link ArchitectureOperationVisitor} that performs a feed-
 * forward through a neural network architecture as the visit operation.
 */
public class SoftmaxFeedForwardVisitor extends ArchitectureOperationVisitor {

    public SoftmaxFeedForwardVisitor() {}

    public SoftmaxFeedForwardVisitor(SoftmaxFeedForwardVisitor rhs) {
        super(rhs);
    }

    @Override
    public SoftmaxFeedForwardVisitor getClone() {
        return new SoftmaxFeedForwardVisitor(this);
    }

    /**
     * Perform a feed-forward using {@link #input} as the input for the FF and
     * storing the output in {@link #output}.
     * @param architecture the architecture to visit.
     */
    @Override
    public void visit(Architecture architecture) {
        List<Layer> layers = architecture.getLayers();
        int size = layers.size();

        ((ForwardingLayer) layers.get(0)).setSource(new PatternInputSource(input));
        Layer currentLayer = null;
        for (int l = 1; l < size; l++) {
            currentLayer = layers.get(l);
            int layerSize = currentLayer.size();
            for (int n = 0; n < layerSize; n++) {
                currentLayer.get(n).calculateActivation(layers.get(l - 1));
            }
        }
        
        Vector activations = currentLayer.getActivations(); // now softmax contains 
        double activationSum = 0;
        for(Numeric element : activations) {
            activationSum += element.doubleValue();
        }
    
        Builder softmaxBuilder = Vector.newBuilder();
      
        for(Numeric element : activations) {
            softmaxBuilder.add(element.doubleValue() / activationSum );
        }
        
        this.output = softmaxBuilder.build();
        //System.out.println("Softmax out: " + this.output.toString());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isDone() {
        return false;
    }
}
