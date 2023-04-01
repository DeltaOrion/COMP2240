import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * File: FileSimulationConfig.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * Creates a {@link SimulationConfig} using a file input. The file should be in the format
 * BEGIN
 * DISP: [context switch time]
 * END
 *
 * ID: p1
 * Arrive: [arrive]
 * ExecSize: [exec time]
 * END
 * ... ,
 * EOF
 */
public class FileSimulationConfig implements SimulationConfig {

    private final List<Job> jobs;
    private int contextSwitchTime;

    /**
     * Creates a simulation config using the file
     *
     * @param file the name of the file to read
     * @throws FileNotFoundException Thrown if the file is an error occurs when trying to access the file *cause it does not exist or no permission*
     */
    public FileSimulationConfig(File file) throws FileNotFoundException {
        this.jobs = new ArrayList<>();
        readFile(file);
    }

    private void readFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        //information about the next job
        int execSize = 0;
        int arrive = 0;
        int id = 0;
        while (scanner.hasNext()) {
            //get next token
            String token = scanner.next().toUpperCase(Locale.ROOT);
            switch (token) {
                case "DISP:":
                    this.contextSwitchTime = scanner.nextInt(); //dispatch time is next int
                    scanner.next(); //skip over end
                    break;
                case "END":
                    Job job = new Job(id,arrive,execSize); //process done, add it to the jobs and go to next
                    jobs.add(job);
                    break;
                case "ID:":
                    id = Integer.parseInt(scanner.next().substring(1)); //id number is after the "p"
                    break;
                case "ARRIVE:":
                    arrive = scanner.nextInt(); //arrival time is next integer
                    break;
                case "EXECSIZE:":
                    execSize = scanner.nextInt(); //exec size is next integer
                    break;
                case "EOF": //we are complete!
                    return;
            }
        }
    }

    @Override
    public int getContextSwitchTime() {
        return contextSwitchTime;
    }

    @Override
    public Collection<Job> getJobs() {
        return jobs;
    }
}
