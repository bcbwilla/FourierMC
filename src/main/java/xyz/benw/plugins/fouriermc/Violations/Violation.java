package xyz.benw.plugins.fouriermc.Violations;

/**
 *  Super basic violation handling
 *
 *  @author bcbwilla
 */
public class Violation {

    private double cps = 0;  // Clicks per second
    private double pd = 0;   // Pattern detection

    /**
     * @return clicks per second score
     */
    public double getCPS() {
        return cps;
    }

    /**
     * @return periodic detection score
     */
    public double getPD() {
        return pd;
    }

    /**
     * Increment clicks per second score
     * @param val the value to add to current cps score
     */
    public void incrementCPS(double val) {
        cps = cps + val;
    }

    /**
     * Increment pattern detection score
     * @param val the value to add to current pd score
     */
    public void incrementPD(double val) {
        pd = pd + val;
    }

    /**
     * @param cps the value to assign to cps
     */
    public void setCPS(double cps) {
        this.cps = cps;
    }

    /**
     * @param pd the value to assign to pd
     */
    public void setPD(double pd) {
        this.pd = pd;
    }

}
