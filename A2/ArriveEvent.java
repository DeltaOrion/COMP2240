/**
 * File: ArriveEvent.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 *
 * Represents an event where a customer arrives at a particular timestamp.
 */
public class ArriveEvent extends Event {

    protected ArriveEvent(Customer customer, int time) {
        super(customer, time);
    }

    @Override
    public void run() {
        //have the customer arrive at the parlor.
        getCustomer().arrive();
    }
}
