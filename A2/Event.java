/**
 * File: Event.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 *
 * Represents an event that a customer can do. Events should be ordered
 * by the timestamp they occur.
 */
public abstract class Event implements Comparable<Event> {

    private final Customer customer;
    private final int time;

    /**
     * Creates a new event
     *
     * @param customer The customer involved in the event
     * @param time The time the event occurs
     */
    protected Event(Customer customer, int time) {
        this.customer = customer;
        this.time = time;
    }

    /**
     * Does the actions of the event. This should only be called when the time-stamp is actually applicable. If this
     * is called before the timestamp the code will still run.
     */
    public abstract void run();

    /**
     * Comparator for events. Events should be strictly ordered by the time they occur
     *
     * @param o The other event
     */
    @Override
    public int compareTo(Event o) {
        return Integer.compare(this.time, o.time);
    }

    /**
     * @return The time this event occurs
     */
    public int getTime() {
        return time;
    }

    /**
     * @return The customer involved in this event
     */
    public Customer getCustomer() {
        return customer;
    }
}
