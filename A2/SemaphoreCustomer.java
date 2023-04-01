import java.util.concurrent.Semaphore;

/**
 * File: SemaphoreCustomer.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 *
 * Completes the synchronization of a customer using a semaphore.
 *
 * This class works very simply
 * acquire() when waiting
 * release() when releasing
 *
 * This works very nicely because release just adds a permit back
 */
public class SemaphoreCustomer extends Customer {

    private final Semaphore sync = new Semaphore(0);

    public SemaphoreCustomer(IceCreamParlor parlor, String name, int arrivalTime, int eatingTime) {
        super(parlor, name, arrivalTime, eatingTime);
    }

    @Override
    public void start() {
        release();
    }

    @Override
    protected void waitStart() throws InterruptedException {
        await();
    }

    @Override
    public void arrive() {
        release();
    }

    @Override
    protected void waitArrival() throws InterruptedException {
        await();
    }

    @Override
    public void seat() {
        release();
    }

    @Override
    protected void waitSeat() throws InterruptedException {
        await();
    }

    @Override
    public void leave() {
        release();
    }

    @Override
    protected void waitLeave() throws InterruptedException {
        await();
    }

    private void release() {
        sync.release();
    }

    private void await() throws InterruptedException {
        sync.acquire();
    }

}
