package xyz.benw.plugins.fouriermc;

import xyz.benw.plugins.fouriermc.violation.ClickType;

import java.util.ArrayDeque;

/**
 * Container for player clicking data.
 * Basically just wraps an ArrayDeque and adds some extra methods.
 *
 * The clicking data is stored as, for example, [0,0,1,0,2,1,0,1,...].
 * Each element represents a samplePeriod, and the value is the number of clicks that occurred in that period.
 *
 * @author bcbwilla
 */
public class ClickData implements IClickData {

    private ArrayDeque<Integer> data = new ArrayDeque<Integer>();

    private ClickType clickType;

    private final int MAX_DATA_LENGTH;

    /**
     * Class constructor
     *
     * @param maxLength the length of the queue.
     *                  If more data is added, the queue will "roll over".
     * @param clickType the type of click (left or right)
     */
    public ClickData(int maxLength, ClickType clickType) {
        this.MAX_DATA_LENGTH = maxLength;
        this.clickType = clickType;
    }

    /**
     * Add an element to the queue.
     * @param x  the element to add to the queue.
     */
    public void add(int x) {
        if(data.size() >= MAX_DATA_LENGTH) {
            data.removeFirst();
        }
        data.addLast(x);
    }

    /**
     * Increment current value (the last value of the queue)
     */
    public void increment() {
        if(!data.isEmpty()) {
            int current = data.removeLast();
            data.addLast(current + 1);
        }
    }

    /**
     * Increment current value (the last value of the queue) by amount x
     * @param x the amount to add to current value
     */
    public void incrementBy(int x) {
        if(!data.isEmpty()) {
            int current = data.removeLast();
            data.addLast(current + x);
        }
    }

    /**
     * @return true if queue empty
     */
    public boolean isEmpty() {
        return data.isEmpty();
    }

    /**
     * @return size of data queue
     */
    public int size() {
        return data.size();
    }

    /**
     * Dump contents into an Integer array.
     *
     * @return clicking data in Integer array
     */
    public Integer[] toArray() {
        Object[] a = data.toArray();

        Integer[] out = new Integer[a.length];
        for(int i = 0; i < a.length; i++) {
           out[i] = (Integer) a[i];
        }
        return out;
    }

    /**
     * Dump contents into a double array.
     *
     * @return clicking data in double array
     */
    public double[] toDoubleArray() {
        Object[] a = data.toArray();

        double[] out = new double[a.length];
        for(int i = 0; i < a.length; i++) {
            out[i] = (double) ((Integer) a[i]).intValue();
        }
        return out;
    }

    /**
     * @return the fixed length of the queue
     */
    public int getMaxLength() {
        return MAX_DATA_LENGTH;
    }

    /**
     * @return click type
     */
    public ClickType getClickType() {
        return clickType;
    }

}