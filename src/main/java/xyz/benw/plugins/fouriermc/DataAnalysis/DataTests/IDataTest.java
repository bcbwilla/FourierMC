package xyz.benw.plugins.fouriermc.DataAnalysis.DataTests;

/**
 * Interface for all tests on clicking data.
 * Each implementation should perform a specific test.
 *
 * @author bcbwilla
 */
public interface IDataTest {

    /**
     * Evaluates a test, comparing a computed property to some criteria.
     *
     * @param critera  the value which is compared to the computed property.
     * @return         true if test passed, false if test failed.
     *                 A failed test implies suspicious activity.
     */
    public boolean evaluate(double critera);

}
