import java.util.Collection;

/**
 * File: Job.java
 *
 * Author: Jacob Boyce
 * Student Number: c3264527
 * Course: COMP2240
 *
 * Represents the medium term scheduling algorithm. This manages when processes, or parts of them (virtual pages) are swapped in and out of memory.
 * The medium term scheduler defines two things
 *   - The resident set policy: The amount of frames that a process should have in memory at any given time, as well as the scope in which frames for a process can be replaced.
 *   - Replacement policy: An algorithm which defines which frame to replace next.
 *
 * If a request memory frame cannot be retrieved for a scheduler then it will throw a page fault which will send a request to an IO device to retrieve the frame.
 * A page fault will last 6 time stamps.
 */
public abstract class MediumTermScheduler {

    private static final int PAGE_FAULT_TIME = 6;
    private final OSSimulation simulation;

    /**
     * Creates a new MediumTermScheduler
     *
     * @param simulation The simulation the scheduler resides on
     */
    public MediumTermScheduler(OSSimulation simulation) {
        this.simulation = simulation;
    }

    /**
     * Initializes the scheduler with the given processes. This should assign memory frames to processes thereby creating and initializing
     * the resident set of each process. For example in a global allocation schedule this will assign a single memory space which all
     * processes can access.
     *
     * @param processes The processes the scheduler needs to use.
     */
    public abstract void initialize(Collection<Process> processes);

    /**
     * Retrieves a frame which a given page resides on. If the scheduler cannot find the frame
     * then this will throw a page fault and return null.
     *
     * @param process The process which the page belongs to
     * @param page The page number
     * @return The frame which the page is located on, or null if none.
     */
    public Frame getFrame(Process process, int page) {

        //Retrieve the replacement policy, which also stores the resident set of the process.
        ReplacementPolicy policy = getPolicy(process);
        Frame[] frames = policy.getResidentSet();
        Frame selected = null;

        //loop through all the frames until we find the frame
        for (Frame frame : frames) {
            if (frame.equals(process, page)) {
                //if we find the frame then reference it and return
                selected = frame;
                policy.reference(frame);
                break;
            }
        }

        //if we cannot find the frame then throw a page fault
        if (selected == null) {
            //System.out.println(process.getId() + " PAGE FAULTED AT " + page);
            //a page fault lasts 6 time stamps
            simulation.addEvent(new PageFaultEvent(process, simulation.getTime() + PAGE_FAULT_TIME, this, page));
            return null;
        }

        //return the frame we found, or null if none
        return selected;
    }


    /**
     * Causes the selected page to be placed in memory. This will replace an existing page for some
     * frame defined by the replacement policy. After the page has been replaced this will call
     * an interrupt for the dispatcher.
     *
     * @param process The process which the page belongs to
     * @param page The page index
     */
    public void replace(Process process, int page) {
        ReplacementPolicy policy = getPolicy(process);
        policy.replace(process, page);
        simulation.getDispatcher().interruptPageFault(process);
    }

    /**
     * Returns the Replacement Policy. Each replacement policy will also store the resident set of the process but is NOT
     * responsible for defining it. The resident set is defined in {@link #initialize(Collection)}
     *
     * @param process The process to get the
     * @return The replacement policy of the process
     */
    protected abstract ReplacementPolicy getPolicy(Process process);

    /**
     * The name is in the format
     * Replacement Policy Name - Resident Set Policy
     *
     * @return The name of this scheduler
     */
    public String getName() {
        return getReplacementPolicyName() + " - " + getResidentSetPolicyName() + " Replacement";
    }

    /**
     * The name of the replacement policy, for example GCLOCK
     *
     * @return the name of the replacement policy being used.
     */
    protected abstract String getReplacementPolicyName();

    /**
     * The name of the resident set policy being used, for example Fixed-Local Replacement.
     *
     * @return The name of the resident set policy.
     */
    public abstract String getResidentSetPolicyName();
}
