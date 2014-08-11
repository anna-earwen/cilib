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

/**
 * Class implements an {@link ArchitectureOperationVisitor} that performs a feed-
 * forward through a neural network architechture as the visit operation.
 */
public class ElmanRecurrentVisitor extends ArchitectureOperationVisitor {


    /**
     * Perform a feed-forward using {@link #input} as the input for the FF and
     * storing the output in {@link #output}.
     * @param architecture the architechture to visit.
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
            /*if(l == 1) {
                System.out.print("Iteration: " + AbstractAlgorithm.get().getIterations() + ", ");
                System.out.print("context activation: ");
                for(int f = 0; f <layers.get(l - 1).size(); f++ ) {
                    System.out.print(layers.get(l - 1).getNeuralInput(f) + ", ");
                }
            }*/
            for (int n = 0; n < layerSize; n++) {
                currentLayer.get(n).calculateActivation(layers.get(l - 1));
            }
        }
        // Now, send stuff to context layer:
        Layer inputLayer = layers.get(0);
        Layer hiddenLayer = layers.get(1);
        int contextSize = hiddenLayer.size() - 1;
        //System.out.println("Context size (supposedly): " + contextSize);
        //System.out.println("Input layer size: " + inputLayer.size());
        //System.out.print("Iteration: " + AbstractAlgorithm.get().getIterations() + ", ");
         //System.out.print("hidden activation: ");
        for(int j = 0; j < contextSize; j++  ) { // go through context nodes
            //System.out.println("j: " + j);
            //System.out.println("Input layer size: " + inputLayer.size());
            //System.out.println(inputLayer.get(j).toString());
            inputLayer.get(j).setActivation(hiddenLayer.get(j).getActivation()); // simply paste hidden activation into the context
            //System.out.print( hiddenLayer.get(j).getActivation() + ", ");
        }
         //System.out.println();

        this.output = currentLayer.getActivations();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public ArchitectureOperationVisitor getClone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
