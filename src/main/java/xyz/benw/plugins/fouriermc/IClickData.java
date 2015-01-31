package xyz.benw.plugins.fouriermc;

/**
 * Interface for container of player clicking data.
 *
 * @author bcbwilla
 */
public interface IClickData {

    /**
     * Add an element to the queue.
     * @param x  the element to add to the queue.
     */
    public void add(int x);

    /**
     * Increment current value (the last value of the queue)
     */
    public void increment();

    /**
     * Increment current value (the last value of the queue) by amount x
     * @param x the amount to add to current value
     */
    public void incrementBy(int x);

    /**
     * @return true if queue empty
     */
    public boolean isEmpty();

    /**
     * @return size of data queue
     */
    public int size();

    /**
     * Dump contents into an Integer array.
     *
     * @return clicking data in Integer array
     */
    public Integer[] toArray();

    /**
     * Dump contents into a double array.
     *
     * @return clicking data in double array
     */
    public double[] toDoubleArray();

    /**
     * @return the fixed length of the queue
     */
    public int getMaxLength();

}
