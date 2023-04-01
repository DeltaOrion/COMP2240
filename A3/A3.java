import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * File: A3.java
 *
 * Author: Jacob Boyce
 * Student Number: c3264527
 * Course: COMP2240
 *
 * Main driver program for running the assignment 3 program. This program aims to simulate a single cored processor running
 * a virtual memory scheme. Each process has several 'instructions' These instructions represent virtual memory pages
 * for that process. When an instruction cannot be found in the resident set of the process it will need to replace it
 * depending on the resident set policy and replacement policy.
 */
public class A3 {

    public static void main(String[] args) {
        //read input from the command line interface
        ///*
        InputConfig config = null;
        try {
            config = new CLIInputConfig(args);
        } catch (InvalidConfigException e) {
            //terminate if error occurs
            System.err.println(e.getMessage());
            return;
        }
        //*/
        //InputConfig config = getTestCase1();

        //create simulations for both fixed allocation policy and variable allocation policy.

        List<OSSimulation> simulations = new ArrayList<>();
        simulations.add(new OSSimulation(config.getTimeQuanta(),config.getFrames(), ResidentSetPolicy.FIXED_ALLOCATION_LOCAL));
        simulations.add(new OSSimulation( config.getTimeQuanta(), config.getFrames(),ResidentSetPolicy.VARIABLE_ALLOCATION_GLOBAL));

        //add all the jobs to the simulations
        for(OSSimulation simulation : simulations)
            simulation.addJobs(config.getJobs());

        //run the simulations
        for(OSSimulation simulation : simulations)
            simulation.run();

        //produce output
        output(simulations);
    }

    /**
     * Produces output in the following format
     * PID                      Process Name                        Turnaround Time             #Faults                  Fault Times
     * Unique ID of process     Name of the process (CLI input)     leave time - enter time     amount of page faults    times when faults occured
     *
     * PID  Process Name    Turnaround Time  #Faults    Fault Times
     * 1    process1.txt    34               3          {1,2,3}
     *
     * @param simulations Simulations to produce output. All simulations must be run using {@link OSSimulation#run()}
     */
    private static void output(List<OSSimulation> simulations) {
        String tableFormat = "%-5s%-18s%-17s%-10s%-15s%n";
        for(OSSimulation simulation : simulations) {
            System.out.println(simulation.getName()+":");
            System.out.printf(tableFormat,"PID","Process Name", "Turnaround Time","# Faults","Fault Times");
            for(Process process : simulation.getProcesses()) {
                Collection<Integer> faults = process.getFaults();
                System.out.printf(tableFormat,process.getId(),process.getName(),process.getTurnaroundTime(),faults.size(),toString(faults));
            }
            System.out.println();
            System.out.println();
        }
    }

    /**
     * Converts an integer collection to a string in the following format
     *
     * {a, b, c, d, ... , n}
     *
     * @param collection The collection of integers to convert
     * @return a string representing the colleciton
     */
    private static String toString(Collection<Integer> collection) {
        StringBuilder builder = new StringBuilder("{");
        int count = 0;
        for(Integer fault : collection) {
            count++;
            builder.append(fault);
            if(count<collection.size()) {
                builder.append(", ");
            }
        }
        builder.append("}");
        return builder.toString();
    }

    /**
     *  BELOW ARE SOME OF THE TEST CASES USED. I KEPT THIS FOR THE REPORT WHICH ASKS TO DISCUSS TESTING
     *  USED
     */

    //Input: Same as assignment sample input 1
    //output same as assignment sample output 1
    private static InputConfig getTestCase1() {
        Job j1 = new Job("Process1.txt", 1);
        for(int i=1;i<=5;i++)
            j1.addInstruction(i);

        Job j2 = new Job("Process2.txt", 2);
        for(int i=5;i>=1;i--)
            j2.addInstruction(i);

        Job j3 = new Job("Process3.txt", 3);
        for(int i=0;i<7;i++)
            j3.addInstruction(1);

        Job j4 = new Job("Process4.txt", 4);
        j4.addInstruction(1);
        j4.addInstruction(2);
        j4.addInstruction(3);
        j4.addInstruction(2);
        j4.addInstruction(1);
        j4.addInstruction(4);
        j4.addInstruction(4);
        j4.addInstruction(4);

        TestInputConfig config = new TestInputConfig(3,30);
        config.addJob(j1);
        config.addJob(j2);
        config.addJob(j3);
        config.addJob(j4);

        return config;
    }

    //Input - Volume Test. Add hundreds of processes. Each process has 10 lots of the same instruction three times
    //Output - All processes should page fault at t=0
    //         All page faults should be done at t=6
    //         All 100 processes should run at until t=306
    //         All 100 processes
    private static InputConfig getTestCase2() {
        TestInputConfig config = new TestInputConfig(3,300);
        for(int i=0;i<100;i++) {
            Job job = new Job("Process"+(i+1),i+1);
            for(int j=0;j<10;j++) {
                for(int k=0;k<3;k++) {
                    job.addInstruction(j);
                }
            }
            config.addJob(job);
        }
        return config;
    }

    //input: all processes have the same instruction
    //output: There should be one page fault at the beginning and after that a
    //simple round robin should proceed as no more page faults should occur.
    private static InputConfig getTestCase3() {
        TestInputConfig config = new TestInputConfig(3,11);
        for(int i=0;i<10;i++) {
            Job job = new Job("Process"+(i+1),i+1);
            for(int j=0;j<10;j++) {
                job.addInstruction(1);
            }
            config.addJob(job);
        }
        return config;
    }

    //input: All processes
    private static InputConfig getTestCase4() {
        TestInputConfig config = new TestInputConfig(10000,10);
        for(int i=0;i<10;i++) {
            Job job = new Job("Process"+(i+1),i+1);
            for(int j=0;j<5;j++) {
                job.addInstruction(j);
            }
            config.addJob(job);
        }
        return config;
    }

    //test all little conditions
    //description of where the condition is tested is inline commented
    private static InputConfig getTestCase5() {
        //Issuing a page fault and blocking a process takes no time.
        // So multiple page faults may occur and then another ready process can run immediately at the same time unit
        Job j1 = new Job("Process1",1);
        Job j2 = new Job("Process2",2);
        j1.addInstruction(1);
        j2.addInstruction(2);
        //If multiple process becomes ready at the same time then they will enter the ready queue in the order they became blocked.
        j1.addInstruction(2);
        for(int i=0;i<12;i++)
            j2.addInstruction(2);

        /*
        If a process P1 is finishes its time quantum at t1 and another process P2 becomes unblocked at the same time t1, then the unblocked process
        P2 is added in the ready queue first and the time-quantum expired process P1 is added after that.
         */
        j1.addInstruction(3);
        //If a process becomes ready at time unit t then execution of that process may occur in the same time unit t without any delay (if there is no other process running or waiting in the ready queue).
        j1.addInstruction(1);
        TestInputConfig config = new TestInputConfig(6,4);
        config.addJob(j1);
        config.addJob(j2);

        return config;
    }


    private static void testClock1() {
        Frame[] frames = new Frame[3];
        for(int i=1;i<=3;i++) {
            frames[i-1] = new Frame(i);
        }
        GLOCKAlgorithm algorithm = new GLOCKAlgorithm(frames);
        Process p = new Process("gaming",new Job("gaming",1));
        algorithm.replace(p,1);
        algorithm.reference(p,1);
        algorithm.reference(p,1);
        algorithm.replace(p,2);
        algorithm.reference(p,2);
        algorithm.replace(p,3);
        algorithm.reference(p,3);
        algorithm.replace(p,4);
        algorithm.replace(p,5);
        algorithm.replace(p,1);
        algorithm.replace(p,2);
        algorithm.replace(p,3);
        algorithm.replace(p,4);
    }

    private static void testClock2() {
        Frame[] frames = new Frame[3];
        for(int i=1;i<=3;i++) {
            frames[i-1] = new Frame(i);
        }
        GLOCKAlgorithm algorithm = new GLOCKAlgorithm(frames);
        Process p = new Process("gaming",new Job("gaming",1));
        algorithm.replace(p,1);
        algorithm.replace(p,2);
        algorithm.replace(p,3);
        algorithm.replace(p,4);
        algorithm.replace(p,5);
        algorithm.reference(p,3);
        algorithm.reference(p,4);
        algorithm.replace(p,6);
    }
}
