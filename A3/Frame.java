import java.util.Objects;

/**
 * File: Frame.java
 *
 * Author: Jacob Boyce
 * Student Number: c3264527
 * Course: COMP2240
 *
 * Represents a memory frame. Each memory frame is the same size as a memory page meaning each frame can hold one page. A page is represented by
 * an integer and the process assigned to said page. Each frame can only have one process stored in it at any time.
 *
 * A page consists of a number {@link #getCurrentPage()} and the process assigned to that page {@link #getAllocatedProcess()}.
 */
public class Frame {
    //id of the frame
    private final int id;
    //used by scheduling algorithms
    private int refCounter;

    //the page allocated to the frame.
    private int currentPage;
    private Process allocatedProcess;

    /**
     * Creates a new frame
     *
     * @param id The id of this frame
     */
    public Frame(int id) {
        this.id = id;
        this.currentPage = -1;
        this.allocatedProcess = null;
        this.refCounter = 0;
    }

    /**
     * @return The id of this frame
     */
    public int getId() {
        return id;
    }

    /**
     * @return The number of the page allocated to this frame. One should also check the allocated process using {@link #getAllocatedProcess()}
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * Sets the current page number to the next page. One should also set the allocated process using {@link #setAllocatedProcess(Process)} when
     * setting the page
     *
     * @param newPage The new page index
     */
    public void setCurrentPage(int newPage) {
        this.currentPage = newPage;
    }

    /**
     * @return the reference bit used by scheduling algorithms
     */
    public int getRefCounter() {
        return refCounter;
    }

    /**
     * Sets the reference bit used by scheduling algorithms
     *
     * @param refCounter The new reference bit
     */
    public void setRefCounter(int refCounter) {
        this.refCounter = refCounter;
    }

    /**
     *
     * @return The process allocated to the page, null if no page is allocated
     */
    public Process getAllocatedProcess() {
        return allocatedProcess;
    }

    /**
     * @param allocatedProcess The process allocated to the page placed in this memory frame. One should also set the page number using
     *                         {@link #setAllocatedProcess(Process)}
     */
    public void setAllocatedProcess(Process allocatedProcess) {
        this.allocatedProcess = allocatedProcess;
    }

    @Override
    public String toString() {
        return "Frame{" +
                "id=" + id +
                ", currentPage=" + currentPage +
                '}';
    }

    /**
     * Checks whether the page currently allocated to the frame is the same as the
     * page given.
     *
     * @param process The process of the given page
     * @param page The page number of the given page
     * @return Whether the currently allocated page is the same as the page given.
     */
    public boolean equals(Process process, int page) {
        return this.currentPage == page && Objects.equals(process,allocatedProcess);
    }
}
