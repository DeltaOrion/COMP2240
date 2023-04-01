/**
 * File: SingleCoreDispatcher.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * Represents a {@link Dispatcher} for an algorithm that runs on a single processor.
 */
public abstract class SingleCoreDispatcher extends Dispatcher {

    private Process runningProcess;

    /**
     * Creates a new single core dispatcher
     *
     * @param name The name of the algorithm being used
     * @param contextSwitchTime The time to switch processes
     */
    protected SingleCoreDispatcher(String name, int contextSwitchTime) {
        super(name, contextSwitchTime);
        runningProcess = null;
    }

    @Override
    public void arrive(Process process) {
        //have the algorithm do something with the arrival, this could mean preempting it
        handleArrival(process);
        if(runningProcess==null) //if there is no process then start running it
            interrupt();
    }

    /**
     * Logic for what the algorithm should do when a new process arrives to the dispatcher
     *
     * @param process The process which has arrived.
     */
    protected abstract void handleArrival(Process process);

    @Override
    public void interrupt() {
        //algorithm should select the process
        Process process = selectProcess();
        //handle the existing process
        if(this.runningProcess!=null) {
            //perform statistics
            this.runningProcess.setFinishExecuting(getTime());
            //handle if the process is not finished
            if(!this.runningProcess.isFinished()) {
                //if the implementation has picked the same process then don't readmit it
                if(process==null) {
                    /**
                     * If there is only one process running in the processor and no other process is waiting in the ready queue
                     * then there is no need to switch the process and the dispatcher will NOT run.
                     *
                     * This is the only logical way to satisfy this constraint, the only way to know if there is no other processes waiting
                     * is if the dispatcher does not pick anything, another process could be added after the time quanta expires so we must
                     * continue to reschedule.
                     */
                    scheduleTask(this.runningProcess);
                    return;
                }

                //If the algorithm simply selected the same process then don't readmit it. Otherwise get the algorithm
                //to return it back to the queue.
                if(!process.equals(this.runningProcess))
                    readmitProcess(this.runningProcess);

            }

        }

        //Now perform a context switch
        this.runningProcess = process;
        contextSwitch();
        if(process==null) //if the CPU is now idle we don't need to do any scheduling
            return;

        System.out.println("T"+getTime()+": "+process.getDisplayID());
        //Schedule the next event for the discrete event simulation
        scheduleTask(process);
    }

    /**
     * Logic for the dispatcher when picking a new process after an interrupt. If there are no processes then return null. For example,
     * FCFS will always pick the next process in the queue.
     *
     * @return The selected process or null if there is no new process to select
     */
    protected abstract Process selectProcess();

    /**
     * Logic for readmitting a process back into its queue after an interrupt. This will only
     * be called if the process has not finished.
     *
     * @param process The process to readmit back into the queue.
     * @throws UnsupportedOperationException if the algorithm is non-preemptive
     */
    protected abstract void readmitProcess(Process process);

    /**
     * Schedules any tasks involving the process. How it is scheduled will depend on the algorithm, for example
     * FCFS will always schedule an interrupt after the task has completed, RR will schedule for the specified time
     * quanta.
     *
     * @param process The process to schedule tasks with
     */
    protected abstract void scheduleTask(Process process);

    /**
     * @return The process currently being executed by the dispatcher.
     */
    protected Process getRunningProcess() {
        return runningProcess;
    }
}
