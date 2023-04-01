/**
 * Represents a condition variable which a monitor can wait on. As Java monitors
 * do most of the work with track and releasing the threads in a fair manner all this class
 * does is tracks whether a thread should wait on the condition.
 *
 * The main purpose of this class is to ensure that if notify() is called before wait() then
 * the thread does not blocking wait.
 *
 * This class also assumes only one thread can wait on a condition!
 */
public class ConditionVariable {

    //if value is true then the monitor should cause the thread to sleep, otherwise it should not.
    private boolean value;

    /**
     * Creates a new condition variable.
     */
    public ConditionVariable() {
        this.value = true;
    }

    /**
     * Resets the condition variable so that it can be wait on again.
     */
    public synchronized void reset() {
        this.value = true;
    }

    /**
     * Notifies the condition variable. This means that threads should not wait on this condition
     * anymore unless {@link #reset()} is called.
     */
    public synchronized void cNotify() {
        this.value = false;
    }

    /**
     * @return Whether the thread should wait on this condition or not.
     */
    public synchronized boolean shouldWait() {
        return value;
    }
}
