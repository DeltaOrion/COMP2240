import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

/**
 * File: SemaphoreIceCreamParlor.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 *
 * Performs the synchronization needed by the ice-cream parlor using semaphores.
 */
public class SemaphoreIceCreamParlor extends IceCreamParlor {

    private final Semaphore mutex = new Semaphore(1); //one for mutual exclusion
    private final Semaphore sync = new Semaphore(0); //one semaphore for synchronization

    public SemaphoreIceCreamParlor() {
        super();
    }

    @Override
    protected void awaitCustomers() throws InterruptedException {
        //take a permit, it starts with 0 so unless release was occurred the
        //thread will now be blocked
        sync.acquire();
    }

    @Override
    protected void releaseCustomers() {
        //puts a permit back.
        sync.release();
    }

    @Override
    protected void resetCustomers() {
        //acquire automatically removes a permit so this isn't necessary for semaphore implementation.
    }

    @Override
    protected void mutex(Runnable runnable) {
        //enter critical section
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        //run the code
        try {
            runnable.run();
        } finally {
            //leave mutex
            mutex.release();
        }
    }

    @Override
    protected <T> T mutex(Callable<T> callable) {
        //enter critical section
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }

        try {
            //run code
            return callable.call();
        } catch (Exception e) {
            //propagate the exception
            throw new RuntimeException(e);
        } finally {
            //leave critical section
            mutex.release();
        }
    }
}
