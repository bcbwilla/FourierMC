package xyz.benw.plugins.fouriermc.DataAnalysis.DataTests;

import org.apache.commons.math3.stat.descriptive.summary.Sum;

/**
 * Computes a player's clicks per second.
 *
 * @author bcbwilla
 */
public class ClicksPerSecond implements IDataTest {

    private double[] data;

    private Long samplePeriod;
    private double clicksPerSecond;

    /**
     * Class constructor.
     *
     * @param data          the clicking data to analyze
     * @param samplePeriod  the period of time that each data element represents
     */
    public ClicksPerSecond(double[] data, Long samplePeriod) {
        this.data = data;
        this.samplePeriod = samplePeriod;
    }

    /**
     * Computes clicks per second and compares to criteria.
     *
     * @param criteria the value which is compared to the computed click speed. If computed value is larger,
     *                 the test is failed.
     * @return         true if test passed, false if test failed.
     *                 A failed test implies suspicious activity.
     */
    @Override
    public boolean evaluate(double criteria) {

        // *20 to convert from ticks to seconds
        int size = data.length;
        if(samplePeriod != 0 && size != 0) {
            clicksPerSecond = (new Sum().evaluate(data) / (samplePeriod * size)) * 20;
        } else {
            clicksPerSecond = 0.0;
        }
        return clicksPerSecond < criteria;
    }

    /**
     * @return the clicks per second value
     */
    public double getClicksPerSecond() {
        return clicksPerSecond;
    }

}
