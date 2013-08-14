/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture.builder;

import java.util.List;
import net.sourceforge.cilib.functions.activation.Linear;
import net.sourceforge.cilib.nn.architecture.Architecture;
import net.sourceforge.cilib.nn.architecture.ForwardingLayer;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.BiasNeuron;
import net.sourceforge.cilib.nn.components.Neuron;

/**
 *
 */
public class ElmanArchitectureBuilder extends ArchitectureBuilder {

    /**
     * Adds the layers to the architecture such that the architecture represents
     * an N layer Elman Neural Network. All layers are fully connected and
     * hidden layers are constructed with a bias neuron if specified so by the
     * {@link LayerConfiguration}, the output layer does not have a bias neuron, 
     * and a context layer is present.
     * @param architecture {@inheritDoc }
     */
    @Override
    public void buildArchitecture(Architecture architecture) {
        List<Layer> layers = architecture.getLayers();
        layers.clear();

        LayerBuilder layerBuilder = this.getLayerBuilder();
        List<LayerConfiguration> layerConfigurations = this.getLayerConfigurations();
        int listSize = layerConfigurations.size();

        layerConfigurations.get(listSize - 1).setBias(false); // output layer doesn't have bias

        // build the input layer
        ForwardingLayer inputLayer = new ForwardingLayer();
        inputLayer.setSourceSize(layerConfigurations.get(0).getSize());
        Neuron prototypeNeuron = new Neuron(); // create a simple prototype
        prototypeNeuron.setActivationFunction(new Linear()); // context nodes simply forward stuff
        for(int i = 0; i < layerConfigurations.get(1).getSize(); i++) { // add context nodes equal to the number of hidden units in the next layer
            inputLayer.add((Neuron)prototypeNeuron.getClone());
        }
        if (layerConfigurations.get(0).isBias()) { // add bias on top
            inputLayer.setBias(true);
            inputLayer.add(new BiasNeuron());
        }
        layers.add(inputLayer);

        Layer currentLayer = inputLayer;
        // build the rest of the layers - for now, there is only one context layer
        int previousLayerAbsoluteSize = currentLayer.size();
        //System.out.println("Size of input layer + context: " + previousLayerAbsoluteSize);
        for (int i = 1; i < listSize; i++) {
            currentLayer = layerBuilder.buildLayer(layerConfigurations.get(i), previousLayerAbsoluteSize);
            layers.add(currentLayer);
            previousLayerAbsoluteSize = currentLayer.size();
        }
    }

    @Override
    public ArchitectureBuilder getClone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
