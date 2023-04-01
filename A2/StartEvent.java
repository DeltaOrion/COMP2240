/**
 * File: StartEvent.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 *
 * Represents an event which causes the customer to start, i.e. signal to the parlor when it will arrive.
 */
public class StartEvent extends Event {
    public StartEvent(Customer customer, int time) {
        super(customer,time);
    }

    @Override
    public void run() {
        getCustomer().start();
    }
}
