package xyz.benw.plugins.fouriermc.DataAnalysis.DataTests;

import xyz.benw.plugins.fouriermc.ClickData;

/**
 * ClicksPerSecond
 *
 * Computes a player's clicks per second.
 *
 */
public class ClicksPerSecond implements IDataTest {

    private ClickData data;

    private double result;
    private Long samplePeriod;


    public ClicksPerSecond(ClickData data, Long samplePeriod) {
        this.data = data;
        this.samplePeriod = samplePeriod;
    }

    @Override
    public boolean evaluate() {
        // *20 to convert from ticks to seconds
        result = data.sum() / (samplePeriod*data.size())*20;

        return result < 5;  //TODO: Put test criterias in config
    }

}
