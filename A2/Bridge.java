import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * File: Bridge.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 *
 * The bridge is a shared resource across all of the farmers. Farmers will initially start off as North and Southbound.
 * Once a farmer has crossed a bridge it will then try to cross the bridge again immediately in the other direction.
 *
 * Only one farmer may be on the bridge at a given time and a farmer must use it for 20 steps. Thus for fairness farmers are served to the bridge in a FCFS basis. That is
 * when the farmer {@link #request(Farmer)} that it wishes to use the bridge, from completing its crossing or otherwise it is added to the end of
 * a queue until all previous requests are served first.
 *
 * An alternative solution would be to simply queue farmers on a single semaphore rather than storing them in a queue, but this solution
 * was chosen because it allows for more flexibility for how the farmers are given access to the bridge, whereas if the single semaphore
 * solution was used, the only method of adding the farmers to the bridge would be FCFS.
 *
 * To start the simulation the {@link #start()} should be used.
 *
 * Once the neon light turns to 100 the simulation ends and all farmers are stopped. Access to the bridge is essentially closed.
 */
public class Bridge {

    private final Semaphore mutex; //semaphore for enforcing mutual exclusion when modifying the state of the bridge
    private Farmer crossingFarmer;
    private final int maxSteps = 20;
    private boolean crossing;
    private int neon;

    private final Queue<Farmer> farmerQueue; //FIFO queue for determining which farmer should use the bridge next
    private List<Farmer> farmers = new ArrayList<>(); //list of farmers for starting and terminating the simulation


    /**
     * Creates a new bridge. This bridge will be initialized so that no one is crossing it. Farmers should
     * be added using {@link #addFarmer(Farmer)}. The simulation can then be started using {@link #start()}
     *
     */
    public Bridge() {
        this.farmerQueue = new ArrayDeque<>();
        this.mutex = new Semaphore(1);
        this.neon = 0;
        this.crossing = false;
    }

    /**
     * Starts the simulation. Once started the farmers will continually crossing northbound and southbound
     * until the neon sign reaches 100. This method should only be called once all farmers are added
     * using {@link #addFarmer(Farmer)}
     */
    public void start() {
        //move all of the farmers into an immutable collection before starting the loop, this is to ensure
        //future calls to this collection are thread safe as only shared mutable state needs to be protected
        //by mutual exclusion. No new farmers may enter the simulation once it starts.
        farmers = Collections.unmodifiableList(farmers);

        for(Farmer farmer : farmers)
            farmer.start(); //start all of the farmers
    }

    /**
     * Signals that a farmer is ready to use the bridge. Whether from completing its crossing of the bridge or as a
     * first time crossing. This request will be added to the end of a queue until all subsequent use requests are
     * handed out.
     *
     * @param farmer The farmer signalling its use of the bridge
     */
    public void request(Farmer farmer) {
        //enter critical section, i.e. the shared mutable state of the bridge (the shared resource)
        //this method is used for ensuring there is no deadlock and that access to the bridge
        //is threadsafe.
        try {
            mutex.acquire();
            try {
                //add this farmer to the queue of waiting farmers
                farmerQueue.add(farmer);
                if (farmer.equals(crossingFarmer)) {
                    //if this farmer is the farmer crossing the bridge then they have successfully crossed
                    //add one to the neon sign
                    neon++;
                    System.out.println("NEON = " + neon);
                    if (neon == 100) {
                        //if this is the last crossing then stop the simulation
                        terminate();
                        //remember the mutex is in a finally which means it will always be released don't worry
                        return;
                    }

                    //as the farmer just finished crossing indicate that no-one is on the bridge
                    crossingFarmer = null;
                    crossing = false;
                }

                //if no one is crossing the bridge, from either the previous farmer signaling or by this
                //farmer being the first farmer then add the next waiting farmer to the bridge
                if (!crossing)
                    releaseFarmer();

            } finally {
                //leave the critical section
                mutex.release();
            }
        } catch (InterruptedException e) {
            //if the thread is interrupted simply terminate the simulation.
            Thread.currentThread().interrupt();
            terminate();
        }
    }

    /**
     * Terminates the simulation. This will notify all farmers that the simulation has ended.
     */
    private void terminate() {
        for(Farmer farmer : farmers)
            farmer.terminate();
    }

    /**
     * Releases a farmer onto the bridge. Requests to use the bridge are served on a FCFS basis. That is when the farmer signals to use the bridge
     * from completing a crossing or otherwise, it must wait until all previous requests are served.
     */
    private void releaseFarmer() {
        if(farmerQueue.isEmpty())
            return;

        //take the next farmer from the queue
        crossingFarmer = farmerQueue.poll();
        //signal to the farmer it can cross
        crossingFarmer.cross();
        //indicate that the bridge is now being crossed
        crossing = true;
    }

    /**
     * @return The maximum amount of steps needed to cross the bridge
     */
    public int getMaxSteps() {
        return maxSteps;
    }

    /**
     * Adds the farmer to the bridge crossing simulation. All farmers should be added prior to using the {@link #start()} method. Once the simulation
     * is started no new farmers can be added to it.
     *
     * @param farmer the farmer to add to the simulation.
     */
    public void addFarmer(Farmer farmer) {
        farmers.add(farmer);
    }


}
