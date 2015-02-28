package xyz.benw.plugins.fouriermc.DataAnalysis;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.benw.plugins.fouriermc.DataAnalysis.DataTests.ClicksPerSecond;
import xyz.benw.plugins.fouriermc.DataAnalysis.DataTests.PatternDetection;
import xyz.benw.plugins.fouriermc.DataAnalysis.DataTests.PatternDetectionMethod;
import xyz.benw.plugins.fouriermc.FourierMC;
import xyz.benw.plugins.fouriermc.IClickData;
import xyz.benw.plugins.fouriermc.SignalLogger;
import xyz.benw.plugins.fouriermc.Violations.CPSViolation;
import xyz.benw.plugins.fouriermc.Violations.IViolation;
import xyz.benw.plugins.fouriermc.Violations.PatternViolation;
import xyz.benw.plugins.fouriermc.Violations.ViolationType;

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

            double cpsCritera = config.getDouble("tests.cps");
            double fisherCriteria = config.getDouble("tests.pattern.fisherp");
            double pdCpsCutoff = config.getDouble("tests.pattern.cpscutoff");

            /* Test each player */
            for (Map.Entry<UUID, IClickData> entry : plugin.clickLogger.entrySet()) {
                UUID playerId = entry.getKey();
                IClickData data = entry.getValue();
                String name = Bukkit.getPlayer(playerId).getDisplayName();

                Map violationMap;
                if(plugin.violationLogger.containsKey(playerId)) {
                    violationMap = plugin.violationLogger.get(playerId);
                } else {
                    violationMap = new HashMap<ViolationType, IViolation>();
                    plugin.violationLogger.put(playerId, violationMap);
                }

                if(!data.isEmpty()) {

                    double[] dataArray = data.toDoubleArray();

                    int n = dataArray.length;
                    double[] halfArray = Arrays.copyOfRange(dataArray, n/2, n); //CPS is more useful over shorter periods

                    ClicksPerSecond cps = new ClicksPerSecond(halfArray, plugin.getSamplePeriod());
                    boolean passedCPS = cps.evaluate(cpsCritera);
                    double cpsValue = cps.getClicksPerSecond();

                    CPSViolation cpsViolation;
                    if(!passedCPS) {
                        if(violationMap.containsKey(ViolationType.CPS)) {
                            cpsViolation = (CPSViolation) violationMap.get(ViolationType.CPS);
                        } else {
                            cpsViolation = new CPSViolation();
                            violationMap.put(ViolationType.CPS, cpsViolation);
                        }

                        cpsViolation.incrementValue(cpsValue);
                        plugin.getLogger().info(name + " failed CPS with a value of " + cpsValue);

                    }

                    if(cpsValue > pdCpsCutoff  && data.size() == data.getMaxLength()) { // PD is more useful over longer periods
                        // Do PatternDetection
                        PatternDetection pd = new PatternDetection(dataArray, PatternDetectionMethod.FISHER);
                        boolean passedPD = pd.evaluate(fisherCriteria);

                        if(!passedPD) {

                            PatternViolation patternViolation;
                            if(violationMap.containsKey(ViolationType.PATTERN)) {
                                patternViolation = (PatternViolation) violationMap.get(ViolationType.PATTERN);
                            } else {
                                patternViolation = new PatternViolation();
                                violationMap.put(ViolationType.CPS, patternViolation);
                            }

                            double value = pd.getFisherPValue();
                            patternViolation.incrementValue(value / fisherCriteria);
                            plugin.getLogger().info(name + " failed PD with a value of " + value);
                        }

                    }

                    if(plugin.getLogSignals()) {
                    SignalLogger.log(plugin, dataArray, playerId);
                    };
                }
            }
        }
    }

}
