import java.util.List;

/**
 * File: InputConfig.java
 *
 * Author: Jacob Boyce
 * Student Number: c3264527
 * Course: COMP2240
 *
 * Represents the input specification for the simulation. The input should define the jobs to be used for all simulations, the amount of frames
 * to assign to the simulation and the time quanta of the round robin.
 */
public interface InputConfig {

    /**
     * The jobs to be used in the simulation. All simulations will run all of the specified jobs.
     *
     * @return A collection of all jobs to be run by all simulations.
     */
    List<Job> getJobs();

    /**
     * This defines the amount of memory the simulation will have. This is the global quantity of frames. In a fixed
     * allocation scheduler this will be divided up
     *
     * @return The amount of memory frames in total
     */
    int getFrames();

    /**
     * This defines the amount of time a process should spend in a round robin scheduler before being preempted.
     *
     * @return The time quantum size for the round robin scheduler.
     */
    int getTimeQuanta();
}
