package xyz.benw.plugins.fouriermc.Violations;

/**
 * Violation interface
 *
 *  @author bcbwilla
 */
public interface IViolation {

    public double getValue();

    public void setValue(double val);

    public void incrementValue(double val);

}
