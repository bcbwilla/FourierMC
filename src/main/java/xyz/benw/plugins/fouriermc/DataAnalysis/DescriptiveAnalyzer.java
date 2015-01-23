package xyz.benw.plugins.fouriermc.DataAnalysis;

import org.bukkit.Bukkit;
import xyz.benw.plugins.fouriermc.ClickData;
import xyz.benw.plugins.fouriermc.DataAnalysis.DataTests.ClicksPerSecond;
import xyz.benw.plugins.fouriermc.FourierMC;

import java.util.Map;
import java.util.UUID;

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

            for (Map.Entry<UUID, ClickData> entry : plugin.clickLogger.entrySet()) {
                UUID playerId = entry.getKey();
                ClickData data = entry.getValue();
                String name = Bukkit.getPlayer(playerId).getDisplayName();

                if(!data.isEmpty()) {

                    double sum = data.sum();
                    if(sum != 0) {
                        double cps = new ClicksPerSecond(data.toDoubleArray(), plugin.getSamplePeriod()).getClicksPerSecond();

                        plugin.getLogger().info("Descriptive Analysis for " + name + ":");
                        plugin.getLogger().info(" Size: " + Integer.toString(data.size()));
                        plugin.getLogger().info(" Max:  " + Integer.toString(data.max()));
                        plugin.getLogger().info(" Mean: " + Double.toString(data.mean()));
                        plugin.getLogger().info(" Sum:  " + Double.toString(sum));
                        plugin.getLogger().info(" Std:  " + Double.toString(data.standardDeviation()));
                        plugin.getLogger().info(" CPS:  " + cps);
                    }
                }
            }
        }
    }
}
