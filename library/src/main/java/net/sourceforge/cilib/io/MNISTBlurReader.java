/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.type.types.Type;
import mnist.tools.MnistManager;
import net.sourceforge.cilib.type.types.Real;

public class MNISTBlurReader implements DataReader<List<Type>> {
    private boolean hasNextRow = false;
    private String inputTrain;
    private String labelTrain;
    private String inputTest;
    private String labelTest;
    private MnistManager mnistTrain;
    private MnistManager mnistTest;
    // Ranges optimised for Elliott:
    private double lowerRange = -5; 
    private double upperRange = 5;
    private double targetLowerRange = -1;
    private double targetUpperRange = 1;
    private static final double MNSIT_LOWER_RANGE = 0;
    private static final double MNIST_UPPER_RANGE = 255;
    private static final int MNSIT_IMAGE_HEIGHT = 28;
    private static final int MNSIT_IMAGE_WIDTH = 28;
    private static final int MNSIT_TARGET_LENGTH = 10;
    
    

    public MNISTBlurReader() {
        super();
    }

    @Override
    public void close() throws CIlibIOException {       
    }

    @Override
    public List<String> getColumnNames() {
        return new ArrayList<String>();
    }

    @Override
    public String getSourceURL() {
        return "";
    }

    @Override
    public boolean hasNextRow() throws CIlibIOException {

        try {
            hasNextRow = (mnistTest.getImages().getCurrentIndex() <= mnistTest.getImages().getCount()
                    || mnistTrain.getImages().getCurrentIndex() <= mnistTrain.getImages().getCount());
            if(!hasNextRow) { System.out.println("Total number of images read = " + (mnistTest.getImages().getCurrentIndex() + mnistTrain.getImages().getCurrentIndex() - 1)); }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hasNextRow;
    }

    
    private double scaleIt(double val, double valLowerRange, double valUpperRange, double newLowerRange, double newUpperRange) 
    {
      return (val - valLowerRange)*(newUpperRange - newLowerRange) 
        / (valUpperRange - valLowerRange) + newLowerRange;
    }
    
    /**
     * 
     * @return the next row
     */
    @Override
    public List<Type> nextRow(){ 
        
        List<Type> theNextRow = Lists.newArrayList();
        try {            
            int[][] image = null;
            int imageLabel = 0;
            boolean hasImg = false;
            
            if(mnistTrain.getImages().getCurrentIndex() <= mnistTrain.getImages().getCount()) {
                image = this.mnistTrain.readImage();
                imageLabel = this.mnistTrain.readLabel();
                hasImg = true;
            } else if(mnistTest.getImages().getCurrentIndex() <= mnistTest.getImages().getCount()){
                image = this.mnistTest.readImage();
                imageLabel = this.mnistTest.readLabel();     
                hasImg = true;       
            }
            if(hasImg) {
                for(int i = 0; i < MNSIT_IMAGE_WIDTH-1; i+=2) {
                    for(int j = 0; j < MNSIT_IMAGE_HEIGHT-1; j+=2) {                    
                        theNextRow.add(Real.valueOf(
                                scaleIt(((double)image[i][j] + (double)image[i+1][j] + (double)image[i][j+1] + (double)image[i+1][j+1])/4.0, 
                                MNSIT_LOWER_RANGE, MNIST_UPPER_RANGE, lowerRange, upperRange)));
                    }
                }

                for(int i = 0; i < MNSIT_TARGET_LENGTH; i++) {
                    if(i == imageLabel) {
                        theNextRow.add(Real.valueOf(targetUpperRange));
                    } else {
                        theNextRow.add(Real.valueOf(targetLowerRange));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return theNextRow;
    }

    @Override
    public void open() throws CIlibIOException {
        try {
            this.mnistTrain = new MnistManager(this.inputTrain,this.labelTrain);        
            this.mnistTest = new MnistManager(this.inputTest,this.labelTest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSourceURL(String sourceURL) {
    }

    public String getInputTrain() {
        return inputTrain;
    }

    public void setInputTrain(String inputTrain) {
        this.inputTrain = inputTrain;
    }

    public String getLabelTrain() {
        return labelTrain;
    }

    public void setLabelTrain(String labelTrain) {
        this.labelTrain = labelTrain;
    }

    public String getInputTest() {
        return inputTest;
    }

    public void setInputTest(String inputTest) {
        this.inputTest = inputTest;
    }

    public String getLabelTest() {
        return labelTest;
    }

    public void setLabelTest(String labelTest) {
        this.labelTest = labelTest;
    }

    public double getLowerRange() {
        return lowerRange;
    }

    public void setLowerRange(double lowerRange) {
        this.lowerRange = lowerRange;
    }

    public double getUpperRange() {
        return upperRange;
    }

    public void setUpperRange(double upperRange) {
        this.upperRange = upperRange;
    }

    public double getTargetLowerRange() {
        return targetLowerRange;
    }

    public void setTargetLowerRange(double targetLowerRange) {
        this.targetLowerRange = targetLowerRange;
    }

    public double getTargetUpperRange() {
        return targetUpperRange;
    }

    public void setTargetUpperRange(double targetUpperRange) {
        this.targetUpperRange = targetUpperRange;
    }

}
