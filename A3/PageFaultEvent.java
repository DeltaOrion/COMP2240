/**
 * File: PageFaultEvent.java
 *
 * Author: Jacob Boyce
 * Student Number: c3264527
 * Course: COMP2240
 *
 * When a page fault occurs, the interrupt routine will handle the fault by blocking that process and placing an I/O request.
 * The I/O controller will process the I/O request and bring the page into main memory. This may require replacing a page in
 * main memory using a page replacement policy (GCLOCK with local or global scope). Other ready processes should be scheduled
 * to run while such an I/O operation is occurring.
 */
public class PageFaultEvent extends Event {

    private final MediumTermScheduler scheduler;
    private final int page;

    /**
     * Creates a new page fault event
     *
     * @param process The process involved with the fault
     * @param time The time when the page fault finishes
     * @param scheduler The scheduler being used to replace the fault
     * @param page The page being placed into memory.
     */
    public PageFaultEvent(Process process, int time, MediumTermScheduler scheduler, int page) {
        super(time,process);
        this.scheduler = scheduler;
        this.page = page;
    }

    @Override
    public void run() {
        scheduler.replace(getProcess(),page);
    }
}
