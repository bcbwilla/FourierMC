package xyz.benw.plugins.fouriermc.violation;

/**
 * Base class for violations
 *
 * @author bcbwilla
 */
public class AbstractViolation {

    private ViolationType violationType;

    public ViolationType getViolationType() {
        return violationType;
    }

    public void setViolationType(ViolationType violationType) {
        this.violationType = violationType;
    }
}
