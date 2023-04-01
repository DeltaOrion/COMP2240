import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * File: TestBridgeInputConfig.java
 *
 * Author: Jacob Boyce
 * Course: COMP2240
 * A simple class for making test inputs for the simulation quickly. I left this in for the report as it requires
 * discussion of testing.
 */
public class TestBridgeInputConfig implements BridgeInputConfig {
    List<Farmer> farmers = new ArrayList<>();

    @Override
    public Collection<Farmer> getFarmers() {
        return Collections.unmodifiableList(farmers);
    }

    public void addFarmer(Farmer farmer) {
        this.farmers.add(farmer);
    }
}
