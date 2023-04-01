import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * File: FileBridgeInputConfig.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * Represents an input configuration for the bridge problem using a file input. The file should be of the following
 * format
 *
 * N=X, S=X
 *
 * where
 *   -N is the amount of farmers starting northbound
 *   -S is the amount of farmers starting southbound
 *   - X is a number
 */
public class FileBridgeInputConfig implements BridgeInputConfig {

    private final List<Farmer> farmers;

    public FileBridgeInputConfig(Bridge bridge, File file) throws FileNotFoundException {
        this.farmers = new ArrayList<>();
        readFarmers(bridge, file);
    }

    /**
     * Reads all of the farmers from the input file and loads them into a list for future use
     *
     * @param bridge the bridge which all farmers should cross
     * @param file the file to read the farmers from
     * @throws FileNotFoundException if the file trying to be read does not exist
     */
    private void readFarmers(Bridge bridge, File file) throws FileNotFoundException {

        //create a scanner using the auto-closeable try-catch block (this automatically closes the scanner when we leave the try block)
        try(Scanner scanner = new Scanner(file)) {
            //split the N=X, S=X by the comma and space to the individual token
            scanner.useDelimiter(",\\s*");
            //loop through these and add farmers
            while (scanner.hasNext())
                addFarmers(bridge,scanner.next());
        }
    }

    /**
     * Adds the farmer from the input token. The token should be in the form
     *
     * (N/S)=X
     *
     * Where x is a number, N/S is the direction of travel.
     *
     * @param bridge The bridge the farmers need to travel across.
     * @param token The input token as specified above.
     */
    private void addFarmers(Bridge bridge ,String token) {
        TravelDirection direction = TravelDirection.NORTHBOUND;
        //split the input token by the "=" sign.
        //split[0] = direction
        //split[1] = number of farmers
        String[] split = token.split("=");
        if(split[0].equalsIgnoreCase("s"))
            direction = TravelDirection.SOUTHBOUND;

        int farmersCount = Integer.parseInt(split[1]);
        //add a farmer for all farmers
        for(int i=1;i<=farmersCount;i++)
            farmers.add(new Farmer(bridge,direction.getToken()+"_Farmer"+i,direction));
    }

    @Override
    public Collection<Farmer> getFarmers() {
        return Collections.unmodifiableList(farmers);
    }
}
