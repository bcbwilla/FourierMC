package xyz.benw.plugins.fouriermc.DataAnalysis.DataTests;

/**
 * IDataTest
 *
 * Interface for all tests on clicking data.
 * A data test tests one specific thing.
 */
public interface IDataTest {

    /* Evaluate the test */
    public boolean evaluate(double critera);

}
