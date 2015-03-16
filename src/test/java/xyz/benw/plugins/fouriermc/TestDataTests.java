package xyz.benw.plugins.fouriermc;

import org.junit.*;
import xyz.benw.plugins.fouriermc.dataanalysis.datatest.ClicksPerSecond;
import xyz.benw.plugins.fouriermc.dataanalysis.datatest.PatternDetection;
import xyz.benw.plugins.fouriermc.dataanalysis.datatest.PatternDetectionMethod;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the data analysis tests
 *
 * @author bcbwilla
 */
public class TestDataTests {

    // Make a lot of data signals to test.
    private double[] p02;
    private double[] p03;
    private double[] p05;
    private double[] p11;
    private double[] p12;
    private double[] r1;
    private double[] r2;
    private double[] r3;
    private double[] r4;
    private double[] r5;

    private double[] noise;

    private final long samplePeriod = 1L;
    private final int SIGNAL_LENGTH = 128;

    @Before
    public void setUp() throws Exception {
        // Code executed before each test
        // Initiate test classes here

         /*
            Build test data
         */

        // Periodic signals (e.g. autoclick)
        // Alternating 1s and 0s.  [1,0,1,0, ...]
        p02 = makePeriodicArray(2, SIGNAL_LENGTH);

        // Alternating 1s and 0s, twice the period  [1,0,0,1,0,0,1,0,0, ...]
        p03 = makePeriodicArray(3, SIGNAL_LENGTH);

        // Etc
        p05 = makePeriodicArray(5, SIGNAL_LENGTH);
        p11 = makePeriodicArray(11, SIGNAL_LENGTH);
        p12 = makePeriodicArray(12, SIGNAL_LENGTH);

        // Random clicking (e.g. humanish)
        r1 = makeRandomArray(128);
        r2 = makeRandomArray(128);
        r3 = makeRandomArray(128);
        r4 = makeRandomArray(128);
        r5 = makeRandomArray(128);
    }

    @Test
    public void testClicksPerSecond() {
        double criteria = 10;

        ClicksPerSecond cps = new ClicksPerSecond(p02, samplePeriod);
        cps.evaluate(criteria);
        double value = cps.getClicksPerSecond();
        assertEquals(10.0, value, 0.0001);

        cps = new ClicksPerSecond(p03, samplePeriod);
        cps.evaluate(criteria);
        value = cps.getClicksPerSecond();
        assertEquals(6.71875, value, 0.0001);

        cps = new ClicksPerSecond(p11, samplePeriod);
        cps.evaluate(criteria);
        value = cps.getClicksPerSecond();
        assertEquals(1.875, value, 0.0001);

    }

    @Test
    public void testPeriodicFisher() {

        double criteria = 0.001;

        // Test math details
        PatternDetection pd = new PatternDetection(makePeriodicArray(10, 32), PatternDetectionMethod.FISHER);
        pd.evaluate(criteria);
        assertEquals(0.273688983011, pd.getFisherGValue(), 0.00000001);
        assertEquals(0.168942550414, pd.getFisherPValue(), 0.0000001);

        pd = new PatternDetection(makePeriodicArray(50, 128), PatternDetectionMethod.FISHER);
        pd.evaluate(criteria);
        assertEquals(0.0488650790574, pd.getFisherGValue(), 0.00000001);
        assertEquals(0.981402503326, pd.getFisherPValue(), 0.0000001);


        // Test detection of artificial signal.
        pd = new PatternDetection(p02, PatternDetectionMethod.FISHER);
        assertEquals(false, pd.evaluate(criteria));

        pd = new PatternDetection(p03, PatternDetectionMethod.FISHER);
        assertEquals(false, pd.evaluate(criteria));

        pd = new PatternDetection(p05, PatternDetectionMethod.FISHER);
        assertEquals(false, pd.evaluate(criteria));

        pd = new PatternDetection(p11, PatternDetectionMethod.FISHER);
        assertEquals(false, pd.evaluate(criteria));

        pd = new PatternDetection(p12, PatternDetectionMethod.FISHER);
        assertEquals(false, pd.evaluate(criteria));


        // Test random clicking
        pd = new PatternDetection(r1, PatternDetectionMethod.FISHER);
        assertEquals(true, pd.evaluate(criteria));

        pd = new PatternDetection(r2, PatternDetectionMethod.FISHER);
        assertEquals(true, pd.evaluate(criteria));

        pd = new PatternDetection(r3, PatternDetectionMethod.FISHER);
        assertEquals(true, pd.evaluate(criteria));

        pd = new PatternDetection(r4, PatternDetectionMethod.FISHER);
        assertEquals(true, pd.evaluate(criteria));

        pd = new PatternDetection(r5, PatternDetectionMethod.FISHER);
        assertEquals(true, pd.evaluate(criteria));

    }


    /**
     * Generate periodic array of 0s and 1s.
     * @param period  the period of the signal
     *                e.g. period = 2 -> [1, 0, 1, 0, 1, 0, 1, 0, ...]
     *                     period = 3 -> [1, 0, 0, 1, 0, 0, 1, 0, ...]
     * @param length  the length of the array
     * @return
     */
    private double[] makePeriodicArray(int period, int length) {

        double[] p = new double[length];

        for(int i=0; i < p.length; i++){
            if(i % period==0){
                p[i] = 1;
            } else {
                p[i] = 0;
            }
        }
        return p;
    }

    /**
     * Generate array of random 0s and 1s
     * @param length  the length of the array
     * @return
     */
    private double[] makeRandomArray(int length) {

        Random r = new Random(1L); // Set seed so test is reproducible
        double[] p = new double[length];

        for(int i=0; i < p.length; i++){
            p[i] = (double) r.nextInt(2);
        }
        return p;
    }
}



