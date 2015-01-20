package xyz.benw.plugins.fouriermc;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.summary.Sum;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;

/**
 * ClickData
 *
 * Container for clicking data.
 * Basically just wraps an ArrayDeque
 * and adds some extra methods.
 *
 * This object represents a clicking signal, e.g.
 * [0,0,1,0,2,1,0,1] etc., where each element represents
 * the number of clicks that occurred during that
 * sampling interval.
 *
 */
public class ClickData {

    private ArrayDeque<Integer> data = new ArrayDeque<Integer>();

    /* Maximum number of sample periods to store */
    private int MAX_DATA_LENGTH = 127;

    private long samplePeriod;

    public ClickData(long samplePeriod) {
        this.samplePeriod = samplePeriod;
    }

    /*
        Convenience constructor to build ClickData from array.
        Mostly for testing purposes.
     */
    public ClickData(long samplePeriod, Integer[] array) {
        this.samplePeriod = samplePeriod;

        for(Integer val : array) {
            data.add(val);
        }
    }


    public void add(int x) {
        if(data.size() > MAX_DATA_LENGTH) {
            data.removeFirst();
        }
        data.addLast(x);
    }


    public void increment() {
        if(!data.isEmpty()) {
            int current = data.removeLast();
            data.addLast(current + 1);
        }
    }


    public void incrementBy(int x) {
        if(!data.isEmpty()) {
            int current = data.removeLast();
            data.addLast(current + x);
        }
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public int size() {
        return data.size();
    }


    public Integer[] toArray() {
        Object[] a = data.toArray();

        Integer[] out = new Integer[a.length];
        for(int i = 0; i < a.length; i++) {
           out[i] = (Integer) a[i];
        }
        return out;
    }


    public double[] toDoubleArray() {
        Object[] a = data.toArray();

        double[] out = new double[a.length];
        for(int i = 0; i < a.length; i++) {
            out[i] = (double) ((Integer) a[i]).intValue();
        }
        return out;
    }

    public int max() {
        return Collections.max(Arrays.asList(toArray()));
    }

    public double sum() {
        return new Sum().evaluate(toDoubleArray());
    }

    public double mean() {
        return new Mean().evaluate(toDoubleArray());
    }

    public double standardDeviation() {
        return new StandardDeviation().evaluate(toDoubleArray());
    }

    /* There is also a formal test for this, but leaving this here for now as well */
    public double clicksPerSecond() {
        /* samplePeriod is in ticks, so need to convert to seconds. */
        return sum() / (samplePeriod*data.size())*20;
    }

}

