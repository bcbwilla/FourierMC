package xyz.benw.plugins.fouriermc.DataAnalysis;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.benw.plugins.fouriermc.ClickData;
import xyz.benw.plugins.fouriermc.DataAnalysis.DataTests.ClicksPerSecond;
import xyz.benw.plugins.fouriermc.DataAnalysis.DataTests.PatternDetection;
import xyz.benw.plugins.fouriermc.DataAnalysis.DataTests.PatternDetectionMethod;
import xyz.benw.plugins.fouriermc.FourierMC;
import xyz.benw.plugins.fouriermc.Violations.Violation;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

/**
 * QuantativeAnalyzer
 *
 * Performs pass/fail tests on player's
 * clicking signal.
 *
 */
public class QuantitativeAnalyzer implements Runnable {

    private FourierMC plugin;

    public QuantitativeAnalyzer(FourierMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if(!plugin.clickLogger.isEmpty()) {

            FileConfiguration config = plugin.getConfig();

            double cpsCritera = config.getDouble("tests.cps");
            double fisherCriteria = config.getDouble("tests.pattern.fisherp");
            double pdCpsCutoff = config.getDouble("tests.pattern.cpscutoff");

            for (Map.Entry<UUID, ClickData> entry : plugin.clickLogger.entrySet()) {
                UUID playerId = entry.getKey();
                ClickData data = entry.getValue();
                String name = Bukkit.getPlayer(playerId).getDisplayName();

                Violation violation;
                if(plugin.violationLogger.containsKey(playerId)) {
                    violation = plugin.violationLogger.get(playerId);
                } else {
                    violation = new Violation();
                    plugin.violationLogger.put(playerId, violation);
                }

                if(!data.isEmpty()) {

                    double[] dataArray = data.toDoubleArray();

                    int n = dataArray.length;
                    double[] halfArray = Arrays.copyOfRange(dataArray, n/2, n);

                    ClicksPerSecond cps = new ClicksPerSecond(halfArray, plugin.getSamplePeriod());
                    boolean passedCPS = cps.evaluate(cpsCritera);
                    double cpsValue = cps.getClicksPerSecond();

                    if(!passedCPS) {
                        violation.incrementCPS(cpsValue);
                        plugin.getLogger().info(name + " failed CPS with a value of " + cpsValue);

                    }

                    if(cpsValue > pdCpsCutoff  && data.size() == data.getMaxLength()) {
                        // Do PatternDetection
                        PatternDetection pd = new PatternDetection(dataArray, PatternDetectionMethod.FISHER);
                        boolean passedPD = pd.evaluate(fisherCriteria);

                        if(!passedPD) {
                            double value = pd.getFisherPValue();
                            violation.incrementPD(value/fisherCriteria);
                            plugin.getLogger().info(name + " failed PD with a value of " + value);
                        }

                    }

                }

            }
        }
    }

}
