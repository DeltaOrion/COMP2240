import java.util.*;

/**
 * File: Process.java
 *
 * Author: Jacob Boyce
 * Student Number: c3264527
 * Course: COMP2240
 *
 * Represents a job that has is currently being executed or has fully been executed by a processor. This is distinct from a job as a job represents a task that the processor
 * can run while this represents one that is actively being run
 */
public class Process {

    //identifying information
    private final String name;
    private final int id;

    //instruction word
    private final List<Instruction> instructions;

    //process control block
    private int programCounter = 0;

    //statistics
    private final int arriveTime;
    private int leaveTime;

    /**
     * Create a new process
     *
     * @param name name of the process
     * @param job The job which represents the process
     */
    public Process(String name, Job job) {
        //pull information from the job
        this.name = name;
        this.id = job.getId();
        List<Instruction> instructions = new ArrayList<>();
        for(int page : job.getInstructions()) {
            instructions.add(new Instruction(page));
        }
        this.instructions = Collections.unmodifiableList(instructions);
        this.arriveTime = 0;
    }

    /**
     * @return  the current instruction which needs to be executed by the process
     */
    public Instruction currentInstruction() {
        if(!hasNextInstruction())
            throw new NoSuchElementException();

        return instructions.get(programCounter);
    }

    /**
     * Retrieves the current instruction which needs to be executed by the process and increments
     * the program counter to the next instruction
     *
     * @return The current instruction to be executed.
     */
    public Instruction nextInstruction() {
        if(!hasNextInstruction())
            throw new NoSuchElementException();

        Instruction current = instructions.get(programCounter);
        programCounter++;
        return current;
    }

    /**
     * @return True, if there exists another instruction to execute, false if otherwise
     */
    public boolean hasNextInstruction() {
        return programCounter < instructions.size();
    }

    /**
     * @return The unique id of the process
     */
    public int getId() {
        return id;
    }

    /**
     * @return The time which the process should arrive
     */
    public int getArriveTime() {
        return arriveTime;
    }

    /**
     * @return The time which the process terminated (completed) execution.
     */
    public int getLeaveTime() {
        return leaveTime;
    }

    /**
     * @param leaveTime Sets the termination (completion) time of this process
     */
    public void setLeaveTime(int leaveTime) {
        this.leaveTime = leaveTime;
    }

    /**
     * This returns a list of times in which a page fault occurred. It does not list which instruction faulted, simply
     * when each fault occurred.
     *
     * @return The page fault times
     */
    public Collection<Integer> getFaults() {
        List<Integer> faults = new ArrayList<>();

        //loop through instructions, if the fault time
        //is not -1 then add it to the faults
        for(Instruction instruction : instructions) {
            if(instruction.getFaultTime()!=-1) {
                faults.add(instruction.getFaultTime());
            }
        }
        return faults;
    }

    /**
     * @return The name of the process
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Process{" +
                "id=" + id +
                ", instructions=" + instructions +
                '}';
    }

    /**
     * The turnaround time is the leave time - arrival time of the process
     *
     * @return The process turnaround time
     */
    public int getTurnaroundTime() {
        return leaveTime - arriveTime;
    }
}
