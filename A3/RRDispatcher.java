import java.util.ArrayDeque;
import java.util.Queue;

/**
 * File: RRDispatcher.java
 *
 * Author: Jacob Boyce
 * Student Number: c3264527
 * Course: COMP2240
 *
 * Represents a dispatcher running the standard RR (Round Robin) Algorithm. A round robin algorithm works by running each process for either the specified time quanta, or until completion,
 * whichever is the shortest amount of time. Once the process has finished its quanta it is placed back into the back of the queue. The next process to be chosen is always picked from the
 * front of the queue.
 */
public class RRDispatcher extends Dispatcher {

    private final int timeQuanta;
    private int timeSpent;
    private final Queue<Process> readyQueue;
    private final Queue<Process> blockedQueue;

    /**
     * Creates a new Round Robin Dispatcher
     *
     * @param simulation The simulation which this dispatcher sits on
     * @param timeQuanta The time quanta Q to use.
     */
    public RRDispatcher(OSSimulation simulation, int timeQuanta) {
        super(simulation);
        this.timeQuanta = timeQuanta;
        timeSpent = 0;
        this.readyQueue = new ArrayDeque<>();
        this.blockedQueue = new ArrayDeque<>();
    }

    /**
     * This algorithm will always add the process to the start of the ready queue. It will not preempt the existing
     * process.
     *
     * @param process The process which has arrived.
     */
    @Override
    protected void newProcess(Process process) {
        readyQueue.add(process);
    }

    /**
     * When RR unblocks the process is simply placed back into the ready queue, no special actions should occur.
     *
     * @param process The process to unblock
     */
    @Override
    protected void unblock(Process process) {
        blockedQueue.remove(process);
        readyQueue.add(process);
    }

    /**
     * When RR blocks a process it will place it in a blocked queue. This will ensure
     * that processes are unblocked in the same order they were blocked
     *
     * @param process The process to block
     */
    @Override
    protected void block(Process process) {
        blockedQueue.add(process);
    }

    /**
     * This algorithm will preempt the existing process if the time spent is greater than the duration of the time quanta.
     *
     * @param process The process that has just arrived or has completed an instruction
     * @return The new process to use.
     */
    @Override
    protected Process readmitProcess(Process process) {
        timeSpent++;
        if(timeSpent>timeQuanta) {
            //if the time spent is greater than the quanta then it has expired,
            //preempt this process and add it to the end of the ready queue
            //System.out.println("Time Quanta Expired");
            readyQueue.add(process);
            return null;
        }

        //otherwise return the existing process
        return process;
    }

    /**
     * Logic for the dispatcher when picking a new process after an interrupt. If there are no processes then return null. This dispatcher
     * will simply pick the next process in the ready queue.
     *
     * @return The selected process or null if there is no new process to select
     */
    @Override
    protected Process selectProcess() {
        //a process is being selected, reset the time quanta.
        timeSpent = 0;
        if(readyQueue.isEmpty())
            return null;

        return readyQueue.poll();
    }
}
