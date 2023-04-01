/**
 * File: Dispatcher.java
 *
 * Author: Jacob Boyce
 * Student Number: c3264527
 * Course: COMP2240
 *
 * Represents a short term scheduling algorithm. A dispatcher, also known as the short term scheduler decides which process to pick
 * from a pool of available processes to be run on the processor. Each process has a list of instructions and each instruction represents the page that instruction
 * executes on. Each instruction takes one timestamp to execute. If the instruction cannot be executed due to not being in memory a page fault should be thrown and another
 * process be executed.
 *
 * Each dispatcher has a list of jobs that it needs to run over its lifetime. Jobs are added to the dispatcher runtime on their scheduled arrival times. The processes are then 'executed' and
 * once they have been executed then stats will be recorded on them.
 */
public abstract class Dispatcher {

    private Process running;
    private final OSSimulation simulation;

    /**
     * Creates a new dispatcher
     *
     * @param simulation The simulation the dispatcher is in
     */
    public Dispatcher(OSSimulation simulation) {
        this.simulation = simulation;
    }

    /**
     * Called when a process arrives to the dispatcher. The algorithm will then decide what to do with the new arrival, this could involve
     * preempting the existing process or simply adding it to a queue.
     *
     * @param process The process to add to the dispatcher.
     */
    public void arrive(Process process) {
        //if no process is running then simply run it
        if(running==null) {
            this.running = process;
            simulation.addEvent(new FetchExecuteEvent(simulation.getTime(),process, this));
        } else {
            //otherwise get the algorithm to admit it whether it be through preemption or otherwise.
            newProcess(process);
        }
    }

    /**
     * Logic for what the algorithm should do when a new process arrives to the dispatcher
     *
     * @param process The process which has arrived.
     */
    protected abstract void newProcess(Process process);

    /**
     * Occurs when the IO device needs to interrupt the processor once a memory transfer has been completed. This will always unblock
     * the process. If no process is executing (the processor is IDLE) then it can be executed immediately.
     *
     * @param process The process which the IO transfer has been complete for.
     */
    public void interruptPageFault(Process process) {
        //System.out.println("IO INTERRUPT for "+process.getId());
        //unblock the process
        unblock(process);
        if(running==null)
            fetchExecute();
    }

    /**
     * Logic for what the algorithm should do when a process is unblocked. This could involve simply moving it from the blocked queue to the ready queue
     * or could be more sophisticated moving it to an auxiliary queue (VRR)
     *
     * @param process The process to unblock
     */
    protected abstract void unblock(Process process);

    /**
     * Logic for what the algorithm should do when a process is blocked. This could be as simple as moving it to a blocked queue or could be more sophisticated
     * rising its priority or whatnot.
     *
     * @param process The process to block
     */
    protected abstract void block(Process process);

    /**
     * Logic for handling an existing process after it has completed an instruction or has been made the running process. In a preemptive algorithm such
     * as RR this may involve replacing the process.
     *
     * @param process The process that has just arrived or has completed an instruction
     * @return The new process to use. If the process should not be swapped out then return the input parameter.
     */
    protected abstract Process readmitProcess(Process process);

    /**
     * Logic for the dispatcher when picking a new process after an interrupt. If there are no processes then return null. For example,
     * FCFS will always pick the next process in the queue.
     *
     * @return The selected process or null if there is no new process to select
     */
    protected abstract Process selectProcess();

    public void fetchExecute() {
        //-- SHORT TERM SCHEDULING --//

        Process currentProcess = running;
        Process oldProcess = currentProcess;
        //System.out.println("Running: "+currentProcess);
        //if the current process is not null check if it has an instruction
        //if it does not then the process is done, terminate it
        //otherwise have the algorithm handle an existing process
        if(currentProcess!=null) {
            if (!currentProcess.hasNextInstruction()) {
                //System.out.println("No more instructions - terminating");
                //no more instructions terminate the program
                currentProcess.setLeaveTime(simulation.getTime());
                currentProcess = null;
            } else {
                currentProcess = readmitProcess(currentProcess);
            }
        }

        //if there is no process
        if(currentProcess==null)
            currentProcess = selectProcess(); //go back to A

        //have the algorithm handle the newly admitted process
        //This should only occur if a new process has been made ready
        //for example this could involve incrementing the time quanta to 1 to show that the process is executing its time quanta to 1 for its first execution
        if(currentProcess!=oldProcess)
            readmitProcess(currentProcess);

        this.running = currentProcess;

        //-- FETCH - EXECUTE --//

        if(currentProcess!=null) {
            //-- Fetch Instruction -- //
            //fetch the page the instruction is located
            Instruction instruction = currentProcess.currentInstruction();
            //fetch the instruction from memory
            Frame frame = simulation.getMediumScheduler().getFrame(currentProcess,instruction.getPage());

            //if the frame cannot be found then we must throw a page fault
            if(frame==null) {
                //block the currently running process
                block(currentProcess);
                this.running = null;
                instruction.setFaultTime(simulation.getTime());
                //Issuing a page fault and blocking a process takes no time. So multiple page faults may occur and then another ready process can run immediately at the same time unit.
                //run this again for the next process in the queue
                fetchExecute();
                return;
            }

            //-- Execute Instruction --//
            //System.out.println("Executing " + currentProcess.getId());
            //finally execute the instruction
            currentProcess.nextInstruction();
            simulation.addEvent(new FetchExecuteEvent(simulation.getTime() + 1, currentProcess, this));
        }
    }

}
