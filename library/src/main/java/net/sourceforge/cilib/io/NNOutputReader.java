/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io;

import net.sourceforge.cilib.io.exception.CIlibIOException;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.io.transform.PatternConversionOperator;
import net.sourceforge.cilib.io.transform.TypeConversionOperator;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Class reads data line by line (with the help of any chosen DataReader), calculates NN output
 * and returns the NN output, also line by line, per pattern. Only the output is returned.
 */
public class NNOutputReader implements DataReader<List<Type>> {

    private NeuralNetwork neuralNetwork;
    private DataReader inputReader;

    /** Default constructor. Initializes the delimiter to be a comma, i.e.
     * the class is a csv reader.
     */
    public NNOutputReader() {
        inputReader = new DelimitedTextFileReader();
    }

    /**
     * Returns the next line in the file.
     * @return a tokenized line in the file.
     */
    @Override
    public List<Type> nextRow() {
        try {
           DataTable dataTable = new StandardDataTable();
           dataTable.addRow(this.inputReader.nextRow());

           TypeConversionOperator op1 = new TypeConversionOperator();
           PatternConversionOperator op2 = new PatternConversionOperator();

           dataTable = op1.operate(dataTable); // convert type
           StandardPatternDataTable patternTable = (StandardPatternDataTable)op2.operate(dataTable); // convert to patterns

           Vector output = this.neuralNetwork.evaluatePattern(patternTable.getRow(0));
           List<Type> list = new ArrayList<Type>();
           for(int i = 0; i < output.size(); i++) {
               list.add(output.get(i));
           }
           return list;
       } catch (CIlibIOException ex) {
            ex.printStackTrace();
            return null;
       }
    }
    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> getColumnNames() {
        return new ArrayList<String>();
    }

    @Override
    public void open() throws CIlibIOException {
        Vector solution = (Vector) AbstractAlgorithm.get().getBestSolution().getPosition();
        NNTrainingProblem problem = (NNTrainingProblem) AbstractAlgorithm.get().getOptimisationProblem();
        neuralNetwork = problem.getNeuralNetwork();
        neuralNetwork.setWeights(solution);
        this.inputReader.open();
    }

    @Override
    public boolean hasNextRow() throws CIlibIOException {
        return this.inputReader.hasNextRow();
    }

    @Override
    public void close() throws CIlibIOException {
        this.inputReader.close();
    }

    @Override
    public String getSourceURL() {
        return this.inputReader.getSourceURL();
    }

    @Override
    public void setSourceURL(String sourceURL) {
       this.inputReader.setSourceURL(sourceURL);
    }

    public DataReader getInputReader() {
        return inputReader;
    }

    public void setInputReader(DataReader inputReader) {
        this.inputReader = inputReader;
    }

}
