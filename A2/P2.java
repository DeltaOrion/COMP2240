/**
 * File: ParlorProblem.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 *
 * Main driver program for problem 2. This is an implementation of the parlor problem using semaphores.
 * See more in {@link ParlorProblem}
 */
public class P2 extends ParlorProblem {

    //create an instance of P3 and run it, this is because
    //parlor problem has some abstract methods which cannot be static.
    private static final P2 instance = new P2();

    public static void main(String[] args) {
        instance.run(args);
    }

    @Override
    protected IceCreamParlor getParlor() {
        return new SemaphoreIceCreamParlor();
    }

    @Override
    protected boolean isMonitor() {
        //we aren't using monitors only semaphores
        return false;
    }
}
