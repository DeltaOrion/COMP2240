import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * File: TestParlorInputConfig.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * A simple class for making test inputs for the simulation quickly. I left this in for the report as it requires
 * discussion of testing.
 */
public class TestParlorInputConfig implements ParlorInputConfig {

    private final List<Customer> customers = new ArrayList<>();

    public void addCustomer(IceCreamParlor parlor, String name, int arrivalTime, int eatingTime, boolean isMonitor) {
        if(isMonitor) {
            customers.add(new MonitorCustomer(parlor,name,arrivalTime,eatingTime));
        } else {
            customers.add(new SemaphoreCustomer(parlor,name,arrivalTime,eatingTime));
        }
    }

    @Override
    public Collection<Customer> getCustomers() {
        return Collections.unmodifiableList(customers);
    }
}
