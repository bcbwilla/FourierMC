package xyz.benw.plugins.fouriermc;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.summary.Sum;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by ben on 16/01/15.
 *
 * Container for data.
 * Basically just wraps an ArrayDeque
 * and adds some extra methods.
 *
 */
public class ClickData {

    private ArrayDeque<Integer> data = new ArrayDeque<Integer>();

    private int MAX_DATA_LENGTH = 127;

    private long samplePeriod;

    public ClickData(long samplePeriod) {
        this.samplePeriod = samplePeriod;
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

    public double clicksPerSecond() {
        // samplePeriod is in ticks, so need to convert to seconds.
        return sum() / (samplePeriod*data.size())*20;
    }

}

