package xyz.benw.plugins.fouriermc.dataanalysis;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import xyz.benw.plugins.fouriermc.dataanalysis.datatest.ClicksPerSecond;
import xyz.benw.plugins.fouriermc.dataanalysis.datatest.PatternDetection;
import xyz.benw.plugins.fouriermc.dataanalysis.datatest.PatternDetectionMethod;
import xyz.benw.plugins.fouriermc.FourierMC;
import xyz.benw.plugins.fouriermc.IClickData;
import xyz.benw.plugins.fouriermc.violation.*;
import xyz.benw.plugins.fouriermc.event.violation.AggregatedViolationEvent;
import xyz.benw.plugins.fouriermc.event.violation.ViolationEvent;

import java.util.*;

/**
 * Performs pass/fail tests on player's clicking signal.
 * Check for clicking too quickly (ClicksPerSecond) and artificially (PatternDetection).
 *
 * @author bcbwilla
 */
public class QuantitativeAnalyzer implements Runnable {

    private FourierMC plugin;

    /**
     * Class constructor.
     * @param plugin instance of FourierMC plugin
     */
    public QuantitativeAnalyzer(FourierMC plugin) {
        this.plugin = plugin;
    }

    /**
     * Run the tests.
     */
    @Override
    public void run() {
        if(!plugin.clickLogger.isEmpty()) {

            FileConfiguration config = plugin.getConfig();

            double cpsCritera = config.getDouble("tests.cps.value");
            double fisherCriteria = config.getDouble("tests.pattern.fisherp");
            double pdCpsCutoff = config.getDouble("tests.pattern.cpscutoff");

            PluginManager pluginManager = Bukkit.getServer().getPluginManager();


            /* Test each player */
            for (Map.Entry<UUID, IClickData> entry : plugin.clickLogger.entrySet()) {
                UUID playerID = entry.getKey();
                IClickData data = entry.getValue();
                String name = Bukkit.getPlayer(playerID).getDisplayName();

                Player player = Bukkit.getPlayer(playerID);

                if(!data.isEmpty()) {

                    double[] dataArray = data.toDoubleArray();

                    int n = dataArray.length;
                    double[] halfArray = Arrays.copyOfRange(dataArray, n/2, n); //CPS is more useful over shorter periods

                    ClicksPerSecond cps = new ClicksPerSecond(halfArray, plugin.getSamplePeriod());
                    boolean passedCPS = cps.evaluate(cpsCritera);
                    double cpsValue = cps.getClicksPerSecond();

                    if(!passedCPS) {

                        Violation violation = new Violation(ViolationType.CPS, cpsValue);
                        ViolationEvent event = new ViolationEvent(player, violation);
                        pluginManager.callEvent(event);
                    }

                    if(cpsValue > pdCpsCutoff  && data.size() == data.getMaxLength()) { // PD is more useful over longer periods
                        // Do PatternDetection
                        PatternDetection pd = new PatternDetection(dataArray, PatternDetectionMethod.FISHER);
                        boolean passedPD = pd.evaluate(fisherCriteria);

                        if(!passedPD) {
                            Violation violation = new Violation(ViolationType.PATTERN, pd.getFisherPValue());
                            ViolationEvent event = new ViolationEvent(player, violation);
                            pluginManager.callEvent(event);
                        }

                    }


                    /* Handle aggregated violations */
                    for(ViolationType violationType : ViolationType.values()) {

                        Map violationMap;
                        if(plugin.violationLogger.containsKey(playerID)) {
                            violationMap = plugin.violationLogger.get(playerID);

                            List violationList;
                            if (violationMap.containsKey(violationType)) {
                                violationList = (ArrayList) violationMap.get(violationType);

                                int timesFailed = violationList.size();
                                Violation firstViolation = (Violation) violationList.get(0);
                                Violation lastViolation = (Violation) violationList.get(timesFailed-1);

                                int failedDuration = (int) (lastViolation.getTimestamp() - firstViolation.getTimestamp());

                                double failedVelocity = 0;
                                if(failedDuration > 0.0) {
                                    failedVelocity = ((double) timesFailed / failedDuration) * 1000.0 * 60;
                                }

                                String configPath = "tests." + violationType.toString().toLowerCase() + ".velocity";
                                double criteria = config.getDouble(configPath);

                                if(failedVelocity > criteria) {
                                    AggregatedViolation aggregatedViolation = new AggregatedViolation(violationType, timesFailed, failedDuration);
                                    AggregatedViolationEvent event = new AggregatedViolationEvent(player, aggregatedViolation);
                                    pluginManager.callEvent(event);

                                }

                            }

                        }

                    }


                }
            }
        }
    }

}
