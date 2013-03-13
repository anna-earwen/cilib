/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.nn;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.DataTableBuilder;
import net.sourceforge.cilib.io.DelimitedTextFileReader;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.io.transform.ShuffleOperator;
import net.sourceforge.cilib.io.transform.TypeConversionOperator;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.nn.NeuralNetworks;
import net.sourceforge.cilib.nn.architecture.visitors.OutputErrorVisitor;
import net.sourceforge.cilib.problem.AbstractProblem;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Class represents a {@link NNTrainingProblem} where the goal is to optimize
 * the set of weights of a neural network to best fit a given dataset (either
 * regression, classification etc.) presented to the network in on-line fashion.
 * (I.e., one pattern at a time).
 */
public class NNOnlineTrainingProblem extends NNTrainingProblem {
    private static final long serialVersionUID = -8765101028460476990L;

    private DataTableBuilder dataTableBuilder;
    private DataTable dataTable; // stores the entire data set from which training patterns are sampled
    private boolean initialized;

    private ProbabilityDistributionFunction randomizer;
    private int onlineSize = 1; // how many patterns to train on each time;
    private int trainingSize;

    /**
     * Default constructor.
     */
    public NNOnlineTrainingProblem() {
        super();
        randomizer = new UniformDistribution();
        dataTableBuilder = new DataTableBuilder(new DelimitedTextFileReader());
        initialized = false;
    }

    /**
     * Initializes the problem by reading in the data and constructing the data table,
     * as well as the initial training and generalization sets. Also initializes (constructs) the neural network.
     */
    @Override
    public void initialise() {
        if (initialized) {
            return;
        }
        try {
            dataTableBuilder.addDataOperator(new TypeConversionOperator());
            dataTableBuilder.addDataOperator(patternConversionOperator);
            dataTableBuilder.buildDataTable();
            dataTable = dataTableBuilder.getDataTable(); // dataTable
            
            trainingSize = (int) (dataTable.size() * trainingSetPercentage);
            //int validationSize = (int) (dataTable.size() * validationSetPercentage);
            int generalizationSize = dataTable.size() - trainingSize;// - validationSize;            

            trainingSet = new StandardPatternDataTable();
            generalizationSet = new StandardPatternDataTable();
            //validationSet =  new StandardPatternDataTable();

            if(shuffle) {
                shuffler = new ShuffleOperator();
                shuffler.operate(dataTable);
            }

            for (int i = 0; i < onlineSize; i++) {
                trainingSet.addRow((StandardPattern) dataTable.getRow((int)randomizer.getRandomNumber(0, trainingSize - 1)));
            }
            
            for (int i = trainingSize; i < generalizationSize + trainingSize; i++) {
                generalizationSet.addRow((StandardPattern) dataTable.getRow(i));
            }

            neuralNetwork.initialize();
        } catch (CIlibIOException exception) {
            exception.printStackTrace();
        }
        initialized = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractProblem getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Calculates the fitness of the given solution by setting the neural network
     * weights to the solution and evaluating the training set in order to calculate
     * the MSE (which is minimized). Also checks whether the window has to be slided,
     * and slides the window when necessary by adjusting the training and generalization sets.
     * @param solution the weights representing a solution.
     * @return a new MinimizationFitness wrapping the MSE training error.
     */
    @Override
    protected Fitness calculateFitness(Type solution) {
        if (trainingSet == null) {
            this.initialise();
        }

        neuralNetwork.setWeights((Vector) solution);

        double errorTraining = 0.0;
        OutputErrorVisitor visitor = new OutputErrorVisitor();
        Vector error = null;
        for (StandardPattern pattern : trainingSet) {
            Vector output = neuralNetwork.evaluatePattern(pattern);
            visitor.setInput(pattern);
            neuralNetwork.getArchitecture().accept(visitor);
            error = visitor.getOutput();
            for (Numeric real : error) {
                errorTraining += real.doubleValue() * real.doubleValue();
            }
        }
        errorTraining /= trainingSet.getNumRows() * error.size();

        operateOnData(); // get new random training patterns into the "on-line" training set
        
        return objective.evaluate(errorTraining);
    }

    @Override
    public void operateOnData() {        
        trainingSet.clear();
        for (int i = 0; i < onlineSize; i++) {
            trainingSet.addRow((StandardPattern) dataTable.getRow((int)randomizer.getRandomNumber(0, trainingSize - 1)));
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public DomainRegistry getDomain() {
        if (!initialized) {
            this.initialise();
        }
        int numWeights = NeuralNetworks.countWeights(neuralNetwork);
        String domainString = neuralNetwork.getArchitecture().getArchitectureBuilder().getLayerBuilder().getDomain();
        StringBasedDomainRegistry stringBasedDomainRegistry = new StringBasedDomainRegistry();
        stringBasedDomainRegistry.setDomainString(domainString + "^" + numWeights);
        return stringBasedDomainRegistry;
    }

    /**
     * Gets the datatable builder.
     * @return the datatable builder.
     */
    public DataTableBuilder getDataTableBuilder() {
        return dataTableBuilder;
    }

    /**
     * Sets the datatable builder.
     * @param dataTableBuilder the new datatable builder.
     */
    public void setDataTableBuilder(DataTableBuilder dataTableBuilder) {
        this.dataTableBuilder = dataTableBuilder;
    }

    /**
     * Gets the source URL of the the datatable builder.
     * @return the source URL of the the datatable builder.
     */
    public String getSourceURL() {
        return dataTableBuilder.getSourceURL();
    }

    /**
     * Sets the source URL of the the datatable builder.
     * @param sourceURL the new source URL of the the datatable builder.
     */
    public void setSourceURL(String sourceURL) {
        dataTableBuilder.setSourceURL(sourceURL);
    }

    public ProbabilityDistributionFunction getRandomizer() {
        return randomizer;
    }

    public void setRandomizer(ProbabilityDistributionFunction randomizer) {
        this.randomizer = randomizer;
    }

    public int getOnlineSize() {
        return onlineSize;
    }

    public void setOnlineSize(int onlineSize) {
        this.onlineSize = onlineSize;
    }
}
