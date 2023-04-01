/**
 * File: ArriveTask.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * Represents a task that involves a process arriving to a dispatcher. The dispatcher should then handle the new process, this may involve preempting or simply
 * adding it to a queue, this will obviously depend on the implementation.
 */
public class ArriveTask extends Task {

    private final Dispatcher dispatcher;
    private final Process arrival;

    protected ArriveTask(Dispatcher dispatcher, Process process) {
        super(process.getProcessId(),process.getArrivalTime());
        this.dispatcher = dispatcher;
        this.arrival = process;
    }

    @Override
    public void run() {
        dispatcher.arrive(arrival);
    }

    @Override
    public boolean isArrival() {
        return true;
    }
}
