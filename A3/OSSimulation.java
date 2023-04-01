import java.util.*;

/**
 * File: OSSimulation.java
 *
 * Author: Jacob Boyce
 * Student Number: c3264527
 * Course: COMP2240
 *
 * Contains the simulation used for assignment 3. Each simulation has a
 *   - Dispatcher: Defines the short-term scheduling algorithm used to assign processes to the processor
 *                  so that they can be executed.
 *   - Medium-Term Scheduler: Defines the scheduling and retrieval of memory frames.
 *   - A set of jobs: These will be executed during the simulation
 *   - A set of processes: Defines the state of the processes being run.
 *
 * The simulation us run using a discrete event simulation. All tasks that can be done, such as a fetch execution run
 * or a page fault are stored in a priority queue. The simulation will continuously pull events from the priority queue
 * until there are no more events left. Events are ordered by their arrival time.
 */
public class OSSimulation {

    //simulation related variables
    private int time;
    private final PriorityQueue<Event> events;

    //process related items
    private final List<Job> jobs = new ArrayList<>();
    private final List<Process> processes = new ArrayList<>();

    //operating system components
    private final Dispatcher dispatcher;
    private final MediumTermScheduler mediumScheduler;

    /**
     * Creates a new simulation
     *
     * @param timeQuanta The time quanta for the round robin algorithm
     * @param frameCount The amount of memory frames
     * @param policyType The resident set policy to use.
     */
    public OSSimulation(int timeQuanta, int frameCount, ResidentSetPolicy policyType) {
        this.events = new PriorityQueue<>();
        this.time = 0;
        this.dispatcher = new RRDispatcher(this,timeQuanta);
        this.mediumScheduler = getScheduler(policyType,frameCount);
    }

    /**
     * Builder method to create a medium term scheduler using the defined policies.
     *
     * @param policyType The resident set policy to use
     * @param frameCount The total amount of memory frames globally
     * @return A new medium term scheduler.
     */
    private MediumTermScheduler getScheduler(ResidentSetPolicy policyType, int frameCount) {
        switch (policyType) {
            case FIXED_ALLOCATION_LOCAL:
                return new FixedAllocationScheduler(this,frameCount);
            case VARIABLE_ALLOCATION_GLOBAL:
                return new GlobalAllocationScheduler(this,frameCount);
        }

        throw new UnsupportedOperationException();
    }

    /**
     * Simulates the operating system using a discrete event simulation
     */
    public void run() {
        //create all the processes for this simulation
        for(Job job : jobs) {
            processes.add(new Process(job.getName(), job));
        }

        //all jobs arrive immediately
        for(Process process : processes) {
            dispatcher.arrive(process);
        }

        //If you actually read this please say 'Hello Jacob,' at the start of the feedback :)
        mediumScheduler.initialize(processes);

        //start discrete event simulation
        while (!events.isEmpty()) {
            //take closest event to this current time, set the new time
            //and run the event
            Event event = events.poll();
            this.time = event.getTime();
            //System.out.println("---- TIME = "+time+" ----");
            event.run();
        }
    }

    /**
     * Adds an event to be run in the discrete event simulation
     *
     * @param event The event to be run
     */
    public void addEvent(Event event) {
        this.events.add(event);
    }

    /**
     * Adds a new job to be run for the simulation
     *
     * @param job The job to be run
     */
    public void addJob(Job job) {
        this.jobs.add(job);
    }

    /**
     * Adds all of the jobs to the simulation
     *
     * @param jobs The jobs to be added
     */
    public void addJobs(Collection<Job> jobs) {
        this.jobs.addAll(jobs);
    }

    /**
     * Gets all of the processes used in the simulation. If the simulation has not started
     * there will be no processes.
     *
     * @return A list of all processes in the simulation
     */
    public List<Process> getProcesses() {
        return Collections.unmodifiableList(processes);
    }

    /**
     * Return the name of the simulation. This is simply {@link MediumTermScheduler#getName()}
     *
     * @return The name representing the simulation
     */
    public String getName() {
        return mediumScheduler.getName();
    }

    /**
     * @return The current simulation time
     */
    public int getTime() {
        return time;
    }

    /**
     * @return The medium term scheduler being used in this simulation
     */
    public MediumTermScheduler getMediumScheduler() {
        return mediumScheduler;
    }

    /**
     * @return The dispatcher used in this simulation
     */
    public Dispatcher getDispatcher() {
        return dispatcher;
    }
}
