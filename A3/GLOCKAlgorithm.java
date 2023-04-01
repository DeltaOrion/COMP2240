/**
 * File: GLOCKAlgorithm.java
 *
 * Author: Jacob Boyce
 * Student Number: c3264527
 * Course: COMP2240
 *
 * See more at {@link ReplacementPolicy}
 *
 * This policy is the generalised version of the Clock policy introduced in the lecture. The frames are considered as circular buffer and
 * initially the Clock pointer is set to indicate the first frame (as in the simple Clock policy). Each frame has a reference counter
 * (instead of a reference bit as in simple Clock) initialized to zero. When a page is loaded the reference counter of that frames is set to
 * zero and after the first reference to that page the reference counter is set to 1 and the clock pointer is incremented to point to the next frame.
 * Whenever the page is subsequently referenced the reference counter is incremented by one. The replacement procedure for GCLOCK works similar to Clock policy.
 * For replacement the GCLOCK scans through the buffer from the pointer’s current position looking for a reference counter with value zero. If the counter
 * is not zero it is decremented by one and the pointer moves to the next frame. When a frame with reference counter zero is found its current page is replaced
 * with the new page and the pointer is set to the next frame.
 */
public class GLOCKAlgorithm implements ReplacementPolicy {

    //the frames that are used by the GLOCK circular buffer
    private final Frame[] frames;
    //the clock pointer pointing to each frame instance
    private int clockPointer = 0;

    /**
     * Generates a new GLOCK Algorithm using the given memory frames.
     *
     * @param frames The frames to be used for this clock algorithm instance.
     */
    public GLOCKAlgorithm(Frame[] frames) {
        this.frames = frames;
    }

    /**
     * Replaces a frame using the GLOCK policy.
     *
     * @param process The process allocated to the page
     * @param newPage The page number to replace
     */
    @Override
    public void replace(Process process, int newPage) {
        //check that this replacement has not already occurred
        //this might happen if multiple processes page fault at the same time for a particular page
        if(pageExists(process,newPage))
            return;

        //get the next frame to replace by looping through circular buffer
        Frame frame = selectFrame();

        //replace the frame with the new page
        frame.setCurrentPage(newPage);
        frame.setAllocatedProcess(process);
        frame.setRefCounter(0);

        //debug(newPage);
    }

    /**
     * Checks whether the given page exists inside of this memory space.
     *
     * @param process The process the page belongs to
     * @param newPage The index of the page to be replaced
     * @return true if the page exists, false if otherwise
     */
    private boolean pageExists(Process process, int newPage) {
        for(Frame frame : getResidentSet()) {
            if(frame.equals(process,newPage))
                return true;
        }
        return false;
    }

    /**
     * Selects the memory frame to replace when using the GLOCK replacement algorithm. This is done
     * by looping through the memory buffer and finding the first frame with a ref counter of 0.
     *
     * @return The frame to pick.
     */
    private Frame selectFrame() {
        //loop through all frames until a suitable frame is found
        while (true) {
            //get the next frame of the clock pointer
            Frame frame = frames[clockPointer];
            //When a frame with reference counter zero is found its current page is replaced with the new page and the pointer is set to the next frame.
            incrementClock();
            if(frame.getRefCounter()==0) {
                //if the counter is 0 return it
                return frame;
            } else {
                //otherwise decrement the counter
                frame.setRefCounter(frame.getRefCounter()-1);
            }
        }
    }

    /**
     * Moves the frame pointer in the circular buffer
     */
    private void incrementClock() {
        //modding ensures it going around in a circle
        clockPointer = (clockPointer+1) % frames.length;
    }

    @Override
    public void reference(Frame frame) {
        //increment the reference bit
        frame.setRefCounter(frame.getRefCounter()+1);
    }

    @Override
    public void reference(Process process, int page) {
        //find the frame using the page definition
        for(Frame frame : frames) {
            //increment the reference bit
            if(frame.equals(process,page))
                frame.setRefCounter(frame.getRefCounter()+1);
        }
    }

    private void debug(int newPage) {
        System.out.print("Replaced "+newPage+" - [");
        for(Frame f : frames) {
            System.out.print(f.getCurrentPage() + ",");
        }
        System.out.println("]");
    }

    @Override
    public Frame[] getResidentSet() {
        return frames;
    }

    @Override
    public String getName() {
        return "GCLOCK";
    }
}
