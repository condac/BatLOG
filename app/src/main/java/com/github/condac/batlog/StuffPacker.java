package com.github.condac.batlog;

import java.io.Serializable;

/**
 * Created by burns on 3/20/18.
 */

public class StuffPacker implements Serializable {

    // THis class creates all that i want to have globaly avaible and i pass the stuffpacker into all functions.

    public BatteryGroup batGroup;

    public StuffPacker() {
        batGroup = new BatteryGroup();
        batGroup.readJSON();
    }
}
