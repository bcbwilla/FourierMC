package xyz.benw.plugins.fouriermc.analysis;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.bukkit.Bukkit;
import xyz.benw.plugins.fouriermc.analysis.datatests.ClicksPerSecond;
import xyz.benw.plugins.fouriermc.FourierMC;
import xyz.benw.plugins.fouriermc.IClickData;

import java.util.Map;
import java.util.UUID;

/**
 * Logs descriptive statistics about a player's signal.
 * Does not actually perform any datatests.
 * Mostly used for debugging.
 *
 * @author bcbwilla
 */
public class DescriptiveAnalyzer implements Runnable {

    private FourierMC plugin;

    public DescriptiveAnalyzer(FourierMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if(!plugin.clickLogger.isEmpty()){

            for (Map.Entry<UUID, IClickData> entry : plugin.clickLogger.entrySet()) {
                UUID playerId = entry.getKey();
                IClickData data = entry.getValue();
                String name = Bukkit.getPlayer(playerId).getDisplayName();

                if(!data.isEmpty()) {

                    double[] dataArray = data.toDoubleArray();
                    double sum = new Sum().evaluate(dataArray);

                    if(sum != 0) {
                        double cps = new ClicksPerSecond(dataArray, plugin.getSamplePeriod()).getClicksPerSecond();
                        double mean = new Mean().evaluate(dataArray);
                        double max = new Max().evaluate(dataArray);
                        double std = new StandardDeviation().evaluate(dataArray);

                        plugin.getLogger().info("Descriptive Analysis for " + name + ":");
                        plugin.getLogger().info(" Size: " + Integer.toString(data.size()));
                        plugin.getLogger().info(" Max:  " + Double.toString(max));
                        plugin.getLogger().info(" Mean: " + Double.toString(mean));
                        plugin.getLogger().info(" Sum:  " + Double.toString(sum));
                        plugin.getLogger().info(" Std:  " + Double.toString(std));
                        plugin.getLogger().info(" CPS:  " + cps);
                    }
                }
            }
        }
    }
}
