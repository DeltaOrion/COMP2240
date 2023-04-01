/**
 * File: FetchExecuteEvent.java
 *
 * Author: Jacob Boyce
 * Student Number: c3264527
 * Course: COMP2240
 *
 * Represents an event that runs a fetch execute on the process
 */
public class FetchExecuteEvent extends Event {

    private final Dispatcher dispatcher;

    protected FetchExecuteEvent(int time, Process process, Dispatcher dispatcher) {
        super(time, process);
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        //run the fetch execute for the dispatcher
        dispatcher.fetchExecute();
    }
}
