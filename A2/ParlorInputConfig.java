import java.util.Collection;

/**
 * File: ParlorInputConfig.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * Represents the input specification for the parlor (P2). The input should define the Customers to be used on the parlor. All of the
 * Customers defined in the input configuration will be added to the simulation. The returned customer can either be a monitor customer
 * or a semaphore customer but this must be defined when entering the customers into the config.
 */
public interface ParlorInputConfig {

    /**
     * @return A collection of all the customers to be used in the simulation
     */
    Collection<Customer> getCustomers();
}
