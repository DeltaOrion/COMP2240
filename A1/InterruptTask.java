/**
 * File: InterruptTask.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * Represents a task which involves interrupting the dispatcher. This could be run because the process has finished, the time quanta has expired
 * or for any other reason why the dispatcher would need to stop running the current process
 */
public class InterruptTask extends Task {

    private final Dispatcher dispatcher;

    protected InterruptTask(Dispatcher dispatcher, int processId, int time) {
        super(processId,time);
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        //interrupt the dispatcher at the required time
        dispatcher.interrupt();
    }

    @Override
    public boolean isArrival() {
        return false;
    }
}
