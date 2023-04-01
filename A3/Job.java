import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * File: Job.java
 *
 * Author: Jacob Boyce
 * Student Number: c3264527
 * Course: COMP2240
 *
 * Represents a task that CAN BE scheduled by a processor. Each job has a unique id, a time at which it arrives and all of the instructions that need to be executed. A process is distinct from the job
 * as a process is a job that is being run by a processor
 */
public class Job {

    private final String name;
    private final int id;
    private final List<Integer> instructions;

    /**
     * Creates a new job
     *
     * @param name The human-readable simple name for the job
     * @param id The unique id of the job
     */
    public Job(String name, int id) {
        this.name = name;
        this.id = id;
        this.instructions = new ArrayList<>();
    }

    /**
     * Adds a new instruction to the job. Each instruction represents the page the instruction
     * resides on.
     *
     * @param instruction The page the instruction resides on
     */
    public void addInstruction(int instruction) {
        this.instructions.add(instruction);
    }

    /**
     * @return A list of all of the instructions to be run for the job.
     */
    public List<Integer> getInstructions() {
        return Collections.unmodifiableList(instructions);
    }

    /**
     * @return The unique Id of the process
     */
    public int getId() {
        return id;
    }

    /**
     * @return A human-readable name of the process
     */
    public String getName() {
        return name;
    }
}
