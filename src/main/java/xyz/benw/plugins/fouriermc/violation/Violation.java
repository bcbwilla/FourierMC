package xyz.benw.plugins.fouriermc.violation;

/**
 * A failed clicking test results in a violation
 *
 * @author bcbwilla
 */
public class Violation extends BaseViolation {

    private ViolationType violationType;
    private double value;
    private Long timestamp;
    private boolean active = true;

    public Violation(ViolationType violationType, double value) {
        super(violationType);
        this.value = value;

        this.timestamp = System.currentTimeMillis();
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean set) {
        active = set;
    }

}
