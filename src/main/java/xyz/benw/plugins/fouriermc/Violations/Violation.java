package xyz.benw.plugins.fouriermc.Violations;

/**
 * A failed clicking test results in a violation
 *
 * @author bcbwilla
 */
public class Violation {

    private ViolationType violationType;
    private double value;
    private Long timestamp;

    public Violation(ViolationType violationType, double value) {
        this.violationType = violationType;
        this.value = value;

        this.timestamp = System.currentTimeMillis();
    }

    public void setViolationType(ViolationType violationType) {
        this.violationType = violationType;
    }

    public ViolationType getViolationType() {
        return violationType;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public Long getTimestamp() {
        return timestamp;
    }

}
