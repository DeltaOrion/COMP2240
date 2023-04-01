/**
 * File: MonitorCustomer.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 *
 * Performs the customer synchronization using a java monitor
 */
public class MonitorCustomer extends Customer {

    private final ConditionVariable started = new ConditionVariable();
    private final ConditionVariable arrived = new ConditionVariable();
    private final ConditionVariable seated = new ConditionVariable();
    private final ConditionVariable left = new ConditionVariable();

    public MonitorCustomer(IceCreamParlor parlor, String name, int arrivalTime, int eatingTime) {
        super(parlor, name, arrivalTime, eatingTime);
    }

    @Override
    public void start() {
        notify(started);
    }

    @Override
    protected void waitStart() throws InterruptedException {
        wait(started);
    }

    @Override
    public void arrive() {
        notify(arrived);
    }

    @Override
    protected void waitArrival() throws InterruptedException {
        wait(arrived);
    }

    @Override
    public void seat() {
        notify(seated);
    }

    @Override
    protected void waitSeat() throws InterruptedException {
        wait(seated);
    }

    @Override
    public void leave() {
        notify(left);
    }

    @Override
    protected void waitLeave() throws InterruptedException {
        wait(left);
    }

    /**
     * Stops the thread waiting on the condition variable.
     *
     * @param conditionVariable The condition variable the thread is waiting on.
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
        if(conditionVariable.shouldWait())
            wait();
    }

}
