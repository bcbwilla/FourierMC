package xyz.benw.plugins.fouriermc.DataAnalysis;

import xyz.benw.plugins.fouriermc.ClickData;
import xyz.benw.plugins.fouriermc.DataAnalysis.DataTests.ClicksPerSecond;
import xyz.benw.plugins.fouriermc.DataAnalysis.DataTests.FourierTransform;
import xyz.benw.plugins.fouriermc.FourierMC;

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

        for (Map.Entry<UUID, ClickData> entry : plugin.clickLogger.entrySet()) {
            UUID playerId = entry.getKey();
            ClickData data = entry.getValue();

            if(!data.isEmpty()) {
                // Tests players for clicking too quickly
                boolean passedCPS = new ClicksPerSecond(data, plugin.getSamplePeriod()).evaluate();

                // Do FourierTransform
                boolean passedFFT = new FourierTransform(data).evaluate();

                // Log stuff if failed, etc.
            }
        }


    }

}
