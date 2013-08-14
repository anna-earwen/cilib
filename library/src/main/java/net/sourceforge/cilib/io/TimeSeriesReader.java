/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io;


import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.type.types.Type;

public class TimeSeriesReader implements DataReader<List<Type>> {
    private DataReader<List<Type>> delegate;
    private int embedding = 2; // default value: predict the next step from the previous step, thus, embedding = two steps in the time series
    private int tau = 1; // specifies the time lag between observations
    private boolean hasNextRow = true;
    private int step = 1; // specifies the distance between patterns
    private List<Type> embeddedRow;
    private List<Type> dataSample;


    public TimeSeriesReader() {
        super();
        this.embeddedRow = new ArrayList<Type>();
        this.dataSample = new ArrayList<Type>();
    }


    @Override
    public void close() throws CIlibIOException {
       delegate.close();
    }

    @Override
    public List<String> getColumnNames() {
        return delegate.getColumnNames();
    }

    @Override
    public String getSourceURL() {
        return delegate.getSourceURL();
    }

    @Override
    public boolean hasNextRow() throws CIlibIOException {
        return hasNextRow && delegate.hasNextRow();
    }

    /**
     * Creates the next row based on the specified embedding and tau. Source is expected to be a single column of time-series values.
     * @return the next row
     */
    @Override
    public List<Type> nextRow(){
          try {
              if(dataSample.size() > 0) { // not the first pattern
                  for(int j = 0; j < step; j++) {
                      dataSample.remove(0); // remove n previous entries
                      if(delegate.hasNextRow()) {
                          dataSample.add(delegate.nextRow().get(0)); // we only operate with index 0 because we assume univariate time series
                      } else {
                          hasNextRow = false;
                          return embeddedRow; // previous pattern
                      }
                  }
              } else { // construct the first dataSample vector
                  int sampleSize = tau * (embedding - 1) + 1;
                  for(int c = 0; c < sampleSize; c++) {
                      if(delegate.hasNextRow()) {
                          dataSample.add(delegate.nextRow().get(0)); // we only operate with index 0 because we assume univariate time series
                      } else {
                          hasNextRow = false;
                          return embeddedRow; // this will be empty
                      }
                  }
              } // Now that dataSample is constructed, sample the embedded row
              embeddedRow.clear();
              for(int j = 0; j < dataSample.size(); j+=tau) {
                  embeddedRow.add(dataSample.get(j));
              }
              return embeddedRow;
          } catch (CIlibIOException e) {
            e.printStackTrace();
            return null;
          }
    }

    @Override
    public void open() throws CIlibIOException {
        delegate.open();
    }

    @Override
    public void setSourceURL(String sourceURL) {
        delegate.setSourceURL(sourceURL);
    }

    public DataReader<List<Type>> getDelegate() {
        return delegate;
    }

    public void setDelegate(DataReader<List<Type>> delegate) {
        this.delegate = delegate;
    }

    public int getEmbedding() {
        return embedding;
    }

    public void setEmbedding(int embedding) {
        this.embedding = embedding;
    }

    public int getTau() {
        return tau;
    }

    public void setTau(int tau) {
        this.tau = tau;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
