package xyz.benw.plugins.fouriermc.player;

import xyz.benw.plugins.fouriermc.ClickData;
import xyz.benw.plugins.fouriermc.FourierMC;
import xyz.benw.plugins.fouriermc.violation.AbstractViolation;
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

    /**
     * Returns all violations
     * @return map of all violations
     */
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

    /**
     * Returns a list of violations with active status active
     * @param active the status of the violations to return
     * @return list of all violations with status matching active
     */
    public ArrayList<Violation> getViolations(ViolationType violationType, boolean active) {
        if(violations.containsKey(violationType)) {
            ArrayList<Violation> violationList = new ArrayList<Violation>();

            for(Violation v : violations.get(violationType)) {
                if(v.isActive() == active) {
                    violationList.add(v);
                }
            }
            return violationList;

        } else {
            return null;
        }
    }

    /**
     * Sets old violations to inactive so they are not aggregated.
     * @param expireAge age (in seconds) at which a violation is set to inactive
     */
    public void expireViolations(int expireAge) {

        for(ViolationType vt : ViolationType.values()) {

            for(Violation v : getViolations(vt, true)) {
                int age = (int) (System.currentTimeMillis() - v.getTimestamp());

                if(age > expireAge*1000) {
                    v.setActive(false);
                }

            }
        }
    }

    /**
     * Add a violation to player's record
     * @param violationType violation type
     * @param violation violation to add
     */
    public void addViolation(ViolationType violationType, Violation violation) {
        if(violations.containsKey(violationType)) {
            violations.get(violationType).add(violation);
        } else {
            List violationList = new ArrayList<Violation>();
            violationList.add(violation);
            violations.put(violationType, violationList);
        }
    }

    /**
     * Returns all aggregated violations
     * @return map of all aggregated violations
     */
    public Map getAggregatedViolations() {
        return aggregatedViolations;
    }

    /**
     * Returns a list of aggregated violations
     * @param violationType type of violation
     * @return list of all aggregated violations matching violationType
     */
    public ArrayList<AggregatedViolation> getAggregatedViolations(ViolationType violationType) {
        if(aggregatedViolations.containsKey(violationType)) {
            return (ArrayList) aggregatedViolations.get(violationType);
        } else {
            return null;
        }

    }

    /**
     * Add an aggregated violation to a player's record
     * @param violationType type of violation
     * @param aggregatedViolation the aggregated violation
     */
    public void addAggregatedViolation(ViolationType violationType, AggregatedViolation aggregatedViolation) {
        if(aggregatedViolations.containsKey(violationType)) {
            aggregatedViolations.get(violationType).add(aggregatedViolation);
        } else {
            List aggregatedViolationList = new ArrayList<Violation>();
            aggregatedViolationList.add(aggregatedViolation);
            aggregatedViolations.put(violationType, aggregatedViolationList);
        }
    }

    /**
     * Returns the player's clicking data signal
     * @return clicking data signal
     */
    public ClickData getClickSignal() {
        return clickSignal;
    }

}
