/**
 * File: Instruction.java
 *
 * Author: Jacob Boyce
 * Student Number: c3264527
 * Course: COMP2240
 *
 * Represents an instruction to be executed by a processor. Each instructions represents the page that
 * the instruction is located on. If a page fault occurs when the instruction runs this should be recorded.
 */
public class Instruction {

    private final int page;
    private int faultTime;

    /**
     * Creates a new instruction
     *
     * @param page The page the instruction is located on
     */
    public Instruction(int page) {
        this.page = page;
        this.faultTime = -1;
    }

    /**
     * @return The page this instruction is located on
     */
    public int getPage() {
        return page;
    }

    /**
     * @return The time this page faulted. -1 if it did not fault
     */
    public int getFaultTime() {
        return faultTime;
    }

    /**
     * Sets the time this instruction faulted at
     *
     * @param faultTime The time this instruction faulted at. -1 if it did not fault.
     */
    public void setFaultTime(int faultTime) {
        this.faultTime = faultTime;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "page=" + page +
                '}';
    }
}
