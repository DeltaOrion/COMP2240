import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * File: TestSimulationConfig.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * A simple class for making test inputs for the simulation quickly. I left this in for the report as it requires
 * discussion of testing.
 */
public class TestSimulationConfig implements SimulationConfig {

    private final int contextSwitchTime;
    private final List<Job> jobs = new ArrayList<>();

    public TestSimulationConfig(int contextSwitchTime) {
        this.contextSwitchTime = contextSwitchTime;
    }

    public void addJob(Job job) {
        jobs.add(job);
    }

    @Override
    public int getContextSwitchTime() {
        return contextSwitchTime;
    }

    @Override
    public Collection<Job> getJobs() {
        return jobs;
    }
}
