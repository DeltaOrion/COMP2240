/**
 * File: Customer.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 *
 * A customer follows this sequence of events
 *  1. Arrive at the parlor
 *  2. If the parlor is full wait in queue, otherwise take a seat
 *  3. Eat ice-cream at seat
 *  4. Leave the parlor
 *
 * This means each customer can do one of three events
 *  1. Start (signal it will arrive at x time at the parlor)
 *  2. Arrive
 *  3. Leave
 *
 *  When a customer waits in queue in the parlor this is simply managed by the parlor which is why it
 *  does not have a corresponding {@link Event}
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 */
public abstract class Customer implements Runnable {

    private final IceCreamParlor parlor;
    private final String name;
    private final int arrivalTime;
    private final int eatingTime;

    private int seatTime = -1;
    private int leaveTime = -1;

    public Customer(IceCreamParlor parlor, String name, int arrivalTime, int eatingTime) {
        this.parlor = parlor;
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.eatingTime = eatingTime;
    }

    @Override
    public void run() {
        try {
            //--- Starting ---//
            //wait for the simulation to start
            waitStart();

            //-- Start (signal when it will arrive at the parlor --//
            parlor.addEvent(new ArriveEvent(this,parlor.getTime() + arrivalTime));
            parlor.signal();
            waitArrival();

            //--- Arrival to parlor ---//
            //if the customer arrives at the parlor it either waits for a seat
            //or is granted one immediately in which case it begins eating ice-cream
            boolean arrived = parlor.arrive(this);
            if(!arrived) {
                //--- Waiting for a seat ---//
                //signal to the parlor we are done for the timestamp and
                //wait for our turn to take a seat
                parlor.signal();
                waitSeat();
            }

            //--- Ice-cream eating ---//
            //schedule for when it is time to leave the parlor, note when we grabbed a seat, signal to the parlor
            //we are done for the timestamp and wait for us to finish our ice-cream
            parlor.addEvent(new LeaveEvent(this,parlor.getTime()+eatingTime));
            seatTime = parlor.getTime();
            parlor.signal();
            waitLeave();

            //--- Leaving Parlor ---//
            //note the time we left, signal to the parlor that we are done with this timestamp and leave
            leaveTime = parlor.getTime();
            parlor.leave();
            parlor.signal();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Customers are named C-(number). For example C13
     *
     * @return The name of the customer.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The time at which the customer arrives at the parlor
     */
    public int getArrivalTime() {
        return arrivalTime;
    }

    /**
     * @return The amount of time to eat the ice-cream
     */
    public int getEatingTime() {
        return eatingTime;
    }

    /**
     * Starts the customer. This will cause the customer to signal when it needs to arrive. If the customer
     * is blocking wait for the start. then it should stop the wait.
     */
    public abstract void start();

    /**
     * Causes the customer to wait for the simulation to start. If {@link #start()} is called first
     * this should not blocking wait.
     *
     * @throws InterruptedException If the thread was interrupted
     */
    protected abstract void waitStart() throws InterruptedException;

    /**
     * Causes the customer to arrive. This will cause the customer to either take a seat, or wait in queue. If the customer
     * is blocking wait for an arrival. then it should stop the wait.
     */
    public abstract void arrive();

    /**
     * Causes the customer to wait until its arrival time. If {@link #arrive()} is called first
     * this should not blocking wait.
     *
     * @throws InterruptedException If the thread was interrupted
     */
    protected abstract void waitArrival() throws InterruptedException;

    /**
     * Causes the customer to seat. This should only be called if the customer was waiting in queue. If the customer
     * is blocking wait for an seat. then it should stop the wait.
     */
    public abstract void seat();

    /**
     * Causes the customer to wait until its seating time. If {@link #seat()} is called first
     * this should not blocking wait.
     *
     * @throws InterruptedException If the thread was interrupted
     */
    protected abstract void waitSeat() throws InterruptedException;

    /**
     * Causes the customer to leave the parlor. If the customer is blocking wait to leave it should stop the wait.
     */
    public abstract void leave();

    /**
     * Causes the customer to wait until its leave time. If {@link #leave()} is called first
     * this should not blocking wait.
     *
     * @throws InterruptedException If the thread was interrupted
     */
    protected abstract void waitLeave() throws InterruptedException;

    /**
     * @return The time the customer took a seat in the simulation
     */
    public int getSeatTime() {
        return seatTime;
    }

    /**
     * @return The time the customer left the parlor in the simulation
     */
    public int getLeaveTime() {
        return leaveTime;
    }
}
