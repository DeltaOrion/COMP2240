import java.io.File;
import java.io.FileNotFoundException;

/**
 * File: P1.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 *
 * Main driver for running the assignment 1 program for problem 1 "Sharing the problem1.Bridge". In this problem there are several farmers
 * each trying to cross the bridge. Each farmer will either cross north or south. There may be at most 1 farmer on the bridge at any given time (deadlock). Farmers
 * going either north or southbound should not be denied access from the bridge (starvation). Every time a farmer crosses a bridge a number is incremented (NEON). Once
 * Neon reaches 100 the program terminates
 *
 * This program uses a FIFO solution where the first farmer to request access to the bridge is given access. Subsequent requests are then appended to the end of the queue. Proceeding
 * requests are served in the order they come.
 *
 * This program will produce an output as follows
 *
 * problem1.Farmer: Waiting for problem1.Bridge. Going towards X
 * problem1.Farmer: Crossing the bridge step 5
 * problem1.Farmer: Crossing the bridge step 10
 * ....
 * problem1.Farmer: Across the bridge.
 * NEON = 1
 *  ... farmer output
 * NEON = 100
 *
 */
public class P1 {

    /*
     * Important Note.
     * As suggested in the JavaDocs for locks https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/locks/Lock.html
     * I use the following try, finally pattern when acquiring and releasing semaphores when using them as a mutex (lock).
     * This ensures that even if a return or exception occurs in the critical section the mutex will always be released.
     *
     * It is also suggested when handling an InterruptException to restore the interrupt
     * and handle it accordingly (clean up variables) so that is what I have done.
     *
     *   try {
     *      mutex.acquire(); //acquire (semaphore) lock
     *   } catch (InterruptedException e) {
     *      Thread.currentThread().interrupt(); //restore interrupt
     *      //cleanup program
     *   }
     *
     *   try {
     *       //do whatever in critical section
     *   } finally {
     *       mutex.release(); //release semaphore (lock)
     *   }
     */

    /**
     * Runs the assignment 1 program for problem 1. This will read the input
     * file from the command line
     *
     * @param args 0 - the name of the file
     */
    public static void main(String[] args) {
        Bridge bridge = new Bridge();
        //get the input from file or from one of the test cases
        BridgeInputConfig config = getConfig(bridge,args);
        if(config==null)
            return;

        //set up all of the farmers
        for(Farmer farmer : config.getFarmers()) {
            bridge.addFarmer(farmer);
            //create a new thread for each farmer
            Thread thread = new Thread(farmer);
            thread.start();
        }

        //start the simulation
        bridge.start();
    }

    private static BridgeInputConfig getConfig(Bridge bridge, String[] args) {
        //return getTestCase4(bridge);
        ///*
        if(args.length==0) {
            System.err.println("No input specified!");
            return null;
        }

        //read the input file
        try {
            return new FileBridgeInputConfig(bridge,new File(args[0]));
        } catch (FileNotFoundException e) {
            System.err.println("Could not find file '"+args[0]+"'");
        }
        return null;
        //*/
    }

    /**
     *  BELOW ARE SOME OF THE TEST CASES USED. I KEPT THIS FOR THE REPORT WHICH ASKS TO DISCUSS TESTING
     *  USED
     */

    /**
     * Test case 1.
     *
     * Input: This tests the simplest case where there is a single farmer
     * Output: It is expected that this single farmer alternates back and forth
     */
    private static BridgeInputConfig getTestCase1(Bridge bridge) {
        TestBridgeInputConfig config = new TestBridgeInputConfig();
        config.addFarmer(new Farmer(bridge,"N_Farmer1",TravelDirection.NORTHBOUND));
        return config;
    }

    /**
     * Test Case 2.
     *
     * Input: This tests a stream of farmers
     * Output: The farmers 1 by 1 cross the bridge to the other side, then all of them cross 1 by one to the other side
     */
    private static BridgeInputConfig getTestCase2(Bridge bridge) {
        TestBridgeInputConfig config = new TestBridgeInputConfig();
        for(int i = 0;i<5;i++) {
            config.addFarmer(new Farmer(bridge, "N_Farmer"+i, TravelDirection.NORTHBOUND));
        }
        return config;
    }

    /*
     * Test case 3
     *
     * Input: Tests 2 farmers on each side
     * Output: The farmers go back and forth in a FIFO manner, this should ensure that there is
     * eventually a cycle for example N1, N2, S1, S2, N1, N2 ...
     */
    private static BridgeInputConfig getTestCase3(Bridge bridge) {
        TestBridgeInputConfig config = new TestBridgeInputConfig();
        for(int i = 0;i<2;i++) {
            config.addFarmer(new Farmer(bridge, "N_Farmer"+i, TravelDirection.NORTHBOUND));
        }

        for(int i = 0;i<2;i++) {
            config.addFarmer(new Farmer(bridge, "S_Farmer"+i, TravelDirection.SOUTHBOUND));
        }
        return config;
    }


    /**
     * Test case 4
     *
     * Input: Tests 1 farmer on one side and 10 on the other side
     * Output: The 1 farmer should not be denied access by the 10 from the other side. It should once again
     * form a cycle eventually S1,S2,S3...N1,S1,S2,S3  ... however the order is non-deterministic
     */
    private static BridgeInputConfig getTestCase4(Bridge bridge) {
        TestBridgeInputConfig config = new TestBridgeInputConfig();
        config.addFarmer(new Farmer(bridge,"N_Farmer1",TravelDirection.NORTHBOUND));

        for(int i=0;i<10;i++) {
            config.addFarmer(new Farmer(bridge,"S_Farmer"+i,TravelDirection.SOUTHBOUND));
        }
        return config;
    }

    /*
     * Test case 5
     *
     * Input: Tests 100 farmers on each side
     * Output: Each farmer should cross the bridge exactly once
     */
    private static BridgeInputConfig getTestCase5(Bridge bridge) {
        TestBridgeInputConfig config = new TestBridgeInputConfig();
        for(int i = 0;i<100;i++) {
            config.addFarmer(new Farmer(bridge, "N_Farmer"+i, TravelDirection.NORTHBOUND));
        }
        return config;
    }
}
