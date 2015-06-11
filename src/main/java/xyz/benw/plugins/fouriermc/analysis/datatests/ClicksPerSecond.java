package xyz.benw.plugins.fouriermc.analysis.datatests;

import org.apache.commons.math3.stat.descriptive.summary.Sum;

/**
 * Computes a player's clicks per second.
 *
 * @author bcbwilla
 */
public class ClicksPerSecond implements IDataTest {

    private double[] data;
    private double clicksPerSecond;

    private Long samplePeriod;

    /**
     * Class constructor.
     *
     * @param data          the clicking data to analyze
     * @param samplePeriod  the period of time that each data element represents
     */
    public ClicksPerSecond(double[] data, Long samplePeriod) {
        this.data = data;
        this.samplePeriod = samplePeriod;
        this.clicksPerSecond = computeClicksPerSecond();
    }

    /**
     * Computes the clicks per second
     */
    public double computeClicksPerSecond() {
        // *20 to convert from ticks to seconds
        int size = data.length;
        double cps;
        if(samplePeriod != 0 && size != 0) {
            cps = (new Sum().evaluate(data) / (samplePeriod * size)) * 20;
        } else {
            cps = 0.0;
        }
        return cps;
    }

    /**
     * Compares clicks per second to critera.
     *
     * @param criteria the value which is compared to the computed click speed. If computed value is larger,
     *                 the test is failed.
     * @return         true if test passed, false if test failed.
     *                 A failed test implies suspicious activity.
     */
    public boolean evaluate(double criteria) {
        return getClicksPerSecond() < criteria;
    }

    /**
     * @return the clicks per second value
     */
    public double getClicksPerSecond() {
        return clicksPerSecond;
    }

}
