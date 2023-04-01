/**
 * File: ReplacementPolicy.java
 *
 * Author: Jacob Boyce
 * Student Number: c3264527
 * Course: COMP2240
 *
 * Defines a replacement policy. Let G be a page that needs to be placed in memory and F a set of frames. The replacement
 * policy should define which frame in F should be replaced for G to be placed in. The replacement policy does NOT define
 * the nature of F or the resident set, that is instead defined by {@link MediumTermScheduler}. This simply defines which frame
 * to replace.
 */
public interface ReplacementPolicy {

    /**
     * Select which frame in F to replace with G.
     *
     * @param process The process which belongs to the page
     * @param newPage The number of the page to replace
     */
    void replace(Process process, int newPage);

    /**
     * References the selected frame, this allows the policy to update any required reference bits.
     *
     * @param frame The frame which has just been referenced
     */
    void reference(Frame frame);

    /**
     * References the frame holding the given page, this allows the policy to update any required reference bits.
     *
     * @param process The process which the page belongs to
     * @param page The index of the page
     */
    void reference(Process process, int page);

    /**
     * The replacement policy does NOT define the resident set, it merely holds it.
     *
     * @return The resident set which this replacement policy is being enacted on.
     */
    Frame[] getResidentSet();

    /**
     * @return THe name of the replacement policy.
     */
    String getName();
}
