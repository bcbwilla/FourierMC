package xyz.benw.plugins.fouriermc.DataAnalysis.DataTests;

import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.util.CombinatoricsUtils;



/**
 * PatternDetection
 *
 * Performs a fourier transform on the data to
 * search for strong frequencies
 *
 */
public class PatternDetection implements IDataTest {

    private double[] data;

    private PatternDetectionMethod method;
    private double[] periodogram;
    private double criteria = 0.001;

    private double fisherGValue;
    private double fisherPValue;


    public PatternDetection(double[] data, PatternDetectionMethod method) {
        this.data = data;
        this.method = method;
    }

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
     *
     * See http://www.mathworks.com/help/signal/ug/significance-testing-for-periodic-component.html
     *
     */
    private double fisherG() {

        double maxVal = new Max().evaluate(periodogram);
        fisherGValue = maxVal / new Sum().evaluate(periodogram);
        return fisherGValue;

    }

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

    public double getFisherGValue() {
        return fisherGValue;
    }

    public double getFisherPValue() {
        return fisherPValue;
    }
}

