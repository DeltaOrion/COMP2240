import java.util.concurrent.Semaphore;

/**
 * File: Farmer.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * Program Description:
 *
 * A farmer will continually attempt to cross the bridge back and forth. Once it has fully crossed the bridge then it will
 * turn around to cross it again. While crossing the bridge the farmer will take 5 steps every 20 milliseconds. A farmer
 * is not allowed to cross a bridge unless it is instructed to do so. When a farmer wants to use a bridge it must signal to
 * the bridge to do so, and wait until it is its turn to use the bridge.
 *
 * The farmer does the following
 *    1. Wait for the simulation to start
 *    2. request access to use the bridge
 *    3. wait for the request to be granted
 *    4. cross the bridge
 *    5. turn around and go back to step 2
 *
 */
public class Farmer implements Runnable {

    private final Bridge bridge;
    private final String name;
    private TravelDirection travelling;
    private int currentStep;
    private final Semaphore sync;
    private boolean complete;

    private final Semaphore mutex;


    public Farmer(Bridge bridge, String name, TravelDirection travelling) {
        this.bridge = bridge;
        this.name = name;
        this.travelling = travelling;
        this.sync = new Semaphore(0);
        this.mutex = new Semaphore(1);
        this.complete = false;
    }


    /**
     * Signals to the farmer that it is allowed to cross the bridge.
     */
    public void cross() {
        sync.release();
    }

    /**
     * Runs the farmer. The farmer will initially wait until the simulation is started. Once started the farmer will attempt to cross the bridge.
     * Once it has fully crossed the bridge then it will turn around to cross it again. While crossing the bridge the farmer will take 5 steps every 20 milliseconds.
     * A farmer is not allowed to cross a bridge unless it is instructed to do so by the bridge. When a farmer wants to use a bridge it must signal to
     * the bridge to do so, and wait until it is its turn to use the bridge.
     */
    @Override
    public void run() {
        try {
            //wait for the simulation to start
            sync.acquire();

            while (true) {
                sendMessage("Waiting for Bridge. Going towards " + travelling.getDisplayName());
                //signal a request to the bridge that this farmer wishes to access it
                //-- Signal to use the bridge --//
                bridge.request(this);
                //wait for the bridge until this farmer is allowed to cross it. This semaphore is only signaled
                //if it is the farmers turn to use the bridge
                //-- Wait for Bridge --//
                sync.acquire();

                //if the simulation is complete while we were waiting then exit
                if(icComplete())
                    return;

                //-- Cross the Bridge --//
                while (currentStep < bridge.getMaxSteps()) {
                    //wait 20 seconds on the bridge before taking 5 steps
                    Thread.sleep(20);
                    currentStep += 5;
                    //if the simulation terminated while we waited then exit (this shouldn't be possible unless a thread was interrupted)
                    if(icComplete())
                        return;

                    if(currentStep!=bridge.getMaxSteps())
                        sendMessage("Crossing Bridge Step " + currentStep+".");
                }

                //-- Turn around and cross the bridge again --//
                //once we have crossed the bridge then start crossing the opposite direction
                sendMessage("Across the Bridge.");
                currentStep = 0;
                travelling = travelling.opposite();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Checks whether the simulation is complete. If so the farmer should terminate.
     *
     * @return Whether the simulation has been terminated
     */
    private boolean icComplete() throws InterruptedException {
        //enter the critical section
        mutex.acquire();
        try {
            return complete;
        } finally {
            //leave the critical section.
            mutex.release();
        }
    }

    public void start() {
        sync.release();
    }

    public void terminate() {
        try {
            mutex.acquire(); //enter critical section
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        try {
            complete = true; //indicate that the simulation has now been complete
            sync.release(); //stop the farmer from waiting so the thread terminates
        } finally {
            mutex.release(); //exit the critical section
        }
    }

    /**
     * Prints a message from a farmer to standard out. This method
     * appends the name of the farmer to any message sent
     *
     * @param message The message to send
     */
    private void sendMessage(String message) {
        System.out.println(name + ": "+message);
    }
}
