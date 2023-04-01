import java.util.concurrent.Callable;

/**
 * File: MonitorIceCreamParlor.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 *
 * Performs the synchronization needed by the ice-cream parlor using Java's inbuilt monitors.
 */
public class MonitorIceCreamParlor extends IceCreamParlor {

    private final ConditionVariable waitingCustomers = new ConditionVariable();

    public MonitorIceCreamParlor() {
        super();
    }

    @Override
    protected synchronized void awaitCustomers() throws InterruptedException {
        //wait on the customer condition variable
        wait(waitingCustomers);
    }

    @Override
    protected synchronized void releaseCustomers() {
        //notify using the condition variable
        notify(waitingCustomers);
    }

    @Override
    protected void resetCustomers() {
        waitingCustomers.reset();
    }

    @Override
    protected void mutex(Runnable runnable) {
        //perform mutex using java's synchronized. synchronize using this monitor.
        synchronized (this) {
            runnable.run();
        }
    }

    @Override
    protected <T> T mutex(Callable<T> callable) {
        //perform mutex using java synchronized
        synchronized (this) {
            try {
                return callable.call();
            } catch (Exception e) {
                //propagate the exception
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Stops the thread waiting on the condition variable.
     *
     * @param conditionVariable The condition variable the thread is waiting.
     */
    private synchronized void notify(ConditionVariable conditionVariable) {
        conditionVariable.cNotify();
        notify();
    }

    /**
     * Causes the thread to sleep using a monitor condition variable. If notify was called
     * before wait then a blocking wait should not occur on this condition.
     *
     * @param conditionVariable The condition to wait on.
     * @throws InterruptedException If the thread is interrupted
     */
    private synchronized void wait(ConditionVariable conditionVariable) throws InterruptedException {
        //only wait if notify was not called.
        if(conditionVariable.shouldWait())
            wait();
    }
}
