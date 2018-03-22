package com.github.condac.batlog;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by burns on 3/20/18.
 */

public class StuffPacker implements Serializable {

    // THis class creates all that i want to have globaly avaible and i pass the stuffpacker into all functions.
    private static StuffPacker instance;

    public List<Battery> batteryList;
    public List<String> batteryListStringIndex;
    public BatteryGroup batGroup;

    // Restrict the constructor from being instantiated
    private StuffPacker(){
        batGroup = new BatteryGroup();
        batGroup.readJSON();
        createBatteryList();
    }

    public static synchronized StuffPacker getInstance(){
        if(instance==null){
            instance=new StuffPacker();
        }
        return instance;
    }

    public void createBatteryList() {
        // Call when app opens to read all data from files.
        batteryList = null; // clear memory if called multiple times
        batteryList = new ArrayList<Battery>();

        batteryListStringIndex = null;
        batteryListStringIndex = new ArrayList<String>();

        File root = new File(Environment.getExternalStorageDirectory(), "BatLOG");
        File directory = new File(root, "Batteries");
        if (directory.exists()) {
            File[] files = directory.listFiles();
            Log.d("Files", "Size: "+ files.length);
            for (int i = 0; i < files.length; i++) {
                Log.d("Files", "FileName:" + files[i].getName());

                batteryListStringIndex.add(files[i].getName());
                batteryList.add(new Battery(files[i].getName()));
            }
        }
    }

    public Battery getBatteryById(String idIn) {

        for (Battery bat : batteryList ) {
            if (bat.getIdString().equals(idIn)) {
                return bat;
            }
        }

        return batteryList.get(0);

    }

    public int getBatteryIndexById(String idIn) {

        for (int i=0; i<batteryList.size(); i++ ) {
            if (batteryList.get(i).getIdString().equals(idIn)) {
                return i;
            }
        }

        return 0;

    }

    public void askForFileAccess() {

    }


}
