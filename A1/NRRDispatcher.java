/**
 * File: NRRDispatcher.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * Represents an {@link AbstractRRDispatcher} running the Narrow Round Robin variant of the RR algorithm. Every time a process is readmitted to the queue, its time quanta
 * is made smaller. In this case it is made continually smaller until it has a time quanta of 2.
 */
public class NRRDispatcher extends AbstractRRDispatcher {

    private final static int MIN_TIME_QUANTA = 2;
    private final static int BASE_TIME_QUANTA = 4;

    protected NRRDispatcher(int contextSwitchTime) {
        super("NRR",contextSwitchTime, BASE_TIME_QUANTA);
    }

    @Override
    protected void modifyQuanta(Process process) {
        //modify the quanta to be one less
        int newTimeQuanta = Math.max(process.getTimeQuanta()-1,MIN_TIME_QUANTA);
        process.setTimeQuanta(newTimeQuanta);
    }
}
