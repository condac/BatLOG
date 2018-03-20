package com.github.condac.batlog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by burns on 3/20/18.
 */

public class BatteryInfo {


    List<Battery> batteryList;

    public void createBatteryList() {
        // Call when app opens to read all data from files.
        batteryList = null; // clear memory if called multiple times
        batteryList = new ArrayList<Battery>();

    }
}
