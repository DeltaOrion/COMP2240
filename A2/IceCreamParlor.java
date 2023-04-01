import java.util.*;
import java.util.concurrent.Callable;

/**
 * File: IceCreamParlor.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 *
 * The Ice-Cream Parlor is a shared object that all customers can use. The parlor has a limited amount of seats. If all
 * of the seats are occupied then the parlor becomes full. When customer arrive at the parlor they are either granted a seat
 * or if the parlor is full must wait until all seats are vacated.
 *
 * Once a customer is seated they will eat their ice-cream and leave.
 *
 * This class runs this simulation as follows
 *
 * 1. Start all customers
 * 2. Run all events for the current timestamp, for example, if 2 customers arrived and 1 finishes eating their ice-cream at timestamp 3.
 * 3. Wait for all customers involved in an event for this timestamp
 * 4. Increment the timestamp to the next event
 * 5. continue back to step 2
 *
 * {@link Event} are tracked using a priority queue. The priority queue is sorted by time incrementally
 *
 * Customers can arrive using {@link #arrive(Customer) and leave using {@link #leave()}
 *
 * A parlor is either
 *   - full - the parlor has filled up and is waiting for customers to vacate
 *   - not full - the parlor has not filled up yet
 *
 * When customers arrived when the parlor is full they are forced to wait in a FIFO basis.
 *
 * NOTE ON RUNNING MULTIPLE EVENTS ON A TIMESTAMP
 * It would definitely be far simpler to run a single event, then wait on that singular customer, however after an email with Nasimul
 * he said that we need to run all events of the current timestamp concurrently before proceeding to the next which is why it is
 * done that way.
 */
public abstract class IceCreamParlor {

    private List<Customer> customers;
    private int time;
    private final PriorityQueue<Event> events;

    private int seatsOccupied;
    private boolean full;
    private final Queue<Customer> waiting;

    private final Set<Customer> awaiting;
    private int awaitingCount;
    private final static int MAX_SEATS = 5;

    /**
     * Creates a new ice-cream parlor. The ice-cream parlor will begin at time=0 with no events and customers.
     */
    public IceCreamParlor() {
        this.time = 0;
        this.events = new PriorityQueue<>();
        this.customers = new ArrayList<>();

        this.waiting = new ArrayDeque<>();
        this.awaiting = new HashSet<>();
        this.full = false;
        this.seatsOccupied = 0;
    }

    /**
     * Starts and runs the simulation. No more customers should be added after this. The simulation works as follows
     *
     * 1. Start all customers
     * 2. Run all events for the current timestamp, for example, if 2 customers arrived and 1 finishes eating their ice-cream at timestamp 3.
     * 3. Wait for all customers involved in an event for this timestamp
     * 4. Increment the timestamp to the next event
     * 5. Cleanup
     * 6. Continue back to step 2 until there are no more events.
     *
     * Events are tracked using a priority queue which orders by event timestamp.
     */
    void start() {
        customers = Collections.unmodifiableList(customers);
        //start all customers
        for (Customer customer : customers) {
            addEvent(new StartEvent(customer, 0));
        }

        while (!events.isEmpty()) {
            //enter critical section

            // -- Increment time to the next event --//
            time = events.peek().getTime();

            //-- Run all events for timestamp --//
            //set the timestamp to the most recent event
            //this is safe to do outside of the mutex because no events are running (all customers signalled they are doing nothing)
            //so no other thread will manipulate the customer queue.
            mutex(() -> {
                while (!events.isEmpty() && events.peek().getTime() == time) {
                    //loop through all events of the new timestamp
                    //run all events concurrently
                    Event event = events.poll();
                    //add to a set of customers we are waiting on (in case we get two events from the same customer)
                    awaiting.add(event.getCustomer());
                    event.run();
                }

                awaitingCount = awaiting.size();
            });

            //set the amount of customers we are waiting for on this timestamp.
            //-- Wait for customers on Timestamp--//
            //We run multiple events on a timestamp, events are on a per customer basis, thus we must
            //wait on all customers involved in an event this timestamp before proceeding to the next
            //timestamp.
            try {
                awaitCustomers();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            //-- Cleanup for next timestamp --//
            awaiting.clear();
            awaitingCount = 0;
            resetCustomers();
        }

    }

    /**
     * Causes a customer to leave the ice-cream parlor. If a customer leaves and the parlor
     * is full, and there is no customers left then the parlor is made no longer full and
     * several customers are allowed to enter the parlor again.
     */
    void leave() {
        mutex(() -> {
            //note that one less seat is occupied
            seatsOccupied--;
            if(full) {
                //check if the parlor filled up and all of the seats have been vacated
                if(seatsOccupied==0) {
                    full = false;
                    //if all the seats have been vacated then stop the parlor from being full, add up
                    //to the seat capacity of new customers in.
                    while (!waiting.isEmpty() && seatsOccupied<MAX_SEATS) {
                        Customer customer  = waiting.poll();
                        customer.seat();
                        awaitingCount++;
                        seatsOccupied++;
                    }

                    //if we grabbed 5 customers for the waiting list then the parlor is full again
                    if(seatsOccupied==MAX_SEATS)
                        full = true;
                }
            }
        });
    }

    /**
     * Causes a customer to arrive at the parlor. If the parlor is full they are forced to wait
     * in a FIFO queue until all customers are vacated.
     *
     * @param customer The customer requesting to use the parlor
     * @return true if the customer was given a seat immediately, false if otherwise
     */
    boolean arrive(final Customer customer) {
        return mutex(() -> {
            if(full) {
                //if the parlor is full then add them to the waiting list
                //until all the seats are empty again
                waiting.add(customer);
                return false;
            } else {
                //otherwise indicate a seat is occupied
                seatsOccupied++;
                if(seatsOccupied==MAX_SEATS) {
                    //if the parlor fills up mark this for later
                    full = true;
                }
                return true;
            }
        });
    }

    /**
     * Allows a customer to signal to the parlor that it is completed its event for this timestamp. Once
     * all customers are done with their events on this timestamp then the program proceeds to the next
     * timestamp.
     */
    public void signal() {
        mutex(() -> {
            //no thread should signal if we aren't awaiting for anyone, if this happens
            //some kind of thread safety error has occurred
            if(awaitingCount==0)
                throw new IllegalStateException();

            //indicate that a customer has now signalled
            awaitingCount--;
            //if all customers have signalled then release
            if(awaitingCount==0)
                releaseCustomers();
        });
    }

    /**
     * @return The current timestamp of the simulation
     */
    int getTime() {
        return mutex(() -> {
            return time;
        });
    }

    /**
     * @param event Adds a new event to the simulation. Events are ordered by their timestamp.
     */
    void addEvent(Event event) {
        mutex(() -> {
            events.add(event);
        });
    }

    /**
     * Customers must be added before the simulation starts.
     *
     * @param customer The customer to add to the simulation.
     */
    void addCustomer(Customer customer) {
        customers.add(customer);
    }

    /**
     * @return A list of all customers in the simulation
     */
    Collection<Customer> getCustomers() {
        return Collections.unmodifiableList(customers);
    }

    /**
     * Causes the simulation to wait for all customers. This should be a blocking
     * wait. if {@link #releaseCustomers()} was called BEFORE this method then
     * no blocking wait should occur.
     *
     * @throws InterruptedException If the thread was interrupted
     */
    protected abstract void awaitCustomers() throws InterruptedException;

    /**
     * Causes the simulation which is currently waiting for all customers to
     * stop waiting for the customers.
     */
    protected abstract void releaseCustomers();

    /**
     * Resets the customer wait to ensure that next time {@link #awaitCustomers()} is called
     * it will blocking wait. This needs to be called before proceeding to the next timestamp.
     */
    protected abstract void resetCustomers();

    /**
     * Performs mutual exclusion for a section of code. This should ensure
     * thread safe access to a variable.
     *
     * @param runnable The code to perform mutual exclusion on.
     */
    protected abstract void mutex(Runnable runnable);

    /**
     * Performs mutual exclusion for a section of code that needs to return
     * some value. This should be used primarily for getters.
     *
     * @param callable The code to perform mutual exclusion on
     * @param <T> The return type
     * @return Some value from the code.
     */
    protected abstract <T> T mutex(Callable<T> callable);
}
