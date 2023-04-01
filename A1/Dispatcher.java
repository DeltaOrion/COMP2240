import java.util.*;

/**
 * File: Dispatcher.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * Represents a short term scheduling algorithm. A dispatcher, also known as the short term scheduler decides which process to pick
 * from a pool of available processes to be run on the processor. This is represented using a 'discrete event simulation'. {@link Task} are added to a priority queue.
 * The next task to be run is based on the one with the lowest time.
 *
 * Each dispatcher has a list of jobs that it needs to run over its lifetime. Jobs are added to the dispatcher runtime on their scheduled arrival times. The processes are then 'executed' and
 * once they have been executed then stats will be recorded on them.
 */
public abstract class Dispatcher {

    private final String name;
    private final List<Process> processes;
    private final PriorityQueue<Task> simulation;
    private int time;
    private final int contextSwitchTime;

    /**
     * Creates a new dispatcher
     *
     * @param name The name of the algorithm being used
     * @param contextSwitchTime The amount of time taken to switch processes
     */
    public Dispatcher(String name, int contextSwitchTime) {
        this.name = name;
        this.contextSwitchTime = contextSwitchTime;
        this.processes = new ArrayList<>();
        this.simulation = new PriorityQueue<>();
        this.time = 0;
    }

    /**
     * Called when a process arrives to the dispatcher. The algorithm will then decide what to do with the new arrival, this could involve
     * preempting the existing process or simply adding it to a queue.
     *
     * @param process The process to add to the dispatcher.
     */
    public abstract void arrive(Process process);

    /**
     * Causes the processor to stop executing the current running task to admit a new one. This could either involve
     *   - The current running process(es) have finished, in that case it should be released
     *   - The running process(es) time quanta has expired, the process should then be context switched out
     *   - The running process(es) are being preempted and need to be context switched out
     *
     * Either a new process should be admitted to the processor(s) or the processor(s) should become idle.
     */
    public abstract void interrupt();

    /**
     * Simulates the dispatcher algorithm being run on the processor(s). This is run using a discrete event simulation
     */
    public void run() {
        for(Process process : processes) {
            //add all new arrivals
            addTask(new ArriveTask(this,process));
        }

        //continue polling tasks until no more have been added to the queue
        while (!simulation.isEmpty()) {
            Task task = simulation.poll();
            time = task.getStartTime();
            task.run();
        }
    }

    /**
     * Performs all time related events relating to a context switch. This involves changing the simulation time.
     */
    protected void contextSwitch() {
        time += contextSwitchTime;
    }

    /**
     * Adds a job to be run by the dispatcher for the simulation.
     *
     * @param job The job to be run
     */
    public void addJob(Job job) {
        this.processes.add(new Process(job));
    }

    /**
     * @return A list of all processes being run by the simulation
     */
    public Collection<Process> getProcesses() {
        return Collections.unmodifiableList(processes);
    }

    /**
     * @return The name of the algorithm used by the dispatcher
     */
    public String getName() {
        return name;
    }

    /**
     * As the simulation runs, the current time changes depending on the next event being run.
     *
     * @return The current time of the simulation.
     */
    public int getTime() {
        return time;
    }

    /**
     * Adds a task to be run by the discrete event simulation. Tasks will be ordered by the time executed and other factors
     *
     * @param task The new task to be run.
     */
    public void addTask(Task task) {
        this.simulation.add(task);
    }
}
