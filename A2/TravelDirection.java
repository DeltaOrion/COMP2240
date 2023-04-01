/**
 * File: TravelDirection.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * Program Description:
 *
 * Represents a direction a farmer can travel on a bridge, this is either north or south.
 */
public enum TravelDirection {

    NORTHBOUND("North","N"),
    SOUTHBOUND("South","S");

    private final String displayName;
    private final String token;

    /**
     * Creates a new travel direction
     *
     * @param displayName The display name of the direction
     * @param token a 1 letter character to represent the travel direction.
     */
    TravelDirection(String displayName, String token) {
        this.displayName = displayName;
        this.token = token;
    }

    /**
     * @return A display name for this travel direction
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return The 1 letter character representing this direction
     */
    public String getToken() {
        return token;
    }

    /**
     * @return The opposite direction of travel to the current direction
     */
    public TravelDirection opposite() {
        if(this == NORTHBOUND)
            return SOUTHBOUND;

        return NORTHBOUND;
    }
}
