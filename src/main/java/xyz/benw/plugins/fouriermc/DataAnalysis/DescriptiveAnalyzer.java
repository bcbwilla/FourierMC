package xyz.benw.plugins.fouriermc.DataAnalysis;

import xyz.benw.plugins.fouriermc.ClickData;
import xyz.benw.plugins.fouriermc.FourierMC;

/**
 * Descriptive Analyzer
 *
 * Logs descriptive statistics about a player's signal
 * Does not actually perform any tests
 */
public class DescriptiveAnalyzer implements Runnable {

    private FourierMC plugin;

    public DescriptiveAnalyzer(FourierMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

        if(!plugin.clickLogger.isEmpty()){

            for(ClickData data : plugin.clickLogger.values()) {

                if(!data.isEmpty()) {
                    plugin.getLogger().info("Descriptive Analysis:");
                    plugin.getLogger().info(" Size: " + Integer.toString(data.size()));
                    plugin.getLogger().info(" Max:  " + Integer.toString(data.max()));
                    plugin.getLogger().info(" Mean: " + Double.toString(data.mean()));
                    plugin.getLogger().info(" Sum:  " + Double.toString(data.sum()));
                    plugin.getLogger().info(" Std:  " + Double.toString(data.standardDeviation()));
                    plugin.getLogger().info(" CPS:  " + Double.toString(data.clicksPerSecond()));
                }
            }
        }
    }
}
