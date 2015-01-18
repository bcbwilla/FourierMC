package xyz.benw.plugins.fouriermc;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.ArrayDeque;

/**
 * Created by ben on 15/01/15.
 */
public class AnalysisUtil {

    //private int[] powersOfTwo = {2, 4, 16, 32, 64, 128};


    public static double getClicksPerSecond(ArrayDeque<Long> a){
        return (double) ( a.size()  / (a.getLast() - a.getFirst().doubleValue())) * 1000;
    }


    public static double[] getFourierTransform(ArrayDeque<Long> a) {

        Object[] ar = a.toArray();

        double[] timeStampArray = new double[a.size()];

        for(int i = 0; i < timeStampArray.length; i++) {
            timeStampArray[i] = new Double(ar[i].toString());
        }

        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] transformed = fft.transform(timeStampArray, TransformType.FORWARD);


        double[] coefficients = new double[transformed.length];

        for(int i = 0; i < transformed.length; i++) {
            coefficients[i] = transformed[i].getReal();
        }

        return coefficients;

    }


}
