import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * File: FixedAllocationScheduler.java
 *
 * Author: Jacob Boyce
 * Student Number: c3264527
 * Course: COMP2240
 *
 * Represents a medium term scheduler that enacts the fixed allocation resident set policy using any replacement policy. In this scheme,
 * the frames allocated to a process do not change over the simulation period i.e. even after a process finishes, the allocated frames do not change.
 * And the page replacements (if necessary) will occur within the frames allocated to a process using GCLOCK policy.
 */
public class FixedAllocationScheduler extends MediumTermScheduler {

    private final Map<Process,ReplacementPolicy> residentSet;
    private final int frameCount;

    /**
     * @param simulation The simulation this scheduler should run on
     * @param frameCount  The total amount of memory frames in memory.
     */
    public FixedAllocationScheduler(OSSimulation simulation, int frameCount) {
        super(simulation);
        this.residentSet = new HashMap<>();
        this.frameCount = frameCount;
    }

    /**
     * Initializes the scheduler with the supplied processes. This will assign each process its own fixed memory
     * space. The global memory space is divided equally to all processes. Any additional frames remain unassigned. This means
     * that each process has its OWN replacement policy.
     *
     * @param processes The processes to assign a memory space to.
     */
    @Override
    public void initialize(Collection<Process> processes) {
        //calculate the amount of frames each process needs
        int framesPerProcess = frameCount/ processes.size();
        int count = 1;
        for(Process process : processes) {
            //give n frames to each process
            Frame[] frames = new Frame[framesPerProcess];
            for(int i=0;i<framesPerProcess;i++) {
                frames[i] = new Frame(count);
                count++;
            }
            //place each process in the table for easy lookup
            residentSet.put(process,new GLOCKAlgorithm(frames));
        }
    }

    @Override
    protected ReplacementPolicy getPolicy(Process process) {
        return residentSet.get(process);
    }

    @Override
    protected String getReplacementPolicyName() {
        for(ReplacementPolicy policy : residentSet.values())
            return policy.getName();

        return "";
    }

    @Override
    public String getResidentSetPolicyName() {
        return "Fixed-Local";
    }

}
