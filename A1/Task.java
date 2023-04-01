/**
 * File: Task.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * Represents a task that can be run in the discrete event simulation. Tasks scheduled to an earlier time
 * should be run before tasks of a later time. This task could involve anything from a job arriving, the process
 * finishing or the dispatcher being interrupted
 */
public abstract class Task implements Comparable<Task> {

    private final int startTime; //when the task should be run
    private final int processId; //the id of the process this task represents

    protected Task(int processId, int startTime) {
        this.startTime = startTime;
        this.processId = processId;
    }

    /**
     * @return The time this task is run
     */
    public int getStartTime() {
        return startTime;
    }

    @Override
    public int compareTo(Task o) {
        //time takes precedent
        int compare = Integer.compare(this.getStartTime(),o.getStartTime());
        /* If a process P1 is interrupted at t1 and another process P2
         * arrives at the same time t1 then the newly arrived process P2 is added in the ready queue first
         * and the interrupted process P1 is added after that.
         */
        if(compare==0) {
            int arrivalCompare = Boolean.compare(o.isArrival(),this.isArrival());
            /* If two processes Px and Py have all other properties same (e.g. arrival time) then the tie
             * between them is broken using their ID i.e. Px will be chosen before Py if x<y.
             */
            if(arrivalCompare==0)
                return Integer.compare(this.getProcessId(), o.getProcessId());

            return arrivalCompare;
        }
        return compare;
    }

    /**
     * Runs the task in the discrete event simulation. This could be a task arriving to a dispatcher
     * or an interrupt occuring
     */
    public abstract void run();

    public int getProcessId() {
        return processId;
    }

    /**
     * Whether this task involves causing a task to arrive to the dispatcher
     *
     * @return whether this task causes an arrival to the dispatcher
     */
    public abstract boolean isArrival();
}
