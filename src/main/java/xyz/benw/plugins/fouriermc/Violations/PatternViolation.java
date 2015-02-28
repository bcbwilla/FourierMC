package xyz.benw.plugins.fouriermc.Violations;

/**
 * Pattern detection violation
 *
 *  @author bcbwilla
 */
public class PatternViolation implements IViolation {


    private double patternValue = 0;


    @Override
    public void setValue(double val) {
        patternValue = val;
    }

    @Override
    public double getValue() {
        return patternValue;
    }

    @Override
    public void incrementValue(double val) {
        patternValue = patternValue + val;
    }
}

