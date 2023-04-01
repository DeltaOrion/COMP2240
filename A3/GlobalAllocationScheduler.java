import java.util.Collection;

/**
 * File: GlobalAllocationScheduler.java
 *
 * Author: Jacob Boyce
 * Student Number: c3264527
 * Course: COMP2240
 *
 * Represents a medium term scheduler that enacts the global variable allocation resident set policy using any replacement policy. In this scheme,
 * no specific frame is allocated to any process rather all frames are available to the processes for use. A process can use an unused frame in the
 * user memory space to bring in its own page. For page replacement it will use GCLOCK policy but will consider all the pages in the user memory space.
 * I.e. the page selected for replacement may belong to any process running in the system. When a process finish execution the frames to that finished process
 * are not returned immediately rather will be replaced by pages from other processes using GCLOCK replacement policy.
 */
public class GlobalAllocationScheduler extends MediumTermScheduler {

    private ReplacementPolicy policy;
    private final int frameCount;

    public GlobalAllocationScheduler(OSSimulation simulation, int frameCount) {
        super(simulation);
        policy = null;
        this.frameCount = frameCount;
    }

    /**
     * Initializes all of the frames for the given process. In this policy no frame is allocated to a process. All frames
     * are available to all processes meaning there should be ONE global replacement policy.
     *
     * @param processes The processes to assign a memory space to.
     */
    @Override
    public void initialize(Collection<Process> processes) {
        Frame[] frames = new Frame[frameCount];
        for(int i=0;i<frameCount;i++) {
            frames[i] = new Frame(i+1);
        }
        policy = new GLOCKAlgorithm(frames);
    }

    @Override
    protected ReplacementPolicy getPolicy(Process process) {
        return policy;
    }

    @Override
    protected String getReplacementPolicyName() {
        return policy.getName();
    }

    @Override
    public String getResidentSetPolicyName() {
        return "Variable-Global";
    }
}
