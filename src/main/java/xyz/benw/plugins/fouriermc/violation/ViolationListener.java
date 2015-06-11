package xyz.benw.plugins.fouriermc.violation;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.benw.plugins.fouriermc.FourierMC;
import xyz.benw.plugins.fouriermc.event.violation.AggregatedViolationEvent;
import xyz.benw.plugins.fouriermc.event.violation.ViolationEvent;

import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;

/**
 * Listens for violation and aggregated violation events
 *
 * @author bcbwilla
 */
public class ViolationListener implements Listener {

    private FourierMC plugin;

    /**
     * Class constructor
     * @param plugin instance of FouirerMC plugin
     */
    public ViolationListener(FourierMC plugin) {
        this.plugin = plugin;
    }

    /**
     * Records when a player violates one of the clicking tests
     * @param event violation event
     */
    @EventHandler
    public void onViolation(ViolationEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        Violation violation = event.getViolation();
        ViolationType violationType = violation.getViolationType();

        plugin.getPlayerData(playerId).addViolation(violationType, violation);

        if(plugin.getDebug()) {
            String msg = event.getPlayer().getDisplayName() + " logged a ";
            msg += violationType.toString() + " value of ";
            msg += String.format("%.02f", violation.getValue());
            msg += " for " + violation.getClickType().toString() + " clicking.";
            plugin.getLogger().log(Level.INFO, msg);
        }

    }

    /**
     * Records when a player's violation record produces suspicious violations
     * @param event aggregated violation event
     */
    @EventHandler
    public void onAggregatedViolation(AggregatedViolationEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        AggregatedViolation av = event.getAggregatedViolation();
        ViolationType violationType = av.getViolationType();

        plugin.getPlayerData(playerId).addAggregatedViolation(violationType, av);

        boolean log = plugin.getConfig().getBoolean("tests.logAggregated");

        if(log) {
            String playerName = event.getPlayer().getDisplayName();
            String timesFailed = Integer.toString(av.getTimesFailed());

            DecimalFormat df = new DecimalFormat("0.00");
            String failedDuration = df.format(av.getfailedDuration() / (1000.0));

            String msg = "Player " + playerName + " failed " + violationType.toString() + " " + timesFailed;
            msg += " times in the past " + failedDuration + " seconds.";

            plugin.getLogger().log(Level.INFO, msg);
        }

    }
}
