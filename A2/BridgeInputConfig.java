import java.util.Collection;

/**
 * File: BridgeInputConfig.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * Represents the input specification for the bridge problem (problem1.P1). The input should define the farmers to be used on the bridge. All of the
 * farmers defined in the input configuration will be added to the simulation. The initial starting direction of the farmer should be initialized
 * in the {@link Farmer#Farmer(Bridge, String, TravelDirection)}
 */
public interface BridgeInputConfig {

    /**
     * @return A collection of all the farmers to be used in the simulation.
     */
    Collection<Farmer> getFarmers();
}
