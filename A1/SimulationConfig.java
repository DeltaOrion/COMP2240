import java.util.Collection;

/**
 * File: SimulationConfig
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * Represents the input specification for the simulation. The input should define the jobs to be used for all dispatchers and the amount of time
 * taken for all dispatchers to switch jobs
 */
public interface SimulationConfig {

    /**
     * The context switch time is the time taken by the dispatcher to switch processes. All dispatchers
     * will have the same switch time.
     *
     * @return The context switch time for all dispatchers.
     */
    int getContextSwitchTime();

    /**
     * The jobs to be used in the simulation. All dispatchers will run all of the specified jobs.
     *
     * @return A collection of all jobs to be run by all dispatchers.
     */
    Collection<Job> getJobs();
}
