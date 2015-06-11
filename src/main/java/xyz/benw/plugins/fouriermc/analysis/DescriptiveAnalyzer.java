package xyz.benw.plugins.fouriermc.analysis;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.bukkit.Bukkit;
import xyz.benw.plugins.fouriermc.ClickData;
import xyz.benw.plugins.fouriermc.analysis.datatests.ClicksPerSecond;
import xyz.benw.plugins.fouriermc.FourierMC;
import xyz.benw.plugins.fouriermc.IClickData;
import xyz.benw.plugins.fouriermc.player.PlayerData;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

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

    public void run() {
        if(!plugin.getPlayerDataMap().isEmpty()){

            for (Map.Entry<UUID, PlayerData> entry : plugin.getPlayerDataMap().entrySet()) {
                UUID playerId = entry.getKey();

                for(ClickData data : entry.getValue().getClickSignals()) {
                    String name = Bukkit.getPlayer(playerId).getDisplayName();

                    if(!data.isEmpty()) {

                        double[] dataArray = data.toDoubleArray();
                        double sum = new Sum().evaluate(dataArray);

                        if(sum != 0) {
                            double cps = new ClicksPerSecond(dataArray, plugin.getSamplePeriod()).getClicksPerSecond();
                            double mean = new Mean().evaluate(dataArray);
                            double max = new Max().evaluate(dataArray);
                            double std = new StandardDeviation().evaluate(dataArray);

                            Logger logger = plugin.getLogger();
                            String msg = "Descriptive Analysis of " + data.getClickType().toString();
                            msg +=  " clicking for " + name + ":";
                            logger.info(msg);
                            logger.info(" Size: " + Integer.toString(data.size()));
                            logger.info(" Max:  " + Double.toString(max));
                            logger.info(" Mean: " + Double.toString(mean));
                            logger.info(" Sum:  " + Double.toString(sum));
                            logger.info(" Std:  " + Double.toString(std));
                            logger.info(" CPS:  " + cps);
                        }
                    }
                }
            }
        }
    }
}
