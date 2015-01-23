package xyz.benw.plugins.fouriermc.Violations;

/**
 *  Super basic violation handling
 *  //TODO Something useful
 */
public class Violation {

    // Fields for the cumulative violation values
    private double cps = 0;  // Clicks per second
    private double pd = 0;   // Pattern detection

    public double getCPS() {
        return cps;
    }

    public double getPD() {
        return pd;
    }

    public void incrementCPS(double val) {
        cps = cps + val;
    }

    public void incrementPD(double val) {
        pd = pd + val;
    }

    public void setCPS(double cps) {
        this.cps = cps;
    }

    public void setPD(double pd) {
        this.pd = pd;
    }

}
