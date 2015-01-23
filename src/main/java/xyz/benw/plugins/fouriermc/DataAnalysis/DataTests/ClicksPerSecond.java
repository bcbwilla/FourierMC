package xyz.benw.plugins.fouriermc.DataAnalysis.DataTests;

import org.apache.commons.math3.stat.descriptive.summary.Sum;

/**
 * ClicksPerSecond
 *
 * Computes a player's clicks per second.
 *
 */
public class ClicksPerSecond implements IDataTest {

    private double[] data;

    private Long samplePeriod;


    public ClicksPerSecond(double[] data, Long samplePeriod) {
        this.data = data;
        this.samplePeriod = samplePeriod;
    }

    @Override
    public boolean evaluate(double criteria) {
        return getClicksPerSecond() < criteria;
    }

    public double getClicksPerSecond() {
        double clicksPerSecond;
        // *20 to convert from ticks to seconds
        int size = data.length;
        if(samplePeriod != 0 && size != 0) {
            clicksPerSecond = (new Sum().evaluate(data) / (samplePeriod * size)) * 20;
        } else {
            clicksPerSecond = 0.0;
        }
        return clicksPerSecond;
    }

}
