package xyz.benw.plugins.fouriermc.dataanalysis.datatest;

import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.util.CombinatoricsUtils;


/**
 * Use signal analysis techniques to determine if clicking data contains a pattern consistent with
 * an autoclicker.
 *
 * @author bcbwilla
 */
public class PatternDetection implements IDataTest {

    private PatternDetectionMethod method;
    private double[] data;
    private double[] periodogram;
    private double fisherGValue;
    private double fisherPValue;

    /**
     * Class constructor
     *
     * @param data   the array of data to analyze
     * @param method the detection method to use
     */
    public PatternDetection(double[] data, PatternDetectionMethod method) {
        this.data = data;
        this.method = method;
    }

    /**
     * Evalute the detection test
     *
     * @param criteria  the detection criteria
     * @return          true if test passed, false if test failed.
     *                  A failed test implies suspicious activity.
     */
    @Override
    public boolean evaluate(double criteria) {

        // No clicking
        if(new Sum().evaluate(data) == 0) {
            return true;
        }

        makePeriodogram();

        if(method == PatternDetectionMethod.FISHER) {
            return fisherPValue() > criteria;

        } else if(method == PatternDetectionMethod.FTEST) {
            // Do something else
        }

        return true;
    }


    /**
     * Generate a periodogram of the signal.
     * This is the basis of either testing method.
     */
    private void makePeriodogram() {

        // Need to insert checks about power of 2, evenness, etc.
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] transformed = fft.transform(data, TransformType.FORWARD);

        int upperLimit = (int)(transformed.length/2.0) - 1;
        double[] periodogram = new double[upperLimit];

        for(int i = 0; i < upperLimit; i++) {
            periodogram[i] = Math.pow(transformed[i+1].abs(), 2);
        }

        this.periodogram = periodogram;
    }

    /**
     * Use the Fisher g-value to find periodicity in signal.
     * See <a href="http://www.mathworks.com/help/signal/ug/significance-testing-for-periodic-component.html">here</a> for background.
     *
     * @return  value of the fisher-g test statistic
     *
     */
    private double fisherG() {

        double maxVal = new Max().evaluate(periodogram);
        fisherGValue = maxVal / new Sum().evaluate(periodogram);
        return fisherGValue;

    }

    /**
     * Compute the p-value corresponding to the computed Fisher g-value.
     * A lower value implies a pattern match, but a decision can only
     * be made once a reasonable threshold is set.
     *
     * @return  the p-value corresponding to the Fisher g-value test.
     */
    private double fisherPValue() {

        int N = periodogram.length;
        double fisherG = fisherG();

        int upperLimit = (int) Math.floor(1/fisherG);

        double[] values = new double[upperLimit];
        for(int k=0; k < upperLimit; k++) {
            double binomialCo = CombinatoricsUtils.binomialCoefficientDouble(N, k+1);
            values[k] = Math.pow(-1, k) * binomialCo * Math.pow((1-(k+1)*fisherG), N-1);
        }

        fisherPValue = new Sum().evaluate(values);
        return fisherPValue;

    }

    /**
     * @return the Fisher g-value
     */
    public double getFisherGValue() {
        return fisherGValue;
    }

    /**
     * @return the p-value of the Fisher g-value test.
     */
    public double getFisherPValue() {
        return fisherPValue;
    }

    //TODO Implement F-Test cross validation
}

