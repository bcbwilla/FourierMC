package xyz.benw.plugins.fouriermc.player;

import xyz.benw.plugins.fouriermc.ClickData;
import xyz.benw.plugins.fouriermc.FourierMC;
import xyz.benw.plugins.fouriermc.violation.AggregatedViolation;
import xyz.benw.plugins.fouriermc.violation.Violation;
import xyz.benw.plugins.fouriermc.violation.ViolationType;

import java.util.*;

/**
 * Class to store all of the player data related to FourierMC
 * Includes clicking signal and violations
 *
 * @author bcbwilla
 */
public class PlayerData {

    private FourierMC plugin;

    /* Violations accumualted by player */
    private Map<ViolationType, List<Violation>> violations = new HashMap<ViolationType, List<Violation>>();
    /* Aggregated violations accumulated by player */
    private Map<ViolationType, List<AggregatedViolation>> aggregatedViolations = new HashMap<ViolationType, List<AggregatedViolation>>();

    /* Player's clicking signal data */
    public ClickData clickSignal;

    /**
     * Class constructor.
     * @param plugin FourierMC instance
     */
    public PlayerData(FourierMC plugin) {
        this.plugin = plugin;
        clickSignal = new ClickData(plugin.getMaxDataLength());
    }

    public Map getViolations() {
        return violations;
    }

    public ArrayList<Violation> getViolations(ViolationType violationType) {
        if(violations.containsKey(violationType)) {
            return (ArrayList) violations.get(violationType);
        } else {
            return null;
        }
    }

    public void addViolation(ViolationType violationType, Violation violation) {
        if(violations.containsKey(violationType)) {
            violations.get(violationType).add(violation);
        } else {
            List violationList = new ArrayList<Violation>();
            violationList.add(violation);
            violations.put(violationType, violationList);
        }
    }

    public Map getAggregatedViolations() {
        return aggregatedViolations;
    }

    public ArrayList<AggregatedViolation> getAggregatedViolations(ViolationType violationType) {
        if(aggregatedViolations.containsKey(violationType)) {
            return (ArrayList) aggregatedViolations.get(violationType);
        } else {
            return null;
        }

    }

    public void addAggregatedViolation(ViolationType violationType, AggregatedViolation aggregatedViolation) {
        if(aggregatedViolations.containsKey(violationType)) {
            aggregatedViolations.get(violationType).add(aggregatedViolation);
        } else {
            List aggregatedViolationList = new ArrayList<Violation>();
            aggregatedViolationList.add(aggregatedViolation);
            aggregatedViolations.put(violationType, aggregatedViolationList);
        }
    }

    public ClickData getClickSignal() {
        return clickSignal;
    }

}
