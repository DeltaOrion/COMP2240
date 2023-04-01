/**
 * File: ParlorProblem.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 *
 * Main driver program for problem 3. This is an implementation of the parlor problem using monitors.
 * See more in {@link ParlorProblem}
 */
public class P3 extends ParlorProblem {

    //create an instance of P3 and run it, this is because
    //parlor problem has some abstract methods which cannot be static.
    private final static P3 instance = new P3();

    public static void main(String[] args) {
        instance.run(args);
    }

    @Override
    protected IceCreamParlor getParlor() {
        //return a monitor parlor
        return new MonitorIceCreamParlor();
    }

    @Override
    protected boolean isMonitor() {
        //we are definetely using monitors for customers
        return true;
    }
}
