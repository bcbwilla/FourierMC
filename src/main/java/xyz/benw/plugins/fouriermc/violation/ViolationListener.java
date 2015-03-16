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
        UUID playerID = event.getPlayer().getUniqueId();
        Violation violation = event.getViolation();
        ViolationType violationType = violation.getViolationType();

        Map violationMap;
        if(plugin.violationLogger.containsKey(playerID)) {
            violationMap = plugin.violationLogger.get(playerID);
        } else {
            violationMap = new HashMap<ViolationType, ArrayList<Violation>>();
            plugin.violationLogger.put(playerID, violationMap);
        }

        List violationList;
        if(violationMap.containsKey(violationType)) {
            violationList = (ArrayList) violationMap.get(violationType);
        } else {
            violationList = new ArrayList<Violation>();
            violationMap.put(violationType, violationList);
        }

        violationList.add(violation);


        if(plugin.getDebug()) {
            String msg = event.getPlayer().getDisplayName() + " logged a ";
            msg += violationType.toString() + " value of " + violation.getValue();
            plugin.getLogger().log(Level.INFO, msg);
        }

    }

    /**
     * Records when a player's violation record produces suspicious violations
     * @param event aggregated violation event
     */
    @EventHandler
    public void onAggregatedViolation(AggregatedViolationEvent event) {

        boolean log = plugin.getConfig().getBoolean("tests.logAggregated");

        if(log) {
            String playerName = event.getPlayer().getDisplayName();

            AggregatedViolation av = event.getAggregatedViolation();
            String violationType = av.getViolationType().toString();
            String timesFailed = Integer.toString(av.getTimesFailed());

            DecimalFormat df = new DecimalFormat("0.00");
            String failedDuration = df.format(av.getfailedDuration() / (1000.0));

            String msg = "Player " + playerName + " failed " + violationType + " " + timesFailed;
            msg += " times in the past " + failedDuration + " seconds.";

            plugin.getLogger().log(Level.INFO, msg);
        }

    }
}
