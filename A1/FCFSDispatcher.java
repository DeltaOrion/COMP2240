import java.util.ArrayDeque;
import java.util.Queue;

/**
 * File: FCFSDispatcher.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * Represents a dispatcher running the FCFS (First comes first served) algorithm. This algorithm works by always selecting the process at the front of a queue. This means
 * the oldest process in the queue is run next.
 * It is non-preemptive and will run a process to completion. As it is non-preemptive processes can never be readmitted into the queue as they are always scheduled until completion.
 */
public class FCFSDispatcher extends SingleCoreDispatcher {

    private final Queue<Process> processQueue;

    /**
     * Creates a new FCFS dispatcher.
     *
     * @param contextSwitchTime The time taken switch processes
     */
    public FCFSDispatcher(int contextSwitchTime) {
        super("FCFS", contextSwitchTime);
        this.processQueue = new ArrayDeque<>();
    }

    @Override
    protected void handleArrival(Process process) {
        //always add a new process to the back of the queue
        processQueue.add(process);
    }

    @Override
    protected Process selectProcess() {
        //take the process from the front of the queue
        return processQueue.poll();
    }

    @Override
    protected void readmitProcess(Process process) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void scheduleTask(Process process) {
        //schedule the task until completion
        int finishTime = getTime() + process.getServiceTime();
        addTask(new InterruptTask(this,process.getProcessId(),finishTime));
    }
}
