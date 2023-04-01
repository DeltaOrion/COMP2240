/**
 * File: RRDispatcher.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * Represents a standard {@link AbstractRRDispatcher}. This does not do anything special each time a process is readmitted.
 */
public class RRDispatcher extends AbstractRRDispatcher {

    private final static int TIME_QUANTA = 4;

    protected RRDispatcher(int contextSwitchTime) {
        super("RR", contextSwitchTime, TIME_QUANTA);
    }

    @Override
    protected void modifyQuanta(Process process) {
        //nothing special here
    }
}
