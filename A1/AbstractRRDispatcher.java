import java.util.ArrayDeque;
import java.util.Queue;

/**
 * File: AbstractRRDispatcher.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * Represents a dispatcher running a variant of a RR (Round Robin) Algorithm. A round robin algorithm works by running each process for either the specified time quanta, or until completion,
 * whichever is the shortest amount of time. Once the process has finished its quanta it is placed back into the back of the queue. The next process to be chosen is always picked from the
 * front of the queue.
 */
public abstract class AbstractRRDispatcher extends SingleCoreDispatcher {

    private final Queue<Process> processQueue;
    private final int baseTimeQuanta;

    protected AbstractRRDispatcher(String name, int contextSwitchTime, int baseTimeQuanta) {
        super(name, contextSwitchTime);
        this.baseTimeQuanta = baseTimeQuanta;
        this.processQueue = new ArrayDeque<>();
    }

    @Override
    protected void handleArrival(Process process) {
        //set the time quanta to the original starting quanta
        process.setTimeQuanta(baseTimeQuanta);
        //add to back of queue
        processQueue.add(process);
    }

    @Override
    protected Process selectProcess() {
        //always choose next process from front of queue
        return processQueue.poll();
    }

    @Override
    protected void readmitProcess(Process process) {
        //do any changes to the time quanta
        modifyQuanta(process);
        //add the process back to the queue
        processQueue.add(process);
    }

    protected abstract void modifyQuanta(Process process);

    @Override
    protected void scheduleTask(Process process) {
        process.setStartExecuting(getTime());
        //schedule the task either to completion, or for the remaining time, whichever is the shorter of the two.
        int finishTime = getTime() + process.getRemainingTime();
        int quantaTime = getTime() + process.getTimeQuanta();
        int nextTime = Math.min(finishTime,quantaTime);
        addTask(new InterruptTask(this,process.getProcessId(),nextTime));
    }
}
