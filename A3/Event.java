/**
 * File: Event.java
 *
 * Author: Jacob Boyce
 * Student Number: c3264527
 * Course: COMP2240
 *
 * Represents a event that can be run in the discrete event simulation. Event scheduled to an earlier time
 * should be run before tasks of a later time. This task could involve anything from a job arriving, the process
 * finishing or the dispatcher being interrupted or a new instruction being executed.
 *
 * Events should be run in FCFS order if the times are equal
 */
public abstract class Event implements Comparable<Event> {

    private final int time;
    private final Process process;

    //each process has a count higher than the previous process
    //this is to ensure FIFO order of the executing processes.
    private static int counter = 0;
    private final int count;

    /**
     * Creates a new event for the discrete event simulation
     *
     * @param time The time the event is run
     * @param process The process associated with the event
     */
    protected Event(int time, Process process) {
        this.time = time;
        this.process = process;
        this.count = counter++;
    }

    /**
     * Runs the event for the simulation
     */
    public abstract void run();

    /**
     * @return The time this event should run
     */
    public int getTime() {
        return this.time;
    }

    /**
     * @return The process associated with this event
     */
    public Process getProcess() {
        return process;
    }

    public int compareTo(Event o) {
        //events of earlier time should run first
        int compare = Integer.compare(this.time,o.time);
        if(compare==0) {
            //if two events arrive at the same time run them in FIFO order
            return Integer.compare(this.count,o.count);
        }

        return compare;
    }

}
