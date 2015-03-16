package xyz.benw.plugins.fouriermc.Violations;

/**
 * Meta information about violations.
 *
 * @author bcbwilla
 */
public class AggregatedViolation {

    private ViolationType violationType;
    private int timesFailed;
    private int failedDuration;
    private double failedVelocity;

    /**
     * Class constructor.
     * @param violationType type of Violation
     * @param timesFailed the number of violations of a given type a player has
     * @param failedDuration the time in which timesFailed violations occured
     */
    public AggregatedViolation(ViolationType violationType, int timesFailed, int failedDuration) {
        this.violationType = violationType;
        this.timesFailed = timesFailed;
        this.failedDuration = failedDuration;

        this.failedVelocity = 0;
        if(failedDuration != 0) {
            this.failedVelocity = (timesFailed / failedDuration) * 1000.0; // Convert ms to seconds
        }
    }

    public void setViolationType(ViolationType violationType) {
        this.violationType = violationType;
    }

    public ViolationType getViolationType() {
        return violationType;
    }

    public int getTimesFailed() {
        return timesFailed;
    }

    public int getfailedDuration() {
        return failedDuration;
    }

    public double getFailedVelocity() {
        return failedVelocity;
    }
}
