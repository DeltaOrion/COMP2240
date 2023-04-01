import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * File: CLIInputConfig.java
 *
 * Author: Jacob Boyce
 * Student Number: c3264527
 * Course: COMP2240
 *
 * Produces an input config using the command line interface (CLI). This will read the command line arguments (String[] args)
 * and produce the required values. The arguments should be in the following format
 *
 * "Memory Frames" "Time Quanta" "Process1.txt" "Process2.txt" ... "ProcessN.txt"
 " where processj.txt is a file containing the process.
 */
public class CLIInputConfig implements InputConfig {

    private int frames;
    private int timeQuanta;
    private final List<Job> jobs;

    public CLIInputConfig(String[] args) throws InvalidConfigException {
        this.jobs = new ArrayList<>();
        readConfig(args);
    }

    /**
     * Reads the config using the Command Line Interface
     * The arguments should be in the following format
     *
     * "Memory Frames" "Time Quanta" "Process1.txt" "Process2.txt" ... "ProcessN.txt"
     " where processj.txt is a file containing the process.
     *
     * @param args The command line arguments
     * @throws InvalidConfigException If an error occurs when reading the config.
     */
    private void readConfig(String[] args) throws InvalidConfigException {
        //check the required arguments exist
        if(args.length==0)
            throw new InvalidConfigException("Frames not specified at index 0");

        if(args.length==1)
            throw new InvalidConfigException("Round Robin Time Quanta Not specified at index 1");

        //try convert the frames and time quanta, throw an error if otherwise
        try {
            frames = Integer.parseInt(args[0]);
            timeQuanta = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new InvalidConfigException("Argument is not a valid integer");
        }

        //read the files beginning at index 2 if any
        readFiles(Arrays.copyOfRange(args,2,args.length));
    }

    private void readFiles(String[] args) throws InvalidConfigException {
        int count = 1;
        for(String arg : args) {
            //loop through all arguments past index 2
            //create a file if exists
            File file = new File(arg);
            try(Scanner scanner = new Scanner(file)) {
                Job job = new Job(arg,count);
                //loop through all tokens (separated by a space)
                while (scanner.hasNext()) {
                    String token = scanner.next();
                    //if we reach the end token terminate
                    if(token.equalsIgnoreCase("end"))
                        break;

                    //ignore the begin token
                    if(token.equalsIgnoreCase("begin"))
                        continue;

                    //convert each instruction to a number and add it to the job
                    int instruction = Integer.parseInt(token);
                    job.addInstruction(instruction);
                }
                count++;
                jobs.add(job);
            } catch (FileNotFoundException e) {
                throw new InvalidConfigException(e.getMessage());
            } catch (NumberFormatException e) {
                throw new InvalidConfigException("Token in file '"+arg+"' is not a number!");
            }
        }
    }

    @Override
    public List<Job> getJobs() {
        return jobs;
    }

    @Override
    public int getFrames() {
        return frames;
    }

    @Override
    public int getTimeQuanta() {
        return timeQuanta;
    }
}
