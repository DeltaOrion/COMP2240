import java.io.File;
import java.io.FileNotFoundException;

/**
 * File: ParlorProblem.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 *
 * Main driver to complete the ice cream parlor problem for assignment 2; problem 2 and 3. This is abstracted out as it is identical for both
 * P2 and P3 with the only difference being whether to generate {@link MonitorCustomer} or {@link SemaphoreCustomer}.
 */
public abstract class ParlorProblem {

    /**
     * Runs the main driver, this will
     *   1. Create the simulation from the input file
     *   2. Add all customers to the parlor and start a thread for all customers
     *   3. output stats to screen
     *
     * @param args Command line arguments
     *             0 - File name
     */
    public void run(String[] args) {
        //create ice-cream parlor
        IceCreamParlor parlor = getParlor();
        //get input
        ParlorInputConfig config = getConfig(parlor, args);
        if(config==null)
            return;

        //add all customers to the parlor
        for(Customer customer : config.getCustomers()) {
            parlor.addCustomer(customer);
            //start a new thread for each customer
            Thread thread = new Thread(customer);
            thread.start();
        }
        //run the parlor simulation
        parlor.start();
        output(parlor);
    }

    /**
     * Produces output as follows
     * Customer Name    Arrival Time       Seats        Leaves
     * C1                   0               5           10
     * ....
     * Cn
     *
     * @param parlor The ice-cream parlor to output stats with
     */
    private void output(IceCreamParlor parlor) {
        //create a table
        System.out.printf("%-15s%-15s%-15s%-15s%n","Customer","Arrives", "Seats","Leaves");
        for(Customer customer : parlor.getCustomers()) {
            System.out.printf("%-15s%-15s%-15s%-15s%n",customer.getName(),customer.getArrivalTime(),customer.getSeatTime(),customer.getLeaveTime());
        }
    }

    /**
     * Get an ice-cream parlor object for the problem. This will depend on the synchronization
     * technique being used. For example if the problem is using monitors this should
     * return a {@link MonitorIceCreamParlor}
     *
     * @return The ice-cream parlor for the problem
     */
    protected abstract IceCreamParlor getParlor();

    /**
     * Whether this problem uses monitors or semaphores for synchronization
     *
     * @return true, if the simulation uses monitors, false if it uses semaphores
     */
    protected abstract boolean isMonitor();

    private ParlorInputConfig getConfig(IceCreamParlor parlor, String[] args) {
        //return getTestCase8(parlor);
        ///*
        if(args.length==0) {
            System.err.println("No input specified!");
            return null;
        }

        //read the input file
        try {
            return new FileParlorInputConfig(parlor,isMonitor(),new File(args[0]));
        } catch (FileNotFoundException e) {
            System.err.println("Could not find file '"+args[0]+"'");
        }
        return null;
        //*/
    }

    /*
     * BELOW ARE TEST CASES. I LEFT THESE IN BECAUSE THE REPORT TOLD ME TO TALK ABOUT
     * TESTING DONE
     */

    /*
     * Test Case 1
     *
     * Input: Example Input Given
     * Output: The output should always be the same as provided in the assignment specification. This should
     * always be deterministic as at no point more than 5 customers arrive at the same time
     *
     */
    private ParlorInputConfig getTestCase1(IceCreamParlor parlor) {
        TestParlorInputConfig config = new TestParlorInputConfig();
        config.addCustomer(parlor,"C1",0,5,isMonitor());
        config.addCustomer(parlor,"C2",0,7,isMonitor());
        config.addCustomer(parlor,"C3",0,8,isMonitor());
        config.addCustomer(parlor,"C4",2,5,isMonitor());
        config.addCustomer(parlor,"C5",3,5,isMonitor());
        config.addCustomer(parlor,"C6",4,3,isMonitor());
        config.addCustomer(parlor,"C7",7,5,isMonitor());
        config.addCustomer(parlor,"C8",10,5,isMonitor());
        return config;
    }

    /*
     * Test Case 2
     *
     * Input: 11 arrive at time = 0 with an eating time of 1
     * Output: The output should have 5 customers enter and leave, then another 5 enter and leave, and then the final 1 enter and leave at t=3
     *
     */
    private ParlorInputConfig getTestCase2(IceCreamParlor parlor) {
        TestParlorInputConfig config = new TestParlorInputConfig();
        config.addCustomer(parlor,"C1",0,1,isMonitor());
        config.addCustomer(parlor,"C2",0,1,isMonitor());
        config.addCustomer(parlor,"C3",0,1,isMonitor());
        config.addCustomer(parlor,"C4",0,1,isMonitor());
        config.addCustomer(parlor,"C5",0,1,isMonitor());
        config.addCustomer(parlor,"C6",0,1,isMonitor());
        config.addCustomer(parlor,"C7",0,1,isMonitor());
        config.addCustomer(parlor,"C8",0,1,isMonitor());
        config.addCustomer(parlor,"C9",0,1,isMonitor());
        config.addCustomer(parlor,"C10",0,1,isMonitor());
        config.addCustomer(parlor,"C11",0,1,isMonitor());
        return config;
    }

    /*
     * Test Case 3
     *
     * Input: 11 arrive at time = 0 with an eating time of 1
     * Output: The output should have 5 customers enter and leave, then another 5 enter and leave, and then the final 1 enter and leave at t=3
     *
     */
    private ParlorInputConfig getTestCase3(IceCreamParlor parlor) {
        TestParlorInputConfig config = new TestParlorInputConfig();
        config.addCustomer(parlor,"C1",0,0,isMonitor());
        config.addCustomer(parlor,"C2",0,0,isMonitor());
        config.addCustomer(parlor,"C3",0,0,isMonitor());
        config.addCustomer(parlor,"C4",0,0,isMonitor());
        config.addCustomer(parlor,"C5",0,1,isMonitor());
        config.addCustomer(parlor,"C6",0,1,isMonitor());
        config.addCustomer(parlor,"C7",0,1,isMonitor());
        config.addCustomer(parlor,"C8",0,1,isMonitor());
        config.addCustomer(parlor,"C9",0,1,isMonitor());
        config.addCustomer(parlor,"C10",0,1,isMonitor());
        config.addCustomer(parlor,"C11",0,1,isMonitor());
        return config;
    }

    /*
     * Test Case 4
     *
     * Input: 4 lots of 5 customers, each lot comes before the previous lot leaves
     * Output: First lot should come in at 0 and leave a 5, next lot enters at 5 and leaves at 10 ...
     * C21 should arrive at time 100, the program should be able to handle the idle time perfectly fine.
     *
     */
    private ParlorInputConfig getTestCase4(IceCreamParlor parlor) {
        TestParlorInputConfig config = new TestParlorInputConfig();
        int count = 1;
        for(int i=0;i<4;i++) {
            for(int j=0;j<5;j++) {
                config.addCustomer(parlor, "C" + count, i * 4, 5, isMonitor());
                count++;
            }
        }

        config.addCustomer(parlor,"C21",100,0,isMonitor());

        return config;
    }

    /*
     * Test Case 5
     *
     * Input: 10000 customers that stay for time i.
     * Output: The important thing is that this does not deadlock, starve or throw an error, the output is ultimately non-deterministic
     *
     */
    private ParlorInputConfig getTestCase5(IceCreamParlor parlor) {
        TestParlorInputConfig config = new TestParlorInputConfig();
        for(int i=1;i<=10000;i++) {
            config.addCustomer(parlor,"C"+i,0,i,isMonitor());
        }

        return config;
    }

    /*
     * Test Case 6
     *
     * Input: 5 customers for 5, another 5 for 5, another 6 for 5, another 6 for 5. This test should not stuff up
     * if there is some kind of gap between the customer arrival.
     * Output: The first 5 enter and leave, the next 5 enter and leave, the next 6 enter, 5 of which are given a seat and 1 must wait,
     *
     */
    private ParlorInputConfig getTestCase6(IceCreamParlor parlor) {
        TestParlorInputConfig config = new TestParlorInputConfig();
        for(int i=1;i<=5;i++) {
            config.addCustomer(parlor,"C"+i,0,5,isMonitor());
        }

        for(int i=6;i<=10;i++) {
            config.addCustomer(parlor,"C"+i,10,5,isMonitor());
        }

        for(int i=11;i<=16;i++) {
            config.addCustomer(parlor,"C"+i,20,5,isMonitor());
        }

        for(int i=17;i<=21;i++) {
            config.addCustomer(parlor,"C"+i,100,5,isMonitor());
        }

        return config;
    }

    /**
     * Test Case 7
     *
     * Input 5: 1000 lots of 5 customers that arrive at i*4 guaranteeing they will queue
     * Output: C1-C5 = 0-5, C5-C10 = 5-10, ... C4995-5000 = 4995-5000
     * where 0-5 means arrive at 0, leave at 5
     *
     *
     */
    private ParlorInputConfig getTestCase7(IceCreamParlor parlor) {
        TestParlorInputConfig config = new TestParlorInputConfig();
        int count = 1;
        for(int i=0;i<1000;i++) {
            for(int j=0;j<5;j++) {
                config.addCustomer(parlor, "C" + count, i * 4, 5, isMonitor());
                count++;
            }
        }

        return config;
    }

    /**
     * Test Case 8
     *
     * Input: 4 customers with eating time 5, 1 with eating time 100
     * Output: first 4 enter and leave, last one leaves at time 100. Next 5 do NOT enter until the last one is finished.
     *
     *
     */
    private ParlorInputConfig getTestCase8(IceCreamParlor parlor) {
        TestParlorInputConfig config = new TestParlorInputConfig();
        int count = 1;
        for(int i=0;i<5;i++) {
            for(int j=0;j<4;j++) {
                config.addCustomer(parlor, "C" + count, i * 4, 5, isMonitor());
                count++;
            }
            config.addCustomer(parlor,"C"+count,i*4,100,isMonitor());
            count++;
        }

        return config;
    }
}
