/**
 * File: Job.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * Represents a task that CAN BE scheduled by a processor. Each job has a unique id, a time at which it arrives and a time of execution. A process is distinct from the job
 * as a process is a job that is being run by a processor
 */
public class Job {

    private final int id;
    private final int arrivalTime;
    private final int serviceTime;

    /**
     * Creates a new job
     *
     * @param id the unique id of the job. No other job should have this number
     * @param arrivalTime The time at which the job arrives to the processor
     * @param processingTime The amount of time that is required to fully execute the job
     */
    public Job(int id, int arrivalTime, int processingTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = processingTime;
    }

    /**
     * @return The unique id number of the job
     */
    public int getId() {
        return id;
    }

    /**
     * @return The time at which the job arrives to the processor
     */
    public int getArrivalTime() {
        return arrivalTime;
    }

    /**
     * @return The amount of time that is required to fully execute the job
     */
    public int getServiceTime() {
        return serviceTime;
    }
}
