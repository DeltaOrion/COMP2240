import java.util.ArrayList;
import java.util.List;

/**
 * File: TestSimulationConfig.java
 *
 * Author: Jacob Boyce
 * Student Number: c3264527
 * Course: COMP2240
 *
 * A simple class for making test inputs for the simulation quickly. I left this in for the report as it requires
 * discussion of testing.
 */
public class TestInputConfig implements InputConfig {

    private final List<Job> jobs;
    private final int timeQuanta;
    private final int frames;

    public TestInputConfig(int timeQuanta, int frames) {
        this.jobs = new ArrayList<>();
        this.timeQuanta = timeQuanta;
        this.frames = frames;
    }

    @Override
    public List<Job> getJobs() {
        return jobs;
    }

    @Override
    public int getFrames() {
        return frames;
    }

    @Override
    public int getTimeQuanta() {
        return timeQuanta;
    }

    public void addJob(Job job) {
        this.jobs.add(job);
    }
}
