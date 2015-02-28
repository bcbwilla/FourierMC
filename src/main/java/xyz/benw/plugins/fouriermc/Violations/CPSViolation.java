package xyz.benw.plugins.fouriermc.Violations;

/**
 * Clicks per second violation
 *
 *  @author bcbwilla
 */
public class CPSViolation implements IViolation {

    private double cpsValue = 0;

    @Override
    public void setValue(double val) {
        cpsValue = val;
    }

    @Override
    public double getValue() {

        return cpsValue;
    }

    @Override
    public void incrementValue(double val) {
        cpsValue = cpsValue + val;
    }


}
