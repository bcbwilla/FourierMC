package xyz.benw.plugins.fouriermc.DataAnalysis.DataTests;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import xyz.benw.plugins.fouriermc.ClickData;


/**
 * Created by ben on 17/01/15.
 */
public class FourierTransform implements IDataTest {

    private ClickData data;

    private double[] result;
    private double criteria;


    public FourierTransform(ClickData data) {
        this.data = data;
    }


    @Override
    public boolean evaluate() {

        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] transformed = fft.transform(data.toDoubleArray(), TransformType.FORWARD);

        double[] coefficients = new double[transformed.length];

        for(int i = 0; i < transformed.length; i++) {
            coefficients[i] = transformed[i].getReal();
        }

        result = coefficients;

        // Do some stuff here

        return true;  //TODO: Devise criteria
    }
}
