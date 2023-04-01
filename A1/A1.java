import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * File: A1.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * Main driver program for running the assignment 1 program. There are several short term scheduling algorithms. Each algorithm behaves different depending on the set of jobs
 * it receives. This program seeks to run a simulation comparing 4 different scheduling algorithms, RR, NRR, FCFS and feedback. This program will do this by simulation each algorithm
 * on the same set of jobs. During the simulation stats about each process will be recorded. These stats will then be outputted on a table for easy reading. The program accepts an input
 * file.
 */
public class A1 {

    /**
     * Runs the assignment one program. This will read the input config from the file specified in the args.
     *
     * @param args args[0] = file name
     */
    public static void main(String[] args) {
        SimulationConfig config = getConfig(args);
        if(config==null)
            return;

        runSimulation(config);
    }

    /**
     * Runs the simulation, produces output in the following format
     *
     * Algorithm Name:
     *   T1: P1
     *   T2: P2
     *   ... for each context switch
     *
     * Process Turnaround Time Waiting Time
     * p1      10              15
     * .... for all process
     *
     * ........... for each algorithm
     *
     * Summary:
     * Algorithm       Average Turnaround Time   Average Waiting Time
     * FCFS            10.00                     6.20
     * ..... for each algorithm
     *
     * @param config The simulation input to be used to run the simulation. This should include the jobs run, and the context
     *               switch time for the dispatchers
     */
    private static void runSimulation(SimulationConfig config) {
        String tableFormat = "%-11s%-20s%-20s%n";
        //set up all of the algorithms
        List<Dispatcher> dispatchers = new ArrayList<>();
        dispatchers.add(new FCFSDispatcher(config.getContextSwitchTime()));
        dispatchers.add(new RRDispatcher(config.getContextSwitchTime()));
        dispatchers.add(new NRRDispatcher(config.getContextSwitchTime()));
        dispatchers.add(new FeedbackDispatcher(config.getContextSwitchTime()));

        //loop through each algorithm
        for(Dispatcher dispatcher : dispatchers) {
            System.out.println(dispatcher.getName()+":");
            //add all of the jobs specified in the input to the dispatcher
            for(Job job : config.getJobs()) {
                dispatcher.addJob(job);
            }

            dispatcher.run();

            System.out.println();
            System.out.format(tableFormat, "Process","Turnaround Time","Waiting Time");
            //after the dispatcher is run show stats about each process
            for(Process process : dispatcher.getProcesses()) {
                System.out.format(tableFormat, process.getDisplayID(),process.getTurnaroundTime(),process.getWaitTime());
            }
            System.out.println();
        }
        //output summary
        System.out.println("Summary");
        tableFormat = "%-15s%-30s%-30s%n";
        System.out.printf(tableFormat,"Algorithm","Average Turnaround Time", "Average Waiting Time");
        for(Dispatcher dispatcher : dispatchers) {
            //generate average stats about each process in the dispatcher
            double averageTurnaroundTime = 0;
            double averageWaitingTime = 0;
            Collection<Process> processes = dispatcher.getProcesses();
            for(Process process : processes) {
                averageTurnaroundTime += process.getTurnaroundTime();
                averageWaitingTime += process.getWaitTime();
            }
            averageTurnaroundTime /= processes.size();
            averageWaitingTime /= processes.size();

            //output these stats nicely in a table
            System.out.printf("%-15s%-30.2f%-30.2f%n",dispatcher.getName(),averageTurnaroundTime,averageWaitingTime);
        }
    }

    private static SimulationConfig getConfig(String[] args) {
        //return getTest5();
        ///*
        String fileName = null;
        //get the name of the file from the args
        if(args.length>0) {
            fileName = args[0];
        } else {
            System.err.println("File name not provided!");
            return null;
        }

        //read the file from the config
        try {
            File file = new File(fileName);
            return new FileSimulationConfig(file);
        } catch (FileNotFoundException e) {
            System.err.println("Unknown file '"+fileName+"'");
            return null;
        }
         //*/
    }

    /**
     *  BELOW ARE SOME OF THE TEST CASES USED. I KEPT THIS FOR THE REPORT WHICH ASKS TO DISCUSS TESTING
     *  USED
     */

    private static SimulationConfig getTest1() {
        //test 1, found in input file, check blocking task in front of small tasks
        TestSimulationConfig config = new TestSimulationConfig(1);
        config.addJob(new Job(1,0,10));
        config.addJob(new Job(2,0,1));
        config.addJob(new Job(3,0,2));
        config.addJob(new Job(4,0,1));
        config.addJob(new Job(5,0,5));
        return config;
    }

    private static SimulationConfig getTest2() {
        //test 2, found in input file, check blocking task in front of small tasks with arrival times
        TestSimulationConfig config = new TestSimulationConfig(1);
        config.addJob(new Job(1,0,10));
        config.addJob(new Job(2,2,1));
        config.addJob(new Job(3,6,2));
        config.addJob(new Job(4,10,1));
        config.addJob(new Job(5,14,5));
        return config;
    }

    private static SimulationConfig getTest3() {
        //Test really long tasks with CPU idle time
        TestSimulationConfig config = new TestSimulationConfig(5);
        config.addJob(new Job(1,0,100));
        config.addJob(new Job(2,1000,100));
        config.addJob(new Job(3,1000,100));
        config.addJob(new Job(4,1000,100));
        config.addJob(new Job(5,1000,100));
        config.addJob(new Job(6,10000,100));
        return config;
    }

    private static SimulationConfig getTest4() {
        //test really short tasks with CPU idle time
        TestSimulationConfig config = new TestSimulationConfig(0);
        config.addJob(new Job(1,0,1));
        config.addJob(new Job(2,2,1));
        config.addJob(new Job(3,6,1));
        config.addJob(new Job(4,10,1));
        config.addJob(new Job(5,14,1));
        return config;
    }

    private static SimulationConfig getTest5() {
        //test tasks with ascending processing time
        TestSimulationConfig config = new TestSimulationConfig(1);
        config.addJob(new Job(1,0,1));
        config.addJob(new Job(2,10,1));
        config.addJob(new Job(3,10,10));
        config.addJob(new Job(4,10,10));
        config.addJob(new Job(5,10,20));
        config.addJob(new Job(6,10,50));
        return config;
    }

    private static SimulationConfig getTest6() {
        //test tasks with equal processing time
        TestSimulationConfig config = new TestSimulationConfig(1);
        config.addJob(new Job(1,0,5));
        config.addJob(new Job(2,2,5));
        config.addJob(new Job(3,4,5));
        config.addJob(new Job(4,6,5));
        config.addJob(new Job(5,8,5));
        config.addJob(new Job(6,10,5));
        return config;
    }
}
