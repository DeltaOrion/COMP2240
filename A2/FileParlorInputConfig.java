import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * File: FileBridgeInputConfig.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * Represents an input configuration for the bridge problem using a file input. The file should be of the following
 * format
 *
 * A1 C1 E1
 * A2 C2 E2
 * A3 C3 E3
 * ...
 * END
 *
 * where
 *   - Ai is the arrival time of the customer
 *   - Ci is the customer name
 *   - E3 is the eating time of the customer
 */
public class FileParlorInputConfig implements ParlorInputConfig {

    private final List<Customer> customers;

    /**
     * Reads all of the customers from the input file and loads them into an array for future use
     *
     * @param parlor the parlor which all customers should use
     * @param file the file to read the customers from
     * @throws FileNotFoundException if the file trying to be read does not exist
     */
    public FileParlorInputConfig(IceCreamParlor parlor, boolean monitor, File file) throws FileNotFoundException {
        this.customers = new ArrayList<>();
        readFile(parlor,monitor,file);
    }

    private void readFile(IceCreamParlor parlor, boolean monitor, File file) throws FileNotFoundException {
        try(Scanner scanner = new Scanner(file)) {
            //each line represents a customer
            while (scanner.hasNextLine()) {
                String token = scanner.nextLine();
                if(token.equalsIgnoreCase("END"))
                    return;

                //split the line into its 3 values
                //Arrival Customer Eating
                String[] split = token.split(" ");
                //grab arrival time
                int arrive = Integer.parseInt(split[0]);
                String name = split[1];
                //grab eating time
                int eatingTime = Integer.parseInt(split[2]);
                customers.add(getCustomer(parlor,arrive,name,eatingTime,monitor));
            }
        }
    }

    /**
     * Factory method for creating a customer using the file input values. If using
     * monitors this will return a {@link MonitorCustomer}
     *
     * @param parlor The parlor to use
     * @param arrive The time the customer arrives
     * @param name The name of the customer
     * @param eatingTime The eating time of the customer
     * @param isMonitor Whether the customer uses a monitor
     * @return A new customer
     */
    private Customer getCustomer(IceCreamParlor parlor ,int arrive, String name, int eatingTime, boolean isMonitor) {
        if(isMonitor) {
            return new MonitorCustomer(parlor,name,arrive,eatingTime);
        } else {
            return new SemaphoreCustomer(parlor,name,arrive,eatingTime);
        }
    }

    /**
     *
     * @return The customers read from the file
     */
    @Override
    public Collection<Customer> getCustomers() {
        return Collections.unmodifiableList(customers);
    }
}
