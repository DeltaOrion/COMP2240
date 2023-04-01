import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * File: FeedbackDispatcher.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * Represents a dispatcher running the Feedback (FB constant) algorithm. This algorithm *typically* does not know the service time of the process. By running the process time and time
 * again it drops priority. When the algorithm is selecting a process is looks through the queue with the highest priority, if nothing is there then it looks at the next highest queue, unless the currently
 * running process has a lower priority. Once a process runs its priority drops until it reaches the lowest priority.
 */
public class FeedbackDispatcher extends SingleCoreDispatcher {

    private final List<Queue<Process>> processQueues;
    private final static int MAX_PRIORITIES = 6;
    private final static int TIME_QUANTA = 4;

    /**
     * Creates a feedback dispatcher.
     *
     * @param contextSwitchTime The time taken to swap processes
     */
    public FeedbackDispatcher(int contextSwitchTime) {
        super("FB (constant)", contextSwitchTime);
        this.processQueues = new ArrayList<>();
        for(int i = 0; i< MAX_PRIORITIES; i++) {
            processQueues.add(new ArrayDeque<>()); //add all process queues
        }
    }

    @Override
    protected void handleArrival(Process process) {
        //set to highest priority
        process.setPriority(0);
        process.setTimeQuanta(TIME_QUANTA);
        //add to highest queue!
        processQueues.get(0).add(process);
    }

    @Override
    protected Process selectProcess() {
        Process runningProcess = getRunningProcess();
        int priority = 0;
        //loop through queues, highest to lowest priority
        for(Queue<Process> processes : processQueues) {
            //select the process from the current queue if there is one
            if(!processes.isEmpty())
                return processes.poll(); //if it is not empty then

            //otherwise check the running process
            if(priority > runningProcess.getPriority()) {
                if(!runningProcess.willFinish(getTime())) {
                    //up the priority, and return the existing process
                    nextPriority(runningProcess);
                    return runningProcess;
                }
            }

            priority++;
        }

        //if no queues have anything and no process is currently running then make the CPU idle.
        return null;
    }

    @Override
    protected void readmitProcess(Process process) {
        //up the priority and admit it to the queue of that priority
        nextPriority(process);
        processQueues.get(process.getPriority()).add(process);
    }

    private void nextPriority(Process process) {
        process.setPriority(Math.min(MAX_PRIORITIES -1,process.getPriority()+1));
    }

    @Override
    protected void scheduleTask(Process process) {
        process.setStartExecuting(getTime());
        //execute either for the time quanta specified or until the process has finished executing.
        int finishTime = getTime() + process.getRemainingTime();
        int quantaTime = getTime() + process.getTimeQuanta();
        int nextTime = Math.min(finishTime,quantaTime);
        addTask(new InterruptTask(this,process.getProcessId(),nextTime));
    }
}
