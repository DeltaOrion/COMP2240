/**
 * File: InvalidConfigException.java
 *
 * Author: Jacob Boyce
 * Student Number: c3264527
 * Course: COMP2240
 *
 * Thrown if the input config parameters given are not valid.
 */
public class InvalidConfigException extends Exception {

    public InvalidConfigException(String message) {
        super(message);
    }
}
