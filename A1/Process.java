/**
 * File: Process.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * Represents a task that has is currently being executed or has fully been executed by a processor. This is distinct from a job as a job represents a task that the processor
 * can run while this represents one that is actively being run
 */
public class Process {

    private final Job job;
    private int finishTime;
    private int timeExecuted;
    private int lastStarted;
    private int timeQuanta;
    private int priority;
    private boolean isFinished;

    /**
     * Creates a new process
     *
     * @param job The job this process represents
     */
    public Process(Job job) {
        this.job = job;
        this.finishTime = 0;
        this.timeExecuted = 0;
        this.lastStarted = 0;
        this.timeQuanta = 0;
        this.isFinished = false;
        this.priority = 0;
    }

    /**
     * @return The unique numerical id of the process
     */
    public int getProcessId() {
        return job.getId();
    }

    /**
     * A process id is displayed as p{numeric id}, for example p12, would be a process with the id
     * 12
     *
     * @return the id of the process to be displayed
     */
    public String getDisplayID() {
        return "p"+getProcessId();
    }

    /**
     * @return The amount of time required to fully execute the process
     */
    public int getServiceTime() {
        return job.getServiceTime();
    }

    /**
     * The time remaining is the (service time) - (time executed).
     *
     * @return The total amount of time remaining for the process to be fully executed
     */
    public int getRemainingTime() {
        return getServiceTime() - timeExecuted;
    }

    /**
     * @return The time that the job arrived to the processor
     */
    public int getArrivalTime() {
        return job.getArrivalTime();
    }

    /**
     * Marks that the process has started executing. To finish the execution period, whether by interrupt or by the task
     * being completed should be done using {@link #setFinishExecuting(int)}. This should only be used if the process
     * has not finished.
     *
     * @param time the time execution begin
     */
    public void setStartExecuting(int time) {
        this.lastStarted = time;
    }

    /**
     * Marks that the process is no longer executing. If the process is completed then the process will now be marked as finished.
     *
     * @param time The time execution finished.
     */
    public void setFinishExecuting(int time) {
        int netTime = time - lastStarted;
        timeExecuted += netTime;
        if(timeExecuted>=getServiceTime()) {
            finishTime = time;
            this.isFinished = true;
        }
    }

    /**
     * Checks whether the process will finish at the given time. This assumes that the process
     * is currently executing.
     *
     * @param time The time to check if the process finishes
     * @return Whether the process will finish at the given time or not.
     */
    public boolean willFinish(int time) {
        return time - lastStarted + timeExecuted >= getServiceTime();
    }

    /**
     * @return Whether the process has finished executing.
     */
    public boolean isFinished() {
        return isFinished;
    }

    /**
     * The turnaround time is the total amount of time the process has spent in the processor, beginning from when it first arrived {@link #getArrivalTime()}
     * ending at when the process finished, including wait time.
     *
     * This is represented by (finish time) - (arrival time)
     *
     * @return The process turnaround time
     */
    public int getTurnaroundTime() {
        return finishTime - getArrivalTime();
    }

    /**
     * The wait time is the amount of time the process spent waiting in the processor.
     *
     * @return The wait time of this process
     */
    public int getWaitTime() {
        return finishTime - getArrivalTime() - getServiceTime();
    }

    /**
     * The time quanta is the amount of time a process should be left to run before being preempted. This value is useful for RR or other preemptive approaches.
     *
     * @return The time quanta of this process
     */
    public int getTimeQuanta() {
        return timeQuanta;
    }

    /**
     * The time quanta is the amount of time a process should be left to run before being preempted. This value is useful for RR or other preemptive approaches. This method
     * sets the time quanta of this process.
     *
     * @param timeQuanta the new time quanta of the process
     */
    public void setTimeQuanta(int timeQuanta) {
        this.timeQuanta = timeQuanta;
    }

    /**
     * Priority represents the importance of the process. In general a higher priority process is run more often than a lower priority process
     *
     * @return The priority of the process.
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Priority represents the importance of the process. In general a higher priority process is run more often than a lower priority process.
     *
     * @param priority The new priority of the process
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }
}
