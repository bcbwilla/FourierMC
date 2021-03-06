package xyz.benw.plugins.fouriermc.analysis;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import xyz.benw.plugins.fouriermc.ClickData;
import xyz.benw.plugins.fouriermc.analysis.datatests.ClicksPerSecond;
import xyz.benw.plugins.fouriermc.analysis.datatests.PatternDetection;
import xyz.benw.plugins.fouriermc.analysis.datatests.PatternDetectionMethod;
import xyz.benw.plugins.fouriermc.FourierMC;
import xyz.benw.plugins.fouriermc.IClickData;
import xyz.benw.plugins.fouriermc.player.PlayerData;
import xyz.benw.plugins.fouriermc.violation.*;
import xyz.benw.plugins.fouriermc.event.violation.AggregatedViolationEvent;
import xyz.benw.plugins.fouriermc.event.violation.ViolationEvent;

import java.util.*;

/**
 * Performs pass/fail datatests on player's clicking signal.
 * Check for clicking too quickly (ClicksPerSecond) and artificially (PatternDetection).
 *
 * @author bcbwilla
 */
public class QuantitativeAnalyzer implements Runnable {

    private FourierMC plugin;
    private PluginManager pluginManager;

    /**
     * Class constructor.
     * @param plugin instance of FourierMC plugin
     */
    public QuantitativeAnalyzer(FourierMC plugin) {
        this.plugin = plugin;
        this.pluginManager = Bukkit.getServer().getPluginManager();
    }

    /**
     * Run the datatests.
     */
    public void run() {
        if(!plugin.getPlayerDataMap().isEmpty()) {

            FileConfiguration config = plugin.getConfig();

            double cpsCritera = config.getDouble("tests.cps.value");
            double fisherCriteria = config.getDouble("tests.pattern.fisherp");
            double pdCpsCutoff = config.getDouble("tests.pattern.cpscutoff");

            /* Test each player */
            for (Map.Entry<UUID, PlayerData> entry : plugin.getPlayerDataMap().entrySet()) {
                UUID playerID = entry.getKey();

                for(ClickData data : entry.getValue().getClickSignals()) {

                    Player player = Bukkit.getPlayer(playerID);

                    if(!data.isEmpty()) {

                        double[] dataArray = data.toDoubleArray();

                        int n = dataArray.length;
                        double[] halfArray = Arrays.copyOfRange(dataArray, n/2, n); //CPS is more useful over shorter periods

                        ClicksPerSecond cps = new ClicksPerSecond(halfArray, plugin.getSamplePeriod());
                        boolean passedCPS = cps.evaluate(cpsCritera);
                        double cpsValue = cps.getClicksPerSecond();

                        if(!passedCPS) {

                            Violation violation = new Violation(ViolationType.CPS, cpsValue, data.getClickType());
                            ViolationEvent event = new ViolationEvent(player, violation);
                            pluginManager.callEvent(event);
                        }

                        if(cpsValue > pdCpsCutoff  && data.size() == data.getMaxLength()) { // PD is more useful over longer periods
                            // Do PatternDetection
                            PatternDetection pd = new PatternDetection(dataArray, PatternDetectionMethod.FISHER);
                            boolean passedPD = pd.evaluate(fisherCriteria);

                            if(!passedPD) {
                                Violation violation = new Violation(ViolationType.PATTERN, pd.getValue(), data.getClickType());
                                ViolationEvent event = new ViolationEvent(player, violation);
                                pluginManager.callEvent(event);
                            }

                        }

                        /* Handle aggregated violations */
                        for(ViolationType violationType : ViolationType.values()) {
                            handleAggregatedViolations(player, violationType);
                        }

                    }
                }
            }
        }
    }

    /**
     * Handled aggregated violations
     * @param player Player triggering violation
     * @param violationType violation type
     */
    private void handleAggregatedViolations(Player player, ViolationType violationType) {

        FileConfiguration config = plugin.getConfig();

        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        playerData.expireViolations(config.getInt("violationExpireAge"));

        List violationList = playerData.getViolations(violationType, true);

        if (violationList != null && violationList.size() > 0) {

            int timesFailed = violationList.size();
            Violation firstViolation = (Violation) violationList.get(0);
            Violation lastViolation = (Violation) violationList.get(timesFailed-1);

            int failedDuration = (int) (lastViolation.getTimestamp() - firstViolation.getTimestamp());

            double failedVelocity = 0;
            if(failedDuration > 0.0) {
                failedVelocity = ((double) timesFailed / failedDuration) * 1000.0 * 60;
            }

            String configPath = "tests." + violationType.toString().toLowerCase() + ".velocity";
            double criteria = plugin.getConfig().getDouble(configPath);

            if(failedVelocity > criteria) {
                AggregatedViolation aggregatedViolation = new AggregatedViolation(violationType, timesFailed, failedDuration);
                AggregatedViolationEvent event = new AggregatedViolationEvent(player, aggregatedViolation);
                pluginManager.callEvent(event);

                // Mark aggregated violations as not active
                for(int i = 0; i < timesFailed; i++) {
                    ((Violation) violationList.get(i)).setActive(false);
                }

            }

        }

    }

}
