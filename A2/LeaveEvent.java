/**
 * File: LeaveEvent.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 *
 * Represents an event where a customer leaves the parlor.
 */
public class LeaveEvent extends Event {

    protected LeaveEvent(Customer customer, int time) {
        super(customer, time);
    }

    @Override
    public void run() {
        //have the customer leave the timestamp.
        getCustomer().leave();
    }
}
