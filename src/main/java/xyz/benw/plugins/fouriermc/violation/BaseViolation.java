package xyz.benw.plugins.fouriermc.violation;

/**
 * Base class for violations
 *
 * @author bcbwilla
 */
public class BaseViolation {

    private ViolationType violationType;

    public BaseViolation(ViolationType violationType) {
        this.violationType = violationType;
    }

    public ViolationType getViolationType() {
        return violationType;
    }

    public void setViolationType(ViolationType violationType) {
        this.violationType = violationType;
    }

}
